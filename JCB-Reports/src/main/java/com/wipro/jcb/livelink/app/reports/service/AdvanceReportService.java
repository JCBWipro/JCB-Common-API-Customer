package com.wipro.jcb.livelink.app.reports.service;

import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.report.IntelliReportResponse;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReportResponse;

/**
 * This AdvanceReportService interface is to create Abstract methods for AdvanceReportService Implementation Service
 */
@Component
public interface AdvanceReportService {
	
	VisualizationReportResponse getVisualizationReportV2(String vin, String startDate, String endDate) throws ProcessCustomError;
	VisualizationReportResponse getVisualizationReportV3(String vin, String startDate, String endDate) throws ProcessCustomError;
	IntelliReportResponse getIntelliReportV3(String vin, String startDate, String endDate) throws ProcessCustomError ;

}
