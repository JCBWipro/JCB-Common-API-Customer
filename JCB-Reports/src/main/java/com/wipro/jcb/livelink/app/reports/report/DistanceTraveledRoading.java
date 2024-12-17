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
 * This Class is to Handle Response related to DistanceTraveledRoading
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceTraveledRoading {
	
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date day;
	
	@Schema(description = "Distance Traveled Roading", example = "1.1", required = true)
	@JsonProperty("distanceTraveledRoading")
	private Double distanceTraveledRoading;

}
