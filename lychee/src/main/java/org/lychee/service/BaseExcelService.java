package org.lychee.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lychee.util.StringEnumUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * excel导入导出工具类
 *
 * @author lizhixiao
 */
@Slf4j
public abstract class BaseExcelService<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 限制EXCEL最大行数
     */
    private static final int MAX_LINE = 5000;
    /**
     * 限制EXCEL最大列数
     */
    private static final int MAX_ROW = 500;
    /**
     * 错误模版
     */
    @Getter
    @Setter
    private String errorMsg;

    /**
     * excel解析
     *
     * @param file        文件
     * @param targetClass 目标类
     * @param enumClass   枚举映射类
     * @return list
     */
    protected List<T> excelResolve(MultipartFile file, Object targetClass, Class enumClass) {
        return excelResolve(file, targetClass, enumClass, 0);
    }

    /**
     * excel解析
     *
     * @param file        文件
     * @param targetClass 目标类
     * @param enumClass   枚举映射类
     * @param rowIndex    起始行
     * @return
     */
    protected List<T> excelResolve(MultipartFile file, Object targetClass, Class enumClass, Integer rowIndex) {
        Sheet sheet = createSheet(file);
        if (null == sheet) {
            return null;
        }
        Field[] declaredFields = targetClass.getClass().getDeclaredFields();
        List<String> fieldsList = Collections.synchronizedList(new ArrayList<>());
        List<T> objectsList = Collections.synchronizedList(new ArrayList<T>());
        for (Row row : sheet) {
            if (row.getRowNum() < rowIndex) {
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getRowNum() == rowIndex && fieldsList.size() == 0) {
                String simpleName = targetClass.getClass().getSimpleName();
                cellIterator.forEachRemaining(e ->
                        fieldsList.add(StringEnumUtil.getValue(e.getStringCellValue().trim(), enumClass))
                );
                continue;
            }
            for (; cellIterator.hasNext(); ) {
                Object obj = null;
                try {
                    obj = targetClass.getClass().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    errorMsg = "对象创建创建错误";
                    return null;
                }
                Object finalObj = obj;
                Arrays.stream(declaredFields).forEach(field -> assignment(cellIterator.next(), field, finalObj));
                objectsList.add((T) obj);
            }
        }
        return objectsList;
    }

    private Sheet createSheet(MultipartFile file) {
        String str = this.fileVerify(file);
        if (null == str) {
            return null;
        }
        Workbook workbook = null;
        try {
            InputStream inputStream = file.getInputStream();
            if (str.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            errorMsg = "创建工作本错误";
            return null;
        }
        Sheet sheet = workbook.getSheetAt(0);
        if (!cellVerify(sheet)) {
            return null;
        }
        return sheet;
    }


    /**
     * 文件校验
     */
    private String fileVerify(MultipartFile file) {
        if (null == file) {
            errorMsg = "文件格式识别错误";
            return null;
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            errorMsg = "文件类型识别错误";
            return null;
        }
        if (!".xls".equalsIgnoreCase(suffix) && !suffix.endsWith(".xlsx")) {
            errorMsg = "文件类型识别错误";
            return null;
        }
        return suffix;
    }

    private Boolean cellVerify(Sheet sheet) {
        if (MAX_LINE < sheet.getLastRowNum()) {
            errorMsg = "文件行数超限";
            return false;
        }
        if (MAX_ROW < sheet.getPhysicalNumberOfRows()) {
            errorMsg = "文件列数超限";
            return false;
        }
        return true;
    }

    /**
     * 赋值
     */
    private void assignment(Cell cell, Field field, Object testClass) {
        try {
            field.setAccessible(true);
            field.set(testClass, null == cell ? "" : cell.getStringCellValue());
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("赋值失败!");
        }
    }


}
