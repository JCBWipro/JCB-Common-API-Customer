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
 * This Class is to Handle Response related to CompactionVibrationOnOffData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompactionVibrationOnOffData implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;

	@Schema(description = "vibration on hrs", example = "0.0", required = true)
	@JsonProperty("vibration_on_hrs")
	private Double vibration_on_hrs;

	@Schema(description = "vibration off hrs", example = "0.0", required = true)
	@JsonProperty("vibration_off_hrs")
	private Double vibration_off_hrs;

}
