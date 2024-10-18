package com.wipro.jcb.livelink.app.alerts.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.alerts.constants.EventLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-10-2024
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlertObject {
    @Schema(description = "Unique identifier of Alert", example = "280", required = true)
    public String id;
    @Schema(description = "Unique identifier of machine", example = "PUNJD22CV0000IIII", required = true)
    public String vin;
    @Schema(description = "Name of alert", example = "Fuel Level is low", required = true)
    public String name;
    @Schema(description = "Event level - priority of alert", example = "YELLOW", allowableValues = "YELLOW,RED", required = true)
    public EventLevel eventLevel;
    @Schema(description = "Date and time on which alert is generated", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    public Date eventGeneratedTime;
    @Schema(description = "Tag to store nickname/reqNo of machine", example = "heavyDuty")
    public String tag;
    @Schema(description = "Machine thumbnail Image", example = "Image URL/Path", required = true)
    private String thumbnail;
    @Schema(description = "Specify whether alert is read or not", example = "false", allowableValues = "true,false", required = true)
    private Boolean readFlag;
    @Schema(description = "mane of machine platform", example = "Excavators", required = true)
    private String platform;
    private AlertInfoResponse alertInfoResponse;
    @Schema(description = "Alert is active or historical", example = "false", allowableValues = "true,false", required = true)
    private Boolean isActive;
    @Schema(description = "isDtcAlert", example = "false", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;

}
