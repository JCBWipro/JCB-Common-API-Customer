package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MachineServiceInfo {
    @Schema(description = "Total current machine hours", example = "280.0", required = true)
    private Double hours = 0.0;
    @Schema(description = "Service overdue by hrs", example = "280.0", required = true)
    private Double overdue = 0.0;
    @Schema(description = "Service overdue at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date overdueAt;
    @Schema(description = "Service due on hrs", example = "280.0", required = true)
    private Double due = 0.0;
    @Schema(description = "Service due at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date dueAt;
    @Schema(description = "Service done this hrs", example = "280.0", required = true)
    private Double done = 0.0;
    @Schema(description = "Service done at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date doneAt;
    @Schema(description = "Machine Image", example = "Image URL/Path", required = true)
    private String thumbnail = "";
    @Schema(description = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @Schema(description = "Model", example = "3DX Super ecoXcellence", required = true)
    private String model;
    @Schema(description = "platform", example = "Backhoe Loader", required = true)
    private String platform;
    @Schema(description = "Reg. No/Location/Nickname", example = "-", required = true)
    private String tag;
    @Schema(description = "Machine image", example = "Image URL/Path", required = true)
    private String image;
    @Schema(description = "Service history of machine for confidured years", example = "list of hist", required = true)
    private List<ServiceHistoryTimeline> history;

}

