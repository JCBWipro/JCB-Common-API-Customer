package com.wipro.jcb.livelink.app.reports.report;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to GearUtilizationWLS
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GearUtilizationWLS {
	
	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date day;
	
	@Schema(description = "gear 1 forward utilization", example="0.0", required=true)
	private Double gear1FwdUtilization;
	
	@Schema(description = "gear 1 backward utilization", example="0.0", required=true)
	private Double gear1BkwdUtilization;
	
	@Schema(description = "gear 2 forward utilization", example="0.0", required=true)
	private Double gear2FwdUtilization;
	
	@Schema(description = "gear 2 backward utilization", example="0.0", required=true)
	private Double gear2BkwdUtilization;
	
	@Schema(description = "gear 3 forward utilization", example="0.0", required=true)
	private Double gear3FwdUtilization;
	
	@Schema(description = "gear 3 backward utilization", example="0.0", required=true)
	private Double gear3BkwdUtilization;
	
	@Schema(description = "gear 4 forward utilization", example="0.0", required=true)
	private Double gear4FwdUtilization;
	
	@Schema(description = "gear 4 backward utilization", example="0.0", required=true)
	private Double gear4BkwdUtilization;
	
	
	@Schema(description = "total fuel in liters", example="0.0", required=true)
	private Double totalFuelUsedInLtrs;
	
	@Schema(description = "average fuel consumption", example="0.0", required=true)
	private Double averageFuelConsumption;
	
	
	@Schema(description = "fuel Used In LPB liters", example="0.0", required=true)
	private Double fuelUsedInLPBLtrs;
	
	@Schema(description = "fuel Used In MPB liters", example="0.0", required=true)
	private Double fuelUsedInMPBLtrs;
	
	@Schema(description = "fuel Used In HPB liters", example="0.0", required=true)
	private Double fuelUsedInHPBLtrs;
	
	@Schema(description = "fuel loss", example="0.0", required=true)
	private Double fuelLoss;

}
