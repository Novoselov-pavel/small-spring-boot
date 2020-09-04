package com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;

/**
 * Интерфейс работы с отчетами
 */
public interface ReportServiceInterface {

    /**
     * Возвращает отчет как {@link Resource}
     *
     * @param reportDocType тиn файла вывода отчета
     * @param reportName имя отчета, определяет содержание отчета
     * @return отчет как {@link Resource}
     * @throws IOException при ошибке файловой системы
     * @throws IllegalArgumentException при неверных параметрах reportDocType, reportName
     */
    Resource getReport(String reportDocType, String reportName) throws IOException;

    /**
     * Возвращает отчет как {@link URL}
     *
     * @param reportDocType тиn файла вывода отчета
     * @param reportName имя отчета, определяет содержание отчета
     * @return отчет как {@link Resource}
     * @throws IOException при ошибке файловой системы
     * @throws IllegalArgumentException при неверных параметрах reportDocType, reportName
     */
    URL getReportFile(String reportDocType, String reportName) throws IOException;

}
