package com.npn.spring.learning.spring.smallspringboot.model.reports;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс описывающий простую таблицу для отчета
 */
public class SimpleReportTable {
    private final String tableName;
    private final List<String> tableColumnNames = new ArrayList<>();
    private final List<ReportTableRow> tableRows = new ArrayList<>();

    /**
     * Создает экземпляр простой таблицы с дефолтным именем NewSimpleReportTable
     */
    public SimpleReportTable() {
        tableName = "NewSimpleReportTable";
    }

    /**
     * Создает экземпляр простой таблицы с указанным именем
     * @param tableName имя таблицы
     */
    public SimpleReportTable(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Добавляет колонку в конец таблицы
     * @param columnName имя колонки
     * @return текущий экземпляр SimpleReportTable
     */
    public SimpleReportTable addColumn(final String columnName){
        tableColumnNames.add(columnName);
        return this;
    }

    /**
     * Удаляет колонку с указанным индексом в таблице вместе с данными.
     * @param columnIndex индекс колонки в таблице
     * @return текущий экземпляр SimpleReportTable
     * @throws IndexOutOfBoundsException если индекс вне таблицы
     */
    public SimpleReportTable removeColumn(final int columnIndex) {
        if (columnIndex<0 || columnIndex>=tableColumnNames.size()) throw new IndexOutOfBoundsException("Invalid column index");
        tableColumnNames.remove(columnIndex);
        tableRows.forEach(x->x.removeColumn(columnIndex));
        return this;
    }


    /**
     * Возвращает текстовое представление данных таблицы по адресу. При отсутствии данных, выдается пустая строка.
     *
     * @param columnIndex индекс колонки в таблице
     * @param rowIndex индекс строки в таблице
     * @return  текущий экземпляр SimpleReportTable
     * @throws IndexOutOfBoundsException если индексы вне таблицы
     */
    public String getCellValue(final int columnIndex, final int rowIndex){
        if (columnIndex<0 || rowIndex<0 || columnIndex>=tableColumnNames.size() || rowIndex >= tableRows.size()) {
            throw  new IndexOutOfBoundsException("Index out of range");
        }
        return tableRows.get(rowIndex).getCell(columnIndex).getValue();
    }

    /**
     * Возвращает список строк тела таблицы. Строка представлена как список из {@link ReportTableCell}.
     * При отсутствии данных возвратит пустой список.
     * @return тело таблицы.
     */
    public List<List<ReportTableCell>> getTableRows() {
        return tableRows
                .stream()
                .map(x->x.getCellList())
                .collect(Collectors.toList());
    }

    /**
     * Возвращает количество строк в таблице
     * @return количество строк в таблице
     */
    public int rowsNumber() {
        return tableRows.size();
    }

    /**
     * Возвращает количество столбцов в таблице     *
     * @return количество столбцов в таблице
     */
    public int columnNumber() {
        return tableColumnNames.size();
    }

    /**
     * Добавляет данные в текущую таблицу.
     * Даныне представлены как список из Map<String, ReportTableCell>, где ключи первой строки - будут добавлены как
     * колонки в текущую таблицу, а значения - будут добавлены в данную таблицу.
     *
     * @param values
     * @return
     */
    public SimpleReportTable addTableData(List<Map<String, ReportTableCell>> values) {
        int startColumnIndex = tableColumnNames.size();
        if (values.size()>0){
            values.get(0).keySet().forEach(x->tableColumnNames.add(x));
        }
        values.forEach(x->tableRows.add(new ReportTableRow(x.values(),startColumnIndex)));
        return this;
    }

    /**
     * Возвращает немодифицирующийся список колонок в таблице
     *
     * @return немодифицирующийся список колонок в таблице
     */
    public List<String> getTableColumnNames() {
        return Collections.unmodifiableList(tableColumnNames);
    }


    private class ReportTableRow {
        private final List<ReportTableCell> cellList = new ArrayList<>();

        public ReportTableRow() {
        }

        public ReportTableRow(final Collection<ReportTableCell> values, final int startColumnIndex) {
            int columnIndex = startColumnIndex;
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
            if (cellList.size()<columnIndex) {
                for (int i = cellList.size(); i <columnIndex; i++) {
                    cellList.add(new ReportTableCell());
                }
            }
            cellList.add(columnIndex,value);
        }

        public ReportTableCell getCell(final int columnIndex) {
            if (columnIndex<0) throw new IndexOutOfBoundsException("Column index is less than zero");
            return columnIndex>=cellList.size()? new ReportTableCell():cellList.get(columnIndex);
        }

        public void removeColumn(final int columnIndex) {
            if (columnIndex>0 && columnIndex<cellList.size()) {
                cellList.remove(columnIndex);
            }
        }

        public List<ReportTableCell> getCellList(){
            List<ReportTableCell> list = new ArrayList<>();
            for (int i = 0; i < tableColumnNames.size(); i++) {
                list.add(getCell(i));
            }
            return list;
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
