package org.lychee.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lychee.web.util.StringEnumUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * excel导入导出工具类
 */
@Slf4j
public abstract class ExcelService<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 限制EXCEL最大行数
     */
    private static final int MAX_LINE = 2000;
    /**
     * 限制EXCEL最大列数
     */
    private static final int MAX_ROW = 100;
    /**
     * 错误模版
     */
    @Getter
    @Setter
    private String errorMsg;

    /**
     * 导入
     *
     * @param file      excel文件
     * @param testClass 解析类
     * @param enumT     枚举映射类
     */
    protected List<T> excelResolve(MultipartFile file, Object testClass, Class enumT) {
        return excelResolve(file, testClass, enumT, 0);
    }

    /**
     * 导入
     *
     * @param file      excel文件
     * @param testClass 解析类
     * @param enumT     枚举映射类
     * @param roleIndex 表头索引
     */
    protected List<T> excelResolve(MultipartFile file, Object testClass, Class enumT, Integer roleIndex) {
        Sheet sheet = createSheet(file);
        if (null == sheet) {
            return null;
        }
        Field[] declaredFields = testClass.getClass().getDeclaredFields();
        List<String> fieldsList = Collections.synchronizedList(new ArrayList<>());
        List<T> objectsList = Collections.synchronizedList(new ArrayList<T>());
        for (Row row : sheet) {
            if (row.getRowNum() < roleIndex) {
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getRowNum() == roleIndex && fieldsList.size() == 0) {
                String simpleName = testClass.getClass().getSimpleName();
                cellIterator.forEachRemaining(e ->
                        fieldsList.add(StringEnumUtil.getValue(e.getStringCellValue().trim(), enumT))
                );
                continue;
            }
            for (; cellIterator.hasNext(); ) {
                Object obj = null;
                try {
                    obj = testClass.getClass().newInstance();
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
            errorMsg = "文件识别错误";
            return null;
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            errorMsg = "文件识别错误";
            return null;
        }
        if (!suffix.equalsIgnoreCase(".xls") && !suffix.endsWith(".xlsx")) {
            errorMsg = "文件识别错误";
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
