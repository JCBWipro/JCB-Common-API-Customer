package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
public class ServiceHistoryTimeline {
    @ApiModelProperty(value = "Service done at time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date serviceDate;
    /*
     * optional date in miliseconds in UTC Example : 0
     */
    @ApiModelProperty(value = "Service job ID", example = "10005692", required = true)
    private String serviceJobId;
    /*
     * optional Example : "string" string
     */
    @ApiModelProperty(value = "Name of service", example = "Beyond warranty", required = true)
    private String serviceName;

    public ServiceHistoryTimeline(Date serviceDate, String serviceJobId, String serviceName) {
        super();
        this.serviceDate = serviceDate;
        this.serviceJobId = serviceJobId;
        this.serviceName = serviceName;
    }

    public ServiceHistoryTimeline() {
        super();
    }

}

