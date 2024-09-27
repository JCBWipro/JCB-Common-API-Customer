package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class FuelConsumptionDuty {

    @Schema(description = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @Schema(description = "Economy Mode", example = "1.1", required = true)
    @JsonProperty("excavationMode")
    private Double excavationMode;

    @Schema(description = "Economy Mode Percentage", example = "1.1", required = true)
    @JsonProperty("excavationModePercentage")
    private Double excavationModePercentage;

    @Schema(description = "Loading Mode", example = "1.1", required = true)
    @JsonProperty("loadingMode")
    private Double loadingMode;

    @Schema(description = "Loading Mode Percentage", example = "1.1", required = true)
    @JsonProperty("loadingModePercentage")
    private Double loadingModePercentage;

    @Schema(description = "Roading Mode ", example = "1.1", required = true)
    @JsonProperty("roadindMode")
    private Double roadindMode;

    @Schema(description ="Roading Mode Percentage", example = "1.1", required = true)
    @JsonProperty("roadindModePercentage")
    private Double roadindModePercentage;

    @Schema(description = "Idle Mode", example = "1.1", required = true)
    @JsonProperty("idleMode")
    private Double idleMode;

    @Schema(description = "Idle Mode Percentage", example = "1.1", required = true)
    @JsonProperty("idleModePercentage")
    private Double idleModePercentage;

    @Override
    public String toString() {
        return "FuelConsumptionDuty [day=" + day + ", excavationMode=" + excavationMode + ", excavationModePercentage="
                + excavationModePercentage + ", loadingMode=" + loadingMode + ", loadingModePercentage="
                + loadingModePercentage + ", roadindMode=" + roadindMode + ", roadindModePercentage="
                + roadindModePercentage + ", idleMode=" + idleMode + ", idleModePercentage=" + idleModePercentage + "]";
    }
}
