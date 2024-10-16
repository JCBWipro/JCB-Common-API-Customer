package com.wipro.jcb.livelink.app.reports.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to VisualizationReportResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisualizationReportResponse {
	
	@Schema(description = "Machine vin", required = true)
	private String vin;
	
	@Schema(description = "startDate - endDate", required = true)
	private String dateRange;
	
	@Schema(description = "Machine visualization report data", required = true)
	private VisualizationReport visualizationReport;

}
