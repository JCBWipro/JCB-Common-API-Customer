package com.wipro.jcb.livelink.app.reports.service;

import java.util.Date;

import com.wipro.jcb.livelink.app.reports.report.PdfReportData;

public interface LoadDataOnDemandRequest {
	
	public PdfReportData getPdfReportData(String vin, Date startdate, Date enddate);

	public PdfReportData getPdfReport(String vin,Date startdate, Date enddate);

}
