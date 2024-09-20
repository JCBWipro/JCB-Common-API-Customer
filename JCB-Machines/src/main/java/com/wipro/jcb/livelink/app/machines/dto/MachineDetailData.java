package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "Engine status", example = "true", allowableValues = "true,false", required = true)
    private boolean engineStatus;
    @ApiModelProperty(value = "Engine value", example = "On", allowableValues = "On,Off", required = true)
    private String engineValue;
    @ApiModelProperty(value = "Connectivity status", example = "true", allowableValues = "true,false", required = true)
    private boolean connectivity;
    @ApiModelProperty(value = "Connectivity value", example = "On", allowableValues = "On,Off", required = true)
    private String connectivityValue;
    @ApiModelProperty(value = "Coolant status", example = "false", allowableValues = "true,false", required = true)
    private boolean coolant;
    @ApiModelProperty(value = "Coolant value", example = "-", allowableValues = "Fault,No Fault", required = true)
    private String coolantValue;
    @ApiModelProperty(value = "Battery status", example = "true", allowableValues = "true,false", required = true)
    private boolean batteryConnectedStatus;
    @ApiModelProperty(value = "Battery value", example = "1", required = true)
    private String batteryVoltage;
    @ApiModelProperty(value = "Oil status", example = "true", allowableValues = "true,false", required = true)
    private boolean OilPressureStatus;
    @ApiModelProperty(value = "Oil value", example = "Fault", allowableValues = "Fault,No Fault", required = true)
    private String oilValue;
    @ApiModelProperty(value = "Air status", example = "false", allowableValues = "true,false", required = true)
    private boolean airFilterStatus;
    @ApiModelProperty(value = "Air value", example = "-", required = true)
    private String airValue;
    @ApiModelProperty(value = "Fuel status", example = "false", allowableValues = "true,false", required = true)
    private boolean fuelLevelStatus;
    @ApiModelProperty(value = "Fuel value", example = "45.45", required = true)
    private String fuelValue;
    @ApiModelProperty(value = "Status as on time", example = "2017-07-13 12:44:20", required = true)
    @JsonIgnore
    private Date statusAsOnTime;
    @ApiModelProperty(value = "last communication time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date lastCommunicationTime;
    @JsonIgnore
    @ApiModelProperty(value = "Machine image", example = "Image URL/Path", required = true)
    private String image;
    @JsonIgnore
    @ApiModelProperty(value = "Machine thumbnail image", example = "Image URL/Path", required = true)
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
