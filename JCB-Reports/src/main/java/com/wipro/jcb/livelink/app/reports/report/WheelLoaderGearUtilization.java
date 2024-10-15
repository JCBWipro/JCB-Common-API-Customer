package com.wipro.jcb.livelink.app.reports.report;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is to Handle Response Related to WheelLoaderGearUtilization
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelLoaderGearUtilization implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;
	
	private Map<String, Double> forward;
	private Map<String, Double> backward;

}
