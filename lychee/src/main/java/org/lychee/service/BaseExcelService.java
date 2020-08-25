package org.lychee.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lychee.constant.LycheeConstant;
import org.lychee.util.StringEnumUtil;
import org.lychee.web.controller.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * excel导入导出工具类
 *
 * @author lizhixiao
 */
public interface BaseExcelService<T> {
    /**
     * excel xls
     */
    static final String XLS = ".xls";
    /**
     * excel xlsx
     */
    static final String XLSX = ".xlsx";
    /**
     * 限制EXCEL最大行数
     */
    static final int MAX_LINE = 5000;
    /**
     * 限制EXCEL最大列数
     */
    static final int MAX_ROW = 500;

    /**
     * 文件校验
     *
     * @param file
     */
    static Sheet createSheet(MultipartFile file) {
        if (null == file) {
            return null;
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            return null;
        }
        if (!XLS.equalsIgnoreCase(suffix) && !suffix.endsWith(XLSX)) {
            return null;
        }
        Workbook workbook = null;
        try {
            InputStream inputStream = file.getInputStream();
            if (suffix.endsWith(XLS)) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            return null;
        }
        Sheet sheet = workbook.getSheetAt(0);
        if (MAX_LINE < sheet.getLastRowNum() || MAX_ROW < sheet.getPhysicalNumberOfRows()) {
            return null;
        }
        return sheet;
    }


    static String getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            } else {
                DataFormatter dataFormatter = new DataFormatter();
                return dataFormatter.formatCellValue(cell);
            }
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue() + "";
        } else if (cell.getCellType() == CellType.FORMULA) {
            return cell.getCellFormula();
        } else if (cell.getCellType() == CellType.BLANK) {
            return null;
        } else {
            return null;
        }
    }


    /**
     * excel解析
     *
     * @param file        文件
     * @param targetClass 目标类
     * @param enumClass   枚举映射类
     * @return
     */
    default AjaxResult<List<T>> excelResolve(MultipartFile file, Class targetClass, Class enumClass) {
        Sheet sheet = createSheet(file);
        if (null == sheet) {
            return new AjaxResult(LycheeConstant.RESPONSE_ERROR_CODE, "文件创建错误", null);
        }
        List<String> fieldsList = Collections.synchronizedList(new ArrayList<>());
        List<T> objectsList = Collections.synchronizedList(new ArrayList<T>());
        Integer rowIndex = 0;
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            if (fieldsList.size() == 0) {
                cellIterator.forEachRemaining(e ->
                        fieldsList.add(StringEnumUtil.getKey(e.getStringCellValue().trim(), enumClass))
                );
                rowIndex += row.getRowNum();
                continue;
            }
            int i = 0;
            try {
                Object obj = targetClass.newInstance();
                for (; cellIterator.hasNext(); ) {
                    Field field = obj.getClass().getDeclaredField(fieldsList.get(i));
                    i++;
                    field.setAccessible(true);
                    field.set(obj, getCellValue(cellIterator.next()));
                }
                objectsList.add((T) obj);
            } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return new AjaxResult(LycheeConstant.RESPONSE_ERROR_CODE, rowIndex + "行" + i + "列文件解析错误", null);
            }


        }
        return new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, objectsList);
    }

}
