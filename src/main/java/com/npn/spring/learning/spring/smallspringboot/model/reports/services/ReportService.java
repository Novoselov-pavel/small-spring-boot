package com.npn.spring.learning.spring.smallspringboot.model.reports.services;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.reports.SimpleReport;
import com.npn.spring.learning.spring.smallspringboot.model.reports.SimpleReportTable;
import com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces.ReportServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

/**
 * Служба работы с отчетами
 */
@Service
public class ReportService implements ReportServiceInterface {

    @Autowired
    UserServiceInterface userService;

    /**
     * Возвращает отчет как {@link Resource}
     *
     * @param reportDocType тиn файла вывода отчета
     * @param reportName имя отчета, определяет содержание отчета
     * @return отчет как {@link Resource}
     * @throws IOException при ошибке файловой системы
     * @throws IllegalArgumentException при неверных параметрах reportDocType, reportName
     */
    @Override
    public Resource getReport(final String reportDocType, final String reportName) throws IOException {
        FileUrlResource resource = new FileUrlResource(getReportFile(reportDocType, reportName));
        return resource;
    }

    /**
     * Возвращает отчет как {@link URL}
     *
     * @param reportDocType тиn файла вывода отчета
     * @param reportName имя отчета, определяет содержание отчета
     * @return отчет как {@link Resource}
     * @throws IOException при ошибке файловой системы
     * @throws IllegalArgumentException при неверных параметрах reportDocType, reportName
     */
    @Override
    public URL getReportFile(String reportDocType, String reportName) throws IOException {
        AvailableReports report = AvailableReports.getReport(reportName);
        AvailableReportsFormats format = AvailableReportsFormats.getFormat(reportDocType);

        if (report==AvailableReports.UNDEFINED || format == AvailableReportsFormats.UNDEFINED) {
            throw new IllegalArgumentException("Type of report or format of report document  wasn't recognise");
        }
        Path path = Files.createTempFile(getTempFilePrefix(),"."+format.getReportFormat());

        switch (report) {
            case USERS_SIMPLE_REPORT:
                return getSimpleReportURL(new SimpleReportTable().addTableData(userService
                        .findAll()
                        .stream()
                        .map(x->x.getReportTableEntity())
                        .collect(Collectors.toList()))
                        ,path,format);
        }
        return null;
    }

    private URL getSimpleReportURL(SimpleReportTable table, Path filePath, AvailableReportsFormats formats) throws IOException {
        try (OutputStream stream = Files.newOutputStream(filePath)) {
            filePath.toFile().deleteOnExit(); /// вообще для коммерческого использования данный вариант не годится
            /// надо создавать систему работы с временными файлами для их очистки (например через PhantomReference), либо работать с Resource через
            ///in-memory имплементации
            SimpleReport report = new SimpleReport(table);
            switch (formats) {
                case DOCX:
                    report.getReportASDocxStream(stream);
                    break;
                case XLSX:
                    report.getReportAsXlsxStream(stream);
            }
            stream.flush();
            return filePath.toUri().toURL();
        }
    }

    private String getTempFilePrefix() {
        Calendar calendar = new GregorianCalendar();
        StringBuilder builder = new StringBuilder(12);
        builder
                .append(calendar.get(Calendar.YEAR))
                .append("-")
                .append(calendar.get(Calendar.MONTH))
                .append("-")
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append("-");
        return builder.toString();
    }

    /**
     * Определяет поддерживаемые форматы выходных файлов отчетов
     */
    public enum AvailableReportsFormats{
        XLSX ("xlsx"), DOCX("docx"), UNDEFINED("undefined");

        private final String reportFormat;

        AvailableReportsFormats(String reportFormat) {
            this.reportFormat = reportFormat;
        }

        public String getReportFormat() {
            return reportFormat;
        }

        /**
         * Возвращает AvailableReportsFormats соответствующий переданному параметру адреса, или UNDEFINED если значение не найдено
         *
         * @param value параметр адреса в запросе к серверу
         * @return соответсвующий AvailableReportsFormats или UNDEFINED
         */
        public static AvailableReportsFormats getFormat (final String value) {
            return Arrays.asList(AvailableReportsFormats.values())
                    .stream()
                    .filter(x->x.reportFormat.equalsIgnoreCase(value))
                    .findFirst()
                    .orElse(AvailableReportsFormats.UNDEFINED);
        }
    }

    /**
     * Определяет поддерживаемые форматы отчетов
     */
    public enum AvailableReports {
        USERS_SIMPLE_REPORT ("users"), UNDEFINED("undefined");

        private final String reportName;

        AvailableReports(String reportName) {
            this.reportName = reportName;
        }

        public String getReportName() {
            return reportName;
        }

        /**
         * Возвращает AvailableReports соответствующий переданному параметру адреса, или UNDEFINED если значение не найдено
         *
         * @param value параметр адреса в запросе к серверу
         * @return соответсвующий AvailableReports или UNDEFINED
         */
        public static AvailableReports getReport (final String value) {
            return Arrays.asList(AvailableReports.values())
                    .stream()
                    .filter(x->x.reportName.equalsIgnoreCase(value))
                    .findFirst()
                    .orElse(AvailableReports.UNDEFINED);
        }
    }
}
