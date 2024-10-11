package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.MachineExcavatorDataId;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(MachineExcavatorDataId.class)
@Table(name = "machine_excavator_data", indexes = {
		@Index(name = "machine_excavator_data_vin_day", columnList = "vin,day", unique = true) })
public class MachineExcavatorData implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	private String vinId;
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@Temporal(TemporalType.DATE)
	@JsonProperty("date")
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@Column(name = "day")
	private Date day;
	private String model;
	private String platform;
	
	@Schema(description = "travel Hours", example = "1.1", required = true)
	@JsonProperty("travel_hrs")
	private Double travelHrs;

	@Schema(description = "slew Hours", example = "2.6", required = true)
	@JsonProperty("slew_hrs")
	private Double slewHrs;

	@Schema(description = " Total Fuel Used In Ltrs", example = "105.0", required = true)
	@JsonProperty("total_fuel_used_in_ltrs")
	private Double totalFuelUsedInLtrs;

	@Schema(description = "Average Fuel Consumption", example = "15.877014528526761", required = true)
	@JsonProperty("average_fuel_consumption")
	private Double averageFuelConsumption;

	@Schema(description = "Fuel Used In LPBLtrs ", example = "15.877014528526761", required = true)
	@JsonProperty("fuel_used_in_LPB_ltrs")
	private Double fuelUsedInLPBLtrs;

	@Schema(description = "Fuel Used In MPB Ltrs", example = "15.877014528526761", required = true)
	@JsonProperty("fuel_used_in_MPB_ltrs")
	private Double fuelUsedInMPBLtrs;

	@Schema(description = "Fuel Used In HPB Ltrs", example = "15.877014528526761", required = true)
	@JsonProperty("fuel_used_in_HPB_ltrs")
	private Double fuelUsedInHPBLtrs;

	@Schema(description = "Fuel Loss", example = "15.877014528526761", required = true)
	@JsonProperty("fuel_loss")
	private Double fuelLoss;

	@Schema(description = "Total Hours", example = "0.0", required = true)
	@JsonProperty("total_hrs")
	private Double totalHrs;

	@Schema(description = "Period L Band Hours", example = "0.0", required = true)
	@JsonProperty("period_l_band_hrs")
	private Double periodLBandHrs;

	@Schema(description = "Period G Band Hours", example = "0.0", required = true)
	@JsonProperty("period_g_band_hrs")
	private Double periodGBandHrs;

	@Schema(description = "Period H Band Hours", example = "0.0", required = true)
	@JsonProperty("period_h_band_hrs")
	private Double periodHBandHrs;

	@Schema(description = "Period H Plus Band Hours", example = "0.0", required = true)
	@JsonProperty("period_h_plus_band_hrs")
	private Double periodHPlusBandHrs;

	@Schema(description = "Hammer Used Time Hrs", example = "0.0", required = true)
	private Double hammerUsedTimeHrs;

	@Schema(description = "Hammer Abuse Count", example = "0.0", required = true)
	private Double hammerAbuseCount;

	@Schema(description = "Engine On Count", example = "0.0", required = true)
	private Double engineOnCount;

	@Schema(description = "Power Boost Time", example = "0.0", required = true)
	private Double powerBoostTime;

	@Schema(description = "Hot Engine ShutDown Count", example = "0.0", required = true)
	private Double hotEngineShutDownCount;

	@Schema(description = "Long Engine Idling Count", example = "0.0", required = true)
	private Double longEngineIdlingCount;

	@Schema(description = "Auto Idle EvtCount", example = "0.0", required = true)
	private Double autoIdleEvtCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
	protected Date creationAt;

	@OneToOne
	@JoinColumn(name = "vin", referencedColumnName = "vin")
	@JsonIgnore
	private Machine machine;

	@Column(name = "vin", updatable = false, insertable = false)
	private String vin;

}
