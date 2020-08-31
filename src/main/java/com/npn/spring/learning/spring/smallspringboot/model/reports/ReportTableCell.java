package com.npn.spring.learning.spring.smallspringboot.model.reports;

import java.util.Objects;

public class ReportTableCell {
    private final ReportTableFieldType cellType;
    private final String value;

    public ReportTableCell() {
        cellType = ReportTableFieldType.STRING;
        value = "";
    }

    public ReportTableCell(final ReportTableFieldType cellType,final String value) {
        this.cellType = cellType;
        this.value = value==null? "": value;
    }

    public ReportTableCell(final String value) {
        cellType = ReportTableFieldType.STRING;
        this.value = value==null? "": value;
    }

    public ReportTableFieldType getCellType() {
        return cellType;
    }

    public String getValue() {
        return value;
    }

    public boolean isBlank() {
        return value==null || value.isBlank();
    }

    public ReportTableCell copyWithType(final ReportTableFieldType type){
        return new ReportTableCell(type,this.getValue());
    }

    public enum ReportTableFieldType {
        STRING, NUMERIC, FORMULA, BOOLEAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportTableCell that = (ReportTableCell) o;
        return cellType == that.cellType &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellType, value);
    }
}
