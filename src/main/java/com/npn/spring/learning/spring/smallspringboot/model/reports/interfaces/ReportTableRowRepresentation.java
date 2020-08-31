package com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces;

import com.npn.spring.learning.spring.smallspringboot.model.reports.ReportTableCell;

import java.util.Map;

/**
 * Возвращает класс в виде его представления для таблицы отчета
 */
@FunctionalInterface
public interface ReportTableRowRepresentation {

    /**
     * Возвращает класс в виде его представления для таблицы отчета
     *
     * @return Map<String, ReportTableCell> где, ключ - имя заголовка столбца таблицы, значение - объект {@link ReportTableCell}
     * определяющий представление поля в отчете
     *
     */
    Map<String, ReportTableCell> getReportTableEntity();
}
