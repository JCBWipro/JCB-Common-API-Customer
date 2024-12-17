package com.wipro.jcb.livelink.app.alerts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.alerts.constants.EventLevel;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/23/2024
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAlert {
    @Schema(description = "Unique identifier for Alert", example = "12", required = true)
    private String id;
    @Schema(description = "Unique identifier for machine", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @Schema(description = "Date on which Alert is generated", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date eventGeneratedTime;
    @Schema(description = "name", example = "Service due", required = true)
    private String name;
    @Schema(description ="EventLevel", example = "YELLOW,RED", required = true)
    private EventLevel level;
    @Schema(description = "description about alert", example = "The machine is approaching service", required = true)
    private String description;
    @Schema(description = "location of machine", example = "Pune", required = true)
    private String location;
    @Schema(description = "Machine Image", example = "Image URL/Path", required = true)
    private String thumbnail;
    @Schema(description = "Name of model of machine", example = "2DX XXX", required = true)
    private String model;
    @Schema(description = "Specify whether alert is read or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean readFlag;
    private AlertInfoResponse alertInfoResponse;
    @Schema(description = "Nickname of machine", example = "-", required = true)
    private String tag;
    @Schema(description = "machine platform name", example = "Exa***", required = true)
    private String platform;
    @Schema(description = "true or falsr depend on alert in active or historical", example = "true", allowableValues = "true,false", required = true)
    private Boolean isActive;
    @Schema(description ="is dtc alert", example = "false", allowableValues="true,false", required=true)
    private Boolean isDtcAlert;
}
