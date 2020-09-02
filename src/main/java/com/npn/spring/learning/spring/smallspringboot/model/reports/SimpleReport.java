package com.npn.spring.learning.spring.smallspringboot.model.reports;


import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Класс представляющий простой отчет
 */
public class SimpleReport {
    private String reportName = "SimpleReport";
    private final SimpleReportTable reportTable;


    public SimpleReport(SimpleReportTable reportTable) {
        this.reportTable = reportTable;
    }

    public SimpleReportTable getReportTable() {
        return reportTable;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * Записывает xlsx отчет в переданный OutputStream
     *
     * @param stream OutputStream куда записывается отчет
     * @throws IOException при ошибках записи
     */
    public void getReportAsXlsxStream (OutputStream stream) throws IOException {
        try(SXSSFWorkbook workbook = new SXSSFWorkbook();) {
            SXSSFSheet sheet = workbook.createSheet(reportName);
            Font headerFont = createTableHeaderFont(workbook.createFont());
            Font recordFont = createTableRecordFont(workbook.createFont());
            CellStyle headerStyle = createTableHeaderStyle(workbook.createCellStyle(),headerFont);
            CellStyle recordStyle = createTableRecordStyle(workbook.createCellStyle(),recordFont);
            int bodyRowStartIndex = writeTableHeaderAndReturnNextRowIndex(sheet,headerStyle,0,0);
            writeTableBody(sheet,recordStyle,bodyRowStartIndex,0);
            workbook.write(stream);
        }
    }



    private int writeTableBody(final SXSSFSheet sheet,final CellStyle style,final int startRowNum,final int startColumnRow) {
        int rowIndex = startRowNum;
        for (List<ReportTableCell> tableRowCells : reportTable.getTableRows()) {
            SXSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = startColumnRow;
            for (ReportTableCell cell : tableRowCells) {
                SXSSFCell fileCell = row.createCell(columnIndex++);
                fileCell.setCellStyle(style);
                if (!cell.isBlank()){
                    switch (cell.getCellType()) {
                        case NUMERIC: fileCell.setCellValue(Double.parseDouble(cell.getValue()));
                            break;
                        case BOOLEAN: fileCell.setCellValue(Boolean.parseBoolean(cell.getValue()));
                            break;
                        default: fileCell.setCellValue(cell.getValue());
                            break;
                    }
                }

            }
        }
        return rowIndex;
    }



    private int writeTableHeaderAndReturnNextRowIndex(final SXSSFSheet sheet,final CellStyle style,final int startRowNum,final int startColumnRow) {
        SXSSFRow row = sheet.createRow(startRowNum);
        if (reportTable.getTableColumnNames().size()<=0) return startRowNum;
        int columnIndex = startColumnRow;
        for (String tableColumnName : reportTable.getTableColumnNames()) {
            SXSSFCell cell = row.createCell(columnIndex++);
            cell.setCellStyle(style);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(tableColumnName);
        }
        return startRowNum+1;
    }

    private CellStyle createTableRecordStyle(CellStyle style, Font font) {
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setShrinkToFit(true);
        return style;
    }

    private CellStyle createTableHeaderStyle(CellStyle style, Font font) {
        style.setFont(font);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.LESS_DOTS);
        style.setShrinkToFit(true);
        return style;
    }


    private Font createTableHeaderFont(Font font) {
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        return font;
    }

    private Font createTableRecordFont(Font font) {
        font.setBold(false);
        font.setFontHeightInPoints((short) 12);
        return font;
    }





}
