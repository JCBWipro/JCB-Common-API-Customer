package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
public class ExcavationModesBHL {
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "economyModeHrs", example = "1.1", required = true)
    @JsonProperty("economy_mode_hrs")
    private Double economyModeHrs;

    @Schema(description = "powerModeHrs", example = "1.1", required = true)
    @JsonProperty("power_mode_hrs")
    private Double powerModeHrs;

    @Schema(description = "activeModeHrs", example = "1.1", required = true)
    @JsonProperty("active_mode_hrs")
    private Double activeModeHrs;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Double getEconomyModeHrs() {
        return economyModeHrs;
    }

    public void setEconomyModeHrs(Double economyModeHrs) {
        this.economyModeHrs = economyModeHrs;
    }

    public Double getPowerModeHrs() {
        return powerModeHrs;
    }

    public void setPowerModeHrs(Double powerModeHrs) {
        this.powerModeHrs = powerModeHrs;
    }

    public Double getActiveModeHrs() {
        return activeModeHrs;
    }

    public void setActiveModeHrs(Double activeModeHrs) {
        this.activeModeHrs = activeModeHrs;
    }

    public ExcavationModesBHL(Date day, Double economyModeHrs, Double powerModeHrs, Double activeModeHrs) {
        super();
        this.day = day;
        this.economyModeHrs = economyModeHrs;
        this.powerModeHrs = powerModeHrs;
        this.activeModeHrs = activeModeHrs;
    }

    public ExcavationModesBHL() {
        super();
    }

    @Override
    public String toString() {
        return "ExcavationModes [day=" + day + ", economyModeHrs=" + economyModeHrs + ", powerModeHrs=" + powerModeHrs
                + ", activeModeHrs=" + activeModeHrs + "]";
    }

}

