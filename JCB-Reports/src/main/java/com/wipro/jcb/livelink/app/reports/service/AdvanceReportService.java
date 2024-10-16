package com.wipro.jcb.livelink.app.reports.service;

import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReportResponse;

/**
 * This AdvanceReportService interface is to create Abstract methods for AdvanceReportService Implementation Service
 */
@Component
public interface AdvanceReportService {
	
	public VisualizationReportResponse getVisualizationReportV2(String vin, String startDate, String endDate) throws ProcessCustomError;

}
