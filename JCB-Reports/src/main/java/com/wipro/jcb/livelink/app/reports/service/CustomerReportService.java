package com.wipro.jcb.livelink.app.reports.service;

import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.report.ReportResponseV2;

public interface CustomerReportService {
	
	ReportResponseV2 getCustomerReportV2(String userName, String filter, String startDate, String endDate) throws ProcessCustomError;

}
