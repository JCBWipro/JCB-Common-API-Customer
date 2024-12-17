package com.wipro.jcb.livelink.app.reports.report;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * This Class is to Handle Response related to FuelConsumptionResponse
 */
@Data
public class FuelConsumptionResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "Day", example = "2017-07-13", required = true)
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	@JsonProperty("date")
	private Date date;

	@Schema(description = "Total Fuel Used", example = "0.0", required = true)
	@JsonProperty("totalFuelUsed")
	private Double totalFuelUsed;

	@Schema(description = "Fuel Average", example = "0.0", required = true)
	@JsonProperty("fuelAverage")
	private Double fuelAverage;
	
	public FuelConsumptionResponse(){}

	public FuelConsumptionResponse(Date date, Double totalFuelUsed, Double fuelAverage) {
		super();
		this.date = date;
		this.totalFuelUsed = totalFuelUsed;
		this.fuelAverage = fuelAverage;
	}

	public FuelConsumptionResponse(Date date, Double totalFuelUsed) {
		super();
		this.date = date;
		this.totalFuelUsed = totalFuelUsed;
	}
	
	
	
}
