package com.npn.spring.learning.spring.smallspringboot.model.reports;


import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;

/**
 * Класс представляющий простой отчет
 */
public class SimpleReport {
    private final String FOOTER_TEXT = "{PAGE \\* MERGEFORMAT} \\ {NUMPAGES \\* MERGEFORMAT}";
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

    /**
     * Записывает docx отчет в переданный OutputStream
     *
     * @param stream OutputStream куда записывается отчет
     * @throws IOException при ошибках записи
     */
    public void getReportASDocxStream(OutputStream stream) throws IOException {
        try(XWPFDocument document = new XWPFDocument()) {
            setDocxA4PageSizeAndLandscape(document);
            createDocxHeader(document);
            createDocxFooter(document);
            writeTableInDocx(document);

            document.write(stream);
        }
    }


    /**
     * Устанавливает для нового файла альбомную ориентацию и A4 размер страницы
     * @param doc XWPFDocument
     */
    private void setDocxA4PageSizeAndLandscape(XWPFDocument doc) {
        CTDocument1 document = doc.getDocument();
        CTBody body = document.getBody();

        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();

        if(!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pageSize = section.getPgSz();
        pageSize.setW(BigInteger.valueOf(16840));
        pageSize.setH(BigInteger.valueOf(11900));
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
    }

    private void createDocxHeader(XWPFDocument doc) {
        XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT);
        XWPFParagraph paragraph = header.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(reportName);
    }

    private void createDocxFooter(XWPFDocument doc) {
        XWPFFooter footer = doc.createFooter(HeaderFooterType.DEFAULT);
        XWPFParagraph paragraph = footer.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(" of ");
        paragraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
    }


    private void writeTableInDocx(XWPFDocument doc) {
        XWPFTable table = doc.createTable(1,reportTable.columnNumber());
        XWPFTableRow row = table.getRow(0);
        row.setRepeatHeader(true);
        int columnIndex = 0;
        for (XWPFTableCell cell : row.getTableCells()) {
            fillTableHeaderCell(cell,columnIndex++);
        }
        reportTable.getTableRows().forEach(x->fillRow(x,table.createRow()));

    }

    private void fillRow(List<ReportTableCell> list, XWPFTableRow row) {
        int columnIndex = 0;
        for (XWPFTableCell cell : row.getTableCells()) {
            fillTableBodyCell(cell,columnIndex++, list);
        }
    }

    private void fillTableBodyCell(XWPFTableCell cell, int columnIndex, List<ReportTableCell> list){
        XWPFRun run = cell.getParagraphArray(0).createRun();
        run.setFontSize(10);
        run.setBold(false);
        run.setFontFamily("Times New Roman");
        run.setText(list.get(columnIndex).getValue());
    }


    private void fillTableHeaderCell(XWPFTableCell cell, int columnIndex){
        XWPFRun run = cell.getParagraphArray(0).createRun();
        run.setFontSize(14);
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setText(reportTable.getTableColumnNames().get(columnIndex));
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
