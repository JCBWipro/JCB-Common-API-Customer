package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class FuelConsumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date date;

    @ApiModelProperty(value = "Total Fuel Used", example = "0.0", required = true)
    @JsonProperty("totalFuelUsed")
    private Double totalFuelUsed;

    @ApiModelProperty(value = "Fuel Average", example = "0.0", required = true)
    @JsonProperty("fuelAverage")
    private Double fuelAverage;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalFuelUsed() {
        return totalFuelUsed;
    }

    public void setTotalFuelUsed(Double totalFuelUsed) {
        this.totalFuelUsed = totalFuelUsed;
    }



    public Double getFuelAverage() {
        return fuelAverage;
    }

    public void setFuelAverage(Double fuelAverage) {
        this.fuelAverage = fuelAverage;
    }

    public FuelConsumption(Date date, Double totalFuelUsed) {
        super();
        this.date = date;
        this.totalFuelUsed = totalFuelUsed;
    }



    public FuelConsumption(Date date, Double totalFuelUsed, Double fuelAverage) {
        super();
        this.date = date;
        this.totalFuelUsed = totalFuelUsed;
        this.fuelAverage = fuelAverage;
    }

    public FuelConsumption() {
        super();
    }

    @Override
    public String toString() {
        return "FuelConsumption [date=" + date + ", totalFuelUsed=" + totalFuelUsed + "]";
    }

}

