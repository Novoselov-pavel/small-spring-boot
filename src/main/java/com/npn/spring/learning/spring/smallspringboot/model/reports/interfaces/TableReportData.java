package com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces;

import com.npn.spring.learning.spring.smallspringboot.model.reports.SimpleReportTable;

/**
 * Интерфейс для получения таблицы для отчета
 */
public interface TableReportData {
    /**
     * Возвращает представление объекта как простой таблицы для отчета
     * @return {@link SimpleReportTable}
     */
    SimpleReportTable getReportTable();
}
