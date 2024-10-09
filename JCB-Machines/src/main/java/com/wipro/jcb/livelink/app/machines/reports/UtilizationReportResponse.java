package com.wipro.jcb.livelink.app.machines.reports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This UtilizationReportResponse is to Handle Response of Utilization Report
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilizationReportResponse {

	@Schema(description = "Machine vin", required = true)
	private String vin;

	@Schema(description = "startDate - endDate", required = true)
	private String dateRange;

	@Schema(description = "Machine Utilization Report Data", required = true)
	private UtilizationReport utilizationReport;

}
