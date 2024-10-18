package com.wipro.jcb.livelink.app.alerts.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
* Author: Rituraj Azad
* User: RI20474447
* Date:17-10-2024
*/

@Setter
@Getter
public class ServiceHistoryDetails {

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM yyyy", timezone = AppServerConstants.timezone)
    @Schema(description = "Date on ehich service is done", example = "2017-09-09", required = true)
    private Date serviceDate;
    /*
     * optional date in miliseconds in UTC Example : 0
     */
    @Schema(description = "Service job ID", example = "10005692", required = true)
    private String serviceJobId;
    /*
     * optional Example : "string" string
     */
    @Schema(description = "Name of service", example = "Beyond warranty", required = true)
    private String serviceName;

    public ServiceHistoryDetails(Date serviceDate, String serviceJobId, String serviceName) {
        super();
        this.serviceDate = serviceDate;
        this.serviceJobId = serviceJobId;
        this.serviceName = serviceName;
    }



}
