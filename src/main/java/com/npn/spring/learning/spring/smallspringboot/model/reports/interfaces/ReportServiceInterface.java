package com.npn.spring.learning.spring.smallspringboot.model.reports.interfaces;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;

public interface ReportServiceInterface {

    Resource getReport(String reportDocType, String reportName) throws IOException;

    URL getReportFile(String reportDocType, String reportName) throws IOException;

}
