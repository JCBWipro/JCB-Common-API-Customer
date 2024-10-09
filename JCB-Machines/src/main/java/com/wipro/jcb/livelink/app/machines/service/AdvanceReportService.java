package com.wipro.jcb.livelink.app.machines.service;

import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.reports.UtilizationReportResponse;

/**
 * This AdvanceReportService interface is to create Abstract methods for AdvanceReportService Implementation Service
 */
@Component
public interface AdvanceReportService {

	UtilizationReportResponse getMachineUtilization(String vin, String startDate, String endDate) throws ProcessCustomError;

}
