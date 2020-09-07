package org.lychee.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lychee.enums.ResponseCodeEnum;
import org.lychee.util.StringEnumUtil;
import org.lychee.web.controller.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel导入导出工具类
 *
 * @author lizhixiao
 */
public interface IExcelService<T> {
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
     * @return Sheet
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
     * 下载Excel
     *
     * @param response
     * @param fileName
     */
    static void download(HttpServletResponse response, String fileName, HSSFWorkbook wb) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
        ServletOutputStream out = response.getOutputStream();
        bis = new BufferedInputStream(is);
        bos = new BufferedOutputStream(out);
        byte[] buff = new byte[2048];
        int bytesRead;
        while ((-1 != (bytesRead = bis.read(buff, 0, buff.length)))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }

    static HSSFCell setCellStyle(HSSFCellStyle cellStyle, HSSFRow row, HSSFCell cell) {
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        if (row.getRowNum() % 2 == 0) {
//            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        }
        cell.setCellStyle(cellStyle);
        return cell;
    }

    /**
     * excel解析
     *
     * @param file        文件
     * @param targetClass 目标类
     * @param enumClass   枚举映射类
     * @return AjaxResult<List < T>>
     */
    default AjaxResult<List<T>> excelResolve(MultipartFile file, Class targetClass, Class enumClass) {
        Sheet sheet = createSheet(file);
        if (null == sheet) {
            return new AjaxResult(ResponseCodeEnum.ERROR.getKey(), "文件创建错误", null);
        }
        List<String> fieldsList = Collections.synchronizedList(new ArrayList<>());
        List<T> objectsList = Collections.synchronizedList(new ArrayList<T>());
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            if (fieldsList.size() == 0) {
                cellIterator.forEachRemaining(e ->
                        fieldsList.add(StringEnumUtil.getKey(e.getStringCellValue().trim(), enumClass))
                );
                continue;
            }
            int i = 0;
            try {
                Object obj = targetClass.newInstance();
                for (; cellIterator.hasNext(); ) {
                    if ((i + 1) > fieldsList.size()) {
                        break;
                    }
                    Field field = null;
                    try {
                        if (null == fieldsList.get(i)) {
                            i++;
                            continue;
                        }
                        field = obj.getClass().getDeclaredField(fieldsList.get(i));
                        field.setAccessible(true);
                        field.set(obj, getCellValue(cellIterator.next()));
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                objectsList.add((T) obj);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return new AjaxResult(ResponseCodeEnum.ERROR.getKey(), "对象解析创建错误", null);
            }


        }
        return new AjaxResult(ResponseCodeEnum.ERROR.getKey(), null, objectsList);
    }

    /**
     * 数据导出Excel
     *
     * @param sheetName 文件名
     * @param enumClass 映射枚举
     * @param listData  数据
     */
    default void exportExport(String sheetName, Class enumClass, List<T> listData, HttpServletResponse response) throws IOException, IllegalAccessException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 0;
        int colNum = 0;
        HSSFRow row = sheet.createRow(rowNum);
        HSSFCell cell = null;
        Map<String, String> titleMap = StringEnumUtil.enumToMap(enumClass);
        for (String val : titleMap.values()) {
            cell = row.createCell(colNum);
            cell.setCellValue(val);
            cell = setCellStyle(cellStyle, row, cell);
            row.setHeightInPoints(20);
            colNum++;
        }
        rowNum++;
        colNum = 0;

        for (int i = 0; i < listData.size(); i++) {
            cell = setCellStyle(cellStyle, row, cell);
            Object data = listData.get(i);
            Field[] fields = data.getClass().getDeclaredFields();
            row = sheet.createRow(rowNum);
            for (String key : titleMap.keySet()) {
                Object value = null;
                cell = row.createCell(colNum);
                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    if (fields[j].getName().equals(key)) {
                        value = fields[j].get(data);
                    }
                    if ("id".equals(key)) {
                        value = rowNum;
                    }
                }
                if (value != null) {
                    cell.setCellValue(value.toString());
                } else {
                    cell.setCellValue("");
                }
                sheet.autoSizeColumn(colNum, true);
                cell.setCellStyle(cellStyle);
                row.setHeightInPoints(20);
                colNum++;
            }
            rowNum++;
            colNum = 0;
        }
        download(response, sheetName, wb);
    }
}
