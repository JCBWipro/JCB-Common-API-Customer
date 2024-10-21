package com.wipro.jcb.livelink.app.reports.service;

import java.util.Date;

import com.wipro.jcb.livelink.app.reports.report.IntelliReport;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReport;

public interface MachineService {
	
	VisualizationReport getReportInstanceV2(String vin, Date startDate, Date endDate);
	
	IntelliReport getIntelliReportV2(String vin, Date startDate, Date endDate);
	
	IntelliReport loadIntelliDigReport(final String vin, Date startDate, Date endDate);
	
	VisualizationReport getReportInstanceV3(String vin, Date startDate, Date endDate);
	
	IntelliReport getIntelliReportV3(String vin, Date startDate, Date endDate);

}
