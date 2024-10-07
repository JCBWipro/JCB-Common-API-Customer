package com.wipro.jcb.livelink.app.machines.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This MachineLocationDetail is to Handle Response related to Machine Location
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineLocationDetail {

	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonProperty("date")
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	private Date date;
	private String startLocation;
	private String endLocation;
	private String startTime;
	private String endTime;
	private Double startLatitude;
	private Double startLongitude;
	private Double endLatitude;
	private Double endLongitude;

}
