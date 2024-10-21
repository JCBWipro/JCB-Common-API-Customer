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
 * This Class is to Handle Response related to MachinePowerBands
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachinePowerBands implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;
	
	@Schema(description = "Low PowerBand", example = "0.0", required = true)
	@JsonProperty("lowPowerBand")
	private Double lowPowerBand;
	
	@Schema(description = "Medium PowerBand", example = "0.0", required = true)
	@JsonProperty("mediumPowerBand")
	private Double mediumPowerBand;
	
	@Schema(description = "High Power Band", example = "0.0", required = true)
	@JsonProperty("highPowerBand")
	private Double highPowerBand;
	
	@Schema(description = "Idle Power Band", example = "0.0", required = true)
	@JsonProperty("idlePowerBand")
	private Double idlePowerBand;

}
