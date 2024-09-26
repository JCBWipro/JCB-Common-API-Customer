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

    @Override
    public String toString() {
        return "FuelConsumptionExcavation [day=" + day + ", economyMode=" + economyMode + ", economyModePercentage="
                + economyModePercentage + ", standardMode=" + standardMode + ", standardModePercentage="
                + standardModePercentage + ", plusMode=" + plusMode + ", plusModePercentage=" + plusModePercentage
                + "]";
    }





}
