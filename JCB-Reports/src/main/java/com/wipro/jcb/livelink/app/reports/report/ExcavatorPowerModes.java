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
 * This class is to Handle Response related to ExcavatorPowerModes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcavatorPowerModes implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;
	
	@Schema(description = "l band", example = "0.0", required = true)
	@JsonProperty("lBand")
	private Double lBand;
	
	@Schema(description = "g band", example = "0.0", required = true)
	@JsonProperty("gBand")
	private Double gBand;
	
	@Schema(description = "h band", example = "0.0", required = true)
	@JsonProperty("hBand")
	private Double hBand;
	
	@Schema(description = "h plus band", example = "0.0", required = true)
	@JsonProperty("hPlusBand")
	private Double hPlusBand;

}
