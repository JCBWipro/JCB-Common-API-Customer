package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
public class MachineCompassBHL {
    @ApiModelProperty(value = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @ApiModelProperty(value = "forwardDirection", example = "1.1", required = true)
    @JsonProperty("forwardDirection")
    private Double forwardDirection;

    @ApiModelProperty(value = "reverseDirection", example = "1.1", required = true)
    @JsonProperty("reverseDirection")
    private Double reverseDirection;

    @ApiModelProperty(value = "neutralDirection", example = "1.1", required = true)
    @JsonProperty("neutralDirection")
    private Double neutralDirection;

    public MachineCompassBHL() {
        super();
    }

    public MachineCompassBHL(Date day, Double forwardDirection, Double reverseDirection, Double neutralDirection) {
        super();
        this.day = day;
        this.forwardDirection = forwardDirection;
        this.reverseDirection = reverseDirection;
        this.neutralDirection = neutralDirection;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Double getForwardDirection() {
        return forwardDirection;
    }

    public void setForwardDirection(Double forwardDirection) {
        this.forwardDirection = forwardDirection;
    }

    public Double getReverseDirection() {
        return reverseDirection;
    }

    public void setReverseDirection(Double reverseDirection) {
        this.reverseDirection = reverseDirection;
    }

    public Double getNeutralDirection() {
        return neutralDirection;
    }

    public void setNeutralDirection(Double neutralDirection) {
        this.neutralDirection = neutralDirection;
    }

    @Override
    public String toString() {
        return "MachineCompassBHL [day=" + day + ", forwardDirection=" + forwardDirection + ", reverseDirection="
                + reverseDirection + ", neutralDirection=" + neutralDirection + "]";
    }

}

