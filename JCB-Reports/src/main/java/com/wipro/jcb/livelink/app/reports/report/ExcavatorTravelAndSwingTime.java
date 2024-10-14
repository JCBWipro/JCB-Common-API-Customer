package com.wipro.jcb.livelink.app.reports.report;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to ExcavatorTravelAndSwingTime
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcavatorTravelAndSwingTime implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;
	
	@Schema(description = "total hours", example = "0.0", required = true)
	@JsonProperty("totalHrs")
	private Double totalHrs;
	
	@Schema(description = "travel hours", example = "0.0", required = true)
	@JsonProperty("travelHrs")
	private Double travelHrs;
	
	@Schema(description = "swing hours", example = "0.0", required = true)
	@JsonProperty("swingHrs")
	private Double swingHrs;

}
