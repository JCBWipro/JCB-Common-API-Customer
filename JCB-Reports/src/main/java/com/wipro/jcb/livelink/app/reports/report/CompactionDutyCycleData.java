package com.wipro.jcb.livelink.app.reports.report;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CompactionDutyCycleData
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompactionDutyCycleData implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "high Vibration", example = "0.0", required = true)
	@JsonProperty("highVibration")
	private Double highVibration = 0.0;

	@Schema(description = "low Vibration", example = "0.0", required = true)
	@JsonProperty("lowVibration")
	private Double lowVibration = 0.0;

	@Schema(description = "static Pass", example = "0.0", required = true)
	@JsonProperty("staticPass")
	private Double staticPass = 0.0;

}
