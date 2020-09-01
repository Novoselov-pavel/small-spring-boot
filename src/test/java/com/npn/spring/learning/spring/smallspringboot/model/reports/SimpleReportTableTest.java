package com.npn.spring.learning.spring.smallspringboot.model.reports;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SimpleReportTableTest {

    private List<Map<String,ReportTableCell>> getTestList() {
        Map<String,ReportTableCell> map1 = new LinkedHashMap<>();
        map1.put("column1", new ReportTableCell("val1"));
        map1.put("column2", new ReportTableCell("val2"));
        map1.put("column3", new ReportTableCell("moveVal"));

        Map<String,ReportTableCell> map2 = new LinkedHashMap<>();
        map2.put("column1", new ReportTableCell("val3"));
        map2.put("column2", new ReportTableCell("val4"));

        List<Map<String,ReportTableCell>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);
        return  list;
    }

    @Test
    void addTableData() {
        SimpleReportTable reportTable = new SimpleReportTable();
        reportTable.addTableData(getTestList());
        assertEquals(2,reportTable.getTableRows().size());
        assertEquals(3,reportTable.getTableColumnNames().size());
        assertEquals("val1",reportTable.getCellValue(0,0));
        assertEquals("",reportTable.getCellValue(2,1));

        reportTable.addColumn("column2");
        assertEquals("",reportTable.getCellValue(3,1));
        assertThrows(IndexOutOfBoundsException.class, ()->reportTable.getCellValue(-1,0));
        assertThrows(IndexOutOfBoundsException.class, ()->reportTable.getCellValue(4,0));
        assertThrows(IndexOutOfBoundsException.class, ()->reportTable.getCellValue(0,-1));
        assertThrows(IndexOutOfBoundsException.class, ()->reportTable.getCellValue(0,2));
    }

    @Test
    void removeColumn() {
        SimpleReportTable reportTable = new SimpleReportTable();
        reportTable.addTableData(getTestList());
        reportTable.removeColumn(1);
        assertEquals(2,reportTable.getTableRows().size());
        assertEquals(2,reportTable.getTableColumnNames().size());
        assertEquals("moveVal",reportTable.getCellValue(1,0));
        assertEquals("",reportTable.getCellValue(1,1));

        assertThrows(IndexOutOfBoundsException.class, ()->reportTable.getCellValue(2,0));
    }


    @Test
    void getTableRows() {
        SimpleReportTable reportTable = new SimpleReportTable();
        reportTable.addTableData(getTestList());
        List<List<ReportTableCell>> cellList = reportTable.getTableRows();
        assertEquals(reportTable.rowsNumber(),cellList.size());
        assertEquals(reportTable.columnNumber(),cellList.get(reportTable.rowsNumber()-1).size());
        assertEquals(new ReportTableCell(),cellList.get(reportTable.rowsNumber()-1).get(reportTable.columnNumber()-1));
    }
}