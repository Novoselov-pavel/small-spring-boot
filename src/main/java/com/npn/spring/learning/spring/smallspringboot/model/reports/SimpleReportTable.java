package com.npn.spring.learning.spring.smallspringboot.model.reports;

import java.util.*;

/**
 * Класс описывающий простую таблицу для отчета
 */
public class SimpleReportTable {
    private final String tableName;
    private final List<String> tableColumnNames = new ArrayList<>();
    private final List<ReportTableRow> tableRows = new ArrayList<>();

    public SimpleReportTable() {
        tableName = "NewSimpleReportTable";
    }

    public SimpleReportTable(String tableName) {
        this.tableName = tableName;
    }

    public SimpleReportTable addColumn(final String columnName){
        tableColumnNames.add(columnName);
        return this;
    }

    public SimpleReportTable removeColumn(final int columnIndex) {
        if (columnIndex<0 || columnIndex>=tableColumnNames.size()) throw new IndexOutOfBoundsException("Invalid column index");
        tableColumnNames.remove(columnIndex);
        tableRows.forEach(x->x.removeColumn(columnIndex));
        return this;
    }

    public SimpleReportTable addRows(List<Map<String, ReportTableCell>> values) {
        if (values.size()>0){
            values.get(0).keySet().forEach(x->tableColumnNames.add(x));
        }
        values.forEach(x->tableRows.add(new ReportTableRow(x.values())));
        return this;
    }




    public class ReportTableRow {
        private final List<ReportTableCell> cellList = new ArrayList<>();

        public ReportTableRow() {
        }

        public ReportTableRow(Collection<ReportTableCell> values) {
            int columnIndex = 0;
            for (ReportTableCell value : values) {
                addCell(columnIndex++,value);
            }
        }

        public void addCell(final int columnIndex, final ReportTableCell.ReportTableFieldType type, final String value){
            this.addCell(columnIndex,new ReportTableCell(type,value));
        }

        public void addCell(final int columnIndex, final String value) {
            this.addCell(columnIndex, ReportTableCell.ReportTableFieldType.STRING,value);
        }

        public void addCell(final int columnIndex, final ReportTableCell value) {
            if (columnIndex<0 || columnIndex >= tableColumnNames.size()) {
                throw new IndexOutOfBoundsException("Illegal columnIndex: "+columnIndex);
            }
            if (cellList.size()<=columnIndex) {
                for (int i = cellList.size(); i <=cellList.size(); i++) {
                    cellList.add(new ReportTableCell());
                }
            }
            cellList.add(columnIndex,value);
        }


        public List<ReportTableCell> getCellList() {
            return cellList;
        }

        public ReportTableCell getCell(final int columnIndex) {
            if (columnIndex<0) throw new IndexOutOfBoundsException("Column index is less than zero");
            return cellList.size()<=columnIndex? new ReportTableCell():cellList.get(columnIndex);
        }

        public void removeColumn(final int columnIndex) {
            if (columnIndex>0 && columnIndex<cellList.size()) {
                cellList.remove(columnIndex);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReportTableRow that = (ReportTableRow) o;
            return Objects.equals(cellList, that.cellList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cellList);
        }
    }



}
