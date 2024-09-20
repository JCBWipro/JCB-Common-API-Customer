package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.constants.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertData {
    @ApiModelProperty(value = "Generated time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date eventGeneratedTime;
    @ApiModelProperty(value = "Nature of fault", example = "YELLOW", allowableValues = "YELLOW,RED", required = true)
    private EventLevel eventLevel;
    @ApiModelProperty(value = "Name", example = "Fuel Level is low", required = true)
    private String name;
    @ApiModelProperty(value = "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    private EventType eventType;
    @ApiModelProperty(value = "Unique identifier for alert", example = "12344556", required = true)
    private String id;
    @ApiModelProperty(value = "Alert is active or historical", example = "false", allowableValues = "true,false", required = true)
    private Boolean isActive;
    @ApiModelProperty(value = "is Dtc Alert", example = "false", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;

    public AlertData() {
    }

    public AlertData(Date eventGeneratedTime, EventLevel eventLevel, String name, EventType eventType, String id,
                     Boolean isActive, Boolean isDtcAlert) {
        super();
        this.eventGeneratedTime = eventGeneratedTime;
        this.eventLevel = eventLevel;
        this.name = name;
        this.eventType = eventType;
        this.id = id;
        this.isActive = isActive;
        this.isDtcAlert = isDtcAlert;
    }

}
