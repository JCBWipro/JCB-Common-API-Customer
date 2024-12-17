package com.wipro.jcb.livelink.app.reports.report;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to FuelConsumptionExcavation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelConsumptionExcavation {
	
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date day;
	
	@Schema(description = "Economy Mode", example = "1.1", required = true)
	@JsonProperty("economyMode")
	private Double economyMode;
	
	@Schema(description = "Economy Mode Percentage", example = "1.1", required = true)
	@JsonProperty("economyModePercentage")
	private Double economyModePercentage;
	
	@Schema(description = "Standard Mode", example = "1.1", required = true)
	@JsonProperty("standardMode")
	private Double standardMode;
	
	@Schema(description = "Standard Mode Percentage", example = "1.1", required = true)
	@JsonProperty("standardModePercentage")
	private Double standardModePercentage;
	
	@Schema(description = "Plus Mode", example = "1.1", required = true)
	@JsonProperty("plusMode")
	private Double plusMode;
	
	@Schema(description = "Standard Mode Percentage", example = "1.1", required = true)
	@JsonProperty("plusModePercentage")
	private Double plusModePercentage;

}
