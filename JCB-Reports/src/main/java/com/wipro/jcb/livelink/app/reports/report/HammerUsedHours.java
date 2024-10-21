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
 * This Class is to Handle Response related to HammerUsedHours
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HammerUsedHours {
	
	@Schema(description = "Day", example = "2023-11-21", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;
	
	@Schema(description = "Hammer Used Time Hrs", example = "0.0", required = true)
	@JsonProperty("hammerUsedTimeHrs")
	private Double hammerUsedTimeHrs;

}
