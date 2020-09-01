package com.npn.spring.learning.spring.smallspringboot.model.reports;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;

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

    public void getReportAsXlsxStream (OutputStream stream) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet(reportName);
        Font headerFont = createTableHeaderFont(workbook.createFont());
        Font recordFont = createTableRecordFont(workbook.createFont());
        CellStyle headerStyle = createTableHeaderStyle(workbook.createCellStyle(),headerFont);
        CellStyle recordStyle = createTableRecordStyle(workbook.createCellStyle(),recordFont);
        int bodyRowStartIndex = writeTableHeaderAndReturnNextRowIndex(sheet,headerStyle,0,0);
        ///TODO
    }


    private int writeTableHeaderAndReturnNextRowIndex(SXSSFSheet sheet, CellStyle style, int startRowNum, int startColumnRow) {
        SXSSFRow row = sheet.createRow(startRowNum);
        if (reportTable.getTableColumnNames().size()<=0) return startRowNum;
        int column = startColumnRow;
        for (String tableColumnName : reportTable.getTableColumnNames()) {
            SXSSFCell cell = row.createCell(column++);
            cell.setCellStyle(style);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(tableColumnName);
        }
        return ++startRowNum;
    }

    private CellStyle createTableRecordStyle(CellStyle style, Font font) {
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    private CellStyle createTableHeaderStyle(CellStyle style, Font font) {
        style.setFont(font);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return style;
    }


    private Font createTableHeaderFont(Font font) {
        font.setBold(true);
        font.setFontHeight((short) 14);
        return font;
    }

    private Font createTableRecordFont(Font font) {
        font.setBold(false);
        font.setFontHeight((short) 12);
        return font;
    }





}
