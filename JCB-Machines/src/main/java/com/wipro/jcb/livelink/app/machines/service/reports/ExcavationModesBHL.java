package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "ExcavationModes [day=" + day + ", economyModeHrs=" + economyModeHrs + ", powerModeHrs=" + powerModeHrs
                + ", activeModeHrs=" + activeModeHrs + "]";
    }

}

