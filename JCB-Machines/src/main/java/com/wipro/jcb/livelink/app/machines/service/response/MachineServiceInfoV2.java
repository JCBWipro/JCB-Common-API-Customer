package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MachineServiceInfoV2 {

    @ApiModelProperty(value = "Total current machine hours", example = "280.0", required = true)
    private Double hours = 0.0;
    @ApiModelProperty(value = "Service overdue by hrs", example = "280.0", required = true)
    private Double overdue = 0.0;
    @ApiModelProperty(value = "Service overdue at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date overdueAt;
    @ApiModelProperty(value = "Service due on hrs", example = "280.0", required = true)
    private Double due = 0.0;
    @ApiModelProperty(value = "Service due at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date dueAt;
    @ApiModelProperty(value = "Service done this hrs", example = "280.0", required = true)
    private Double done = 0.0;
    @ApiModelProperty(value = "Service done at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date doneAt;
    @ApiModelProperty(value = "Machine Image", example = "Image URL/Path", required = true)
    private String thumbnail = "";
    @ApiModelProperty(value = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private String vin;
    @ApiModelProperty(value = "Model", example = "3DX Super ecoXcellence", required = true)
    private String model;
    @ApiModelProperty(value = "platform", example = "Backhoe Loader", required = true)
    private String platform;
    @ApiModelProperty(value = "Reg. No/Location/Nickname", example = "-", required = true)
    private String tag;
    @ApiModelProperty(value = "Machine image", example = "Image URL/Path", required = true)
    private String image;
    @ApiModelProperty(value = "Service history of machine for confidured years", example = "list of hist", required = true)
    private List<ServiceHistoryTimeline> history;

}
