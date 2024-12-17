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
 * This class is to Handle Response Related to WeekelyDutyCycle
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekelyDutyCycle implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;

	@Schema(description = "cumulative loaded weight", example = "0.0", required = true)
	@JsonProperty("cumulativeLoadedWeight")
	private Double cumulativeLoadedWeight;

}
