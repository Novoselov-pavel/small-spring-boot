package com.npn.spring.learning.spring.smallspringboot.model.reports;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimpleReportTest {

    private List<Map<String,ReportTableCell>> getTestList() {
        Map<String,ReportTableCell> map1 = new LinkedHashMap<>();
        map1.put("column1", new ReportTableCell("val1"));
        map1.put("column2", new ReportTableCell("val2"));
        map1.put("column3", new ReportTableCell(ReportTableCell.ReportTableFieldType.NUMERIC,"1.005"));

        Map<String,ReportTableCell> map2 = new LinkedHashMap<>();
        map2.put("column1", new ReportTableCell(ReportTableCell.ReportTableFieldType.BOOLEAN,"true"));
        map2.put("column2", new ReportTableCell("val4"));

        List<Map<String,ReportTableCell>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);
        return  list;
    }

    private SimpleReport getTestReport() {
        SimpleReportTable reportTable = new SimpleReportTable();
        reportTable.addTableData(getTestList());
        return new SimpleReport(reportTable);
    }

    /**
     * Полноценное автоматическое тестирование данного метода для некоммерческой разработки не имеет смысла,
     * из-за необходимости включения большого количества кода.
     */
    @Test
    void getReportAsXlsxStream() {
        SimpleReport simpleReport = getTestReport();
        try {
            Path outputFile = Files.createTempFile(null,".xlsx");
            try (OutputStream stream = Files.newOutputStream(outputFile);) {
                simpleReport.getReportAsXlsxStream(stream);
                stream.flush();
            }
            Files.deleteIfExists(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Полноценное автоматическое тестирование данного метода для некоммерческой разработки не имеет смысла,
     * из-за необходимости включения большого количества кода.
     */
    @Test
    void getReportASDocxStream() {
        SimpleReport simpleReport = getTestReport();
        try{
            Path outputFile = Files.createTempFile(null,".docx");
            try(OutputStream stream = Files.newOutputStream(outputFile)) {
                simpleReport.getReportASDocxStream(stream);
                stream.flush();
            }
            Files.deleteIfExists(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}