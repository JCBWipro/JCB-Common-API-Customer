package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.MachineWheelLoaderDataId;

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
@Table(name = "machine_wls_data", indexes = {
		@Index(name = "machine_wls_data_vin_day", columnList = "vin,day", unique = true) })
@IdClass(MachineWheelLoaderDataId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineWheelLoaderData implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Schema(description = "day", example = "2019-10-29", required = true)
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("day")
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

	@Schema(description = "gear 1 forward utilization", example = "0.0", required = true)
	private Double gear1FwdUtilization;

	@Schema(description = "gear 1 backward utilization", example = "0.0", required = true)
	private Double gear1BkwdUtilization;

	@Schema(description = "gear 2 forward utilization", example = "0.0", required = true)
	private Double gear2FwdUtilization;

	@Schema(description = "gear 2 backward utilization", example = "0.0", required = true)
	private Double gear2BkwdUtilization;

	@Schema(description = "gear 3 forward utilization", example = "0.0", required = true)
	private Double gear3FwdUtilization;

	@Schema(description = "gear 3 backward utilization", example = "0.0", required = true)
	private Double gear3BkwdUtilization;

	@Schema(description = "gear 4 forward utilization", example = "0.0", required = true)
	private Double gear4FwdUtilization;

	@Schema(description = "gear 4 backward utilization", example = "0.0", required = true)
	private Double gear4BkwdUtilization;

	@Schema(description = "total fuel in liters", example = "0.0", required = true)
	private Double totalFuelUsedInLtrs;

	@Schema(description = "average fuel consumption", example = "0.0", required = true)
	private Double averageFuelConsumption;

	@Schema(description = "fuel Used In LPB liters", example = "0.0", required = true)
	private Double fuelUsedInLPBLtrs;

	@Schema(description = "fuel Used In MPB liters", example = "0.0", required = true)
	private Double fuelUsedInMPBLtrs;

	@Schema(description = "fuel Used In HPB liters", example = "0.0", required = true)
	private Double fuelUsedInHPBLtrs;

	@Schema(description = "fuel loss", example = "0.0", required = true)
	private Double fuelLoss;

	@Schema(description = "load bucket wait", example = "0.0", required = true)
	private Double loadBucketWeight;

	@Schema(description = "cumulative loaded weight", example = "0.0", required = true)
	private Double cumulativeLoadedWeight;

	@Schema(description = "number of buckets", example = "0.0", required = true)
	private Double numberOfBuckets;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
	private Date createdAt;

}
