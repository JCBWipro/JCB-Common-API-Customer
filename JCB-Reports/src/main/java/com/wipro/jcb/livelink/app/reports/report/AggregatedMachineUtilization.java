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
 * This Class is to Handle Response related to AggregatedMachineUtilization
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedMachineUtilization {

	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date day;

	@Schema(description = "Idle hours", example = "20.0", required = true)
	@JsonProperty("idle")
	private Double idleHours;

	@Schema(description = "Off hours", example = "10.0", required = true)
	@JsonProperty("off")
	private Double offHours;
	@Schema(description = "Working hours", example = "120.0", required = true)
	@JsonProperty("working")
	private Double workingHours;

}
