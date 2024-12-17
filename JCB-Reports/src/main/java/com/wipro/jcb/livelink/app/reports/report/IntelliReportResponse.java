package com.wipro.jcb.livelink.app.reports.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to IntelliReportResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntelliReportResponse {
	
	@Schema(description = "Machine vin", required = true)
	private String vin;
	
	@Schema(description = "startDate - endDate", required = true)
	private String dateRange;
	
	@Schema(description = "Machine intelli report data", required = true)
	IntelliReport intelliReport;

}
