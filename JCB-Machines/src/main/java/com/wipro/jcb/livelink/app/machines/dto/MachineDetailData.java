package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MachineDetailData {
    @Schema(description = "Engine status", example = "true", allowableValues = "true,false", required = true)
    private boolean engineStatus;
    @Schema(description = "Engine value", example = "On", allowableValues = "On,Off", required = true)
    private String engineValue;
    @Schema(description = "Connectivity status", example = "true", allowableValues = "true,false", required = true)
    private boolean connectivity;
    @Schema(description = "Connectivity value", example = "On", allowableValues = "On,Off", required = true)
    private String connectivityValue;
    @Schema(description = "Coolant status", example = "false", allowableValues = "true,false", required = true)
    private boolean coolant;
    @Schema(description = "Coolant value", example = "-", allowableValues = "Fault,No Fault", required = true)
    private String coolantValue;
    @Schema(description = "Battery status", example = "true", allowableValues = "true,false", required = true)
    private boolean batteryConnectedStatus;
    @Schema(description = "Battery value", example = "1", required = true)
    private String batteryVoltage;
    @Schema(description = "Oil status", example = "true", allowableValues = "true,false", required = true)
    private boolean OilPressureStatus;
    @Schema(description = "Oil value", example = "Fault", allowableValues = "Fault,No Fault", required = true)
    private String oilValue;
    @Schema(description = "Air status", example = "false", allowableValues = "true,false", required = true)
    private boolean airFilterStatus;
    @Schema(description = "Air value", example = "-", required = true)
    private String airValue;
    @Schema(description = "Fuel status", example = "false", allowableValues = "true,false", required = true)
    private boolean fuelLevelStatus;
    @Schema(description = "Fuel value", example = "45.45", required = true)
    private String fuelValue;
    @Schema(description = "Status as on time", example = "2017-07-13 12:44:20", required = true)
    @JsonIgnore
    private Date statusAsOnTime;
    @Schema(description = "last communication time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date lastCommunicationTime;
    @JsonIgnore
    @Schema(description = "Machine image", example = "Image URL/Path", required = true)
    private String image;
    @JsonIgnore
    @Schema(description = "Machine thumbnail image", example = "Image URL/Path", required = true)
    private String thumbnail;

    @Override
    public String toString() {
        return "MachineDetailData [engineStatus=" + engineStatus + ", engineValue=" + engineValue + ", connectivity="
                + connectivity + ", connectivityValue=" + connectivityValue + ", coolant=" + coolant + ", coolantValue="
                + coolantValue + ", batteryConnectedStatus=" + batteryConnectedStatus + ", batteryVoltage="
                + batteryVoltage + ", OilPressureStatus=" + OilPressureStatus + ", oilValue=" + oilValue
                + ", airFilterStatus=" + airFilterStatus + ", airValue=" + airValue + ", fuelLevelStatus="
                + fuelLevelStatus + ", fuelValue=" + fuelValue + ", statusAsOnTime=" + statusAsOnTime
                + ", lastCommunicationTime=" + lastCommunicationTime + ", image=" + image + ", thumbnail=" + thumbnail
                + "]";
    }

    public boolean getEngineStatus() {
        return engineStatus;
    }
}
