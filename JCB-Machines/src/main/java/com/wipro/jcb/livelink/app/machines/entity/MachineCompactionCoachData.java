package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.MachineCompactionCoachDataId;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(MachineCompactionCoachDataId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "machine_compactor_data", indexes = {
		@Index(name = "machine_compactor_data_vin_day", columnList = "vin,day", unique = true) })
public class MachineCompactionCoachData implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Schema(description = "Day", example = "2019-10-24", required = true)
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	@Column(name = "day")
	private Date day;

	@Id
	@JsonIgnore
	@Column(name = "vin_id")
	private String vinId;
	private String platform;
	private String model;

	@OneToOne
	@JoinColumn(name = "vin", referencedColumnName = "vin")
	@JsonIgnore
	private Machine machine;

	@Column(name = "vin", updatable = false, insertable = false)
	private String vin;

	@Schema(description = "High Vibration Hrs", example = "0.0", required = true)
	@JsonProperty("highvibrationhrs")
	private Double highVibrationHrs;

	@Schema(description = "Low Vibration Hrs", example = "0.0", required = true)
	@JsonProperty("lowvibrationhrs")
	private Double lowVibrationHrs;

	@Schema(description = "Static Pass Hrs", example = "0.0", required = true)
	@JsonProperty("staticpasshrs")
	private Double staticPassHrs;

	@Schema(description = "Vibration On Hrs", example = "0.0", required = true)
	@JsonProperty("vibrationonhrs")
	private Double vibrationOnHrs;

	@Schema(description = "Vibration Off Hrs", example = "0.0", required = true)
	@JsonProperty("vibrationoffhrs")
	private Double vibrationOffHrs;

	@Schema(description = "fuelUsedInLowPowerBandLtrs", example = "0.0", required = true)
	@JsonProperty("fuelUsedInLowPowerBandLtrs")
	private Double fuelUsedInLowPowerBandLtrs;

	@Schema(description = "fuelUsedInMediumPowerBandLtrs", example = "0.0", required = true)
	@JsonProperty("fuelUsedInMediumPowerBandLtrs")
	private Double fuelUsedInMediumPowerBandLtrs;

	@Schema(description = "fuelUsedInHighPowerBandLtrs", example = "0.0", required = true)
	@JsonProperty("fuelUsedInHighPowerBandLtrs")
	private Double fuelUsedInHighPowerBandLtrs;

	@Schema(description = "fuelUsedInIdlePowerBandLtrs", example = "0.0", required = true)
	@JsonProperty("fuelUsedInIdlePowerBandLtrs")
	private Double fuelUsedInIdlePowerBandLtrs;

	@Schema(description = "averageFuelConsumption", example = "0.0", required = true)
	@JsonProperty("averageFuelConsumption")
	private Double averageFuelConsumption;

	@Schema(description = "totalFuelUsedInLtrs", example = "0.0", required = true)
	@JsonProperty("totalFuelUsedInLtrs")
	private Double totalFuelUsedInLtrs;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
	private Date createdAt;

}
