package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class FuelConsumptionExcavation {

    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "Economy Mode", example = "1.1", required = true)
    @JsonProperty("economyMode")
    private Double economyMode;

    @Schema(description = "Economy Mode Percentage", example = "1.1", required = true)
    @JsonProperty("economyModePercentage")
    private Double economyModePercentage;

    @Schema(description = "Standard Mode", example = "1.1", required = true)
    @JsonProperty("standardMode")
    private Double standardMode;

    @Schema(description = "Standard Mode Percentage", example = "1.1", required = true)
    @JsonProperty("standardModePercentage")
    private Double standardModePercentage;

    @Schema(description = "Plus Mode", example = "1.1", required = true)
    @JsonProperty("plusMode")
    private Double plusMode;

    @Schema(description = "Standard Mode Percentage", example = "1.1", required = true)
    @JsonProperty("plusModePercentage")
    private Double plusModePercentage;

    public Date getDay() {
        return day;
    }


    public Double getEconomyMode() {
        return economyMode;
    }


    public void setEconomyMode(Double economyMode) {
        this.economyMode = economyMode;
    }


    public Double getStandardMode() {
        return standardMode;
    }

    public Double getStandardModePercentage() {
        return standardModePercentage;
    }

    public Double getPlusMode() {
        return plusMode;
    }

    public Double getPlusModePercentage() {
        return plusModePercentage;
    }

    public void setDay(Date day) {
        this.day = day;
    }



    public Double getEconomyModePercentage() {
        return economyModePercentage;
    }


    public void setEconomyModePercentage(Double economyModePercentage) {
        this.economyModePercentage = economyModePercentage;
    }


    public void setStandardMode(Double standardMode) {
        this.standardMode = standardMode;
    }

    public void setStandardModePercentage(Double standardModePercentage) {
        this.standardModePercentage = standardModePercentage;
    }

    public void setPlusMode(Double plusMode) {
        this.plusMode = plusMode;
    }

    public void setPlusModePercentage(Double plusModePercentage) {
        this.plusModePercentage = plusModePercentage;
    }

    public FuelConsumptionExcavation() {
        super();
    }


    public FuelConsumptionExcavation(Date day, Double economyMode, Double economyModePercentage, Double standardMode,
                                     Double standardModePercentage, Double plusMode, Double plusModePercentage) {
        super();
        this.day = day;
        this.economyMode = economyMode;
        this.economyModePercentage = economyModePercentage;
        this.standardMode = standardMode;
        this.standardModePercentage = standardModePercentage;
        this.plusMode = plusMode;
        this.plusModePercentage = plusModePercentage;
    }


    @Override
    public String toString() {
        return "FuelConsumptionExcavation [day=" + day + ", economyMode=" + economyMode + ", economyModePercentage="
                + economyModePercentage + ", standardMode=" + standardMode + ", standardModePercentage="
                + standardModePercentage + ", plusMode=" + plusMode + ", plusModePercentage=" + plusModePercentage
                + "]";
    }





}
