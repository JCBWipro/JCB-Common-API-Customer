package com.wipro.jcb.livelink.app.reports.report;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to AdvanceReportChart
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceReportChart {
	
	@Schema(description = "chart", example = "DutyCycle", required = true)
	@JsonProperty("chart")
	private String chart;

}
