package com.wipro.jcb.livelink.app.machines.service.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class DistanceTraveledRoading {

    @ApiModelProperty(value = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @ApiModelProperty(value = "Distance Traveled Roading", example = "1.1", required = true)
    @JsonProperty("distanceTraveledRoading")
    private Double distanceTraveledRoading;

    public Date getDay() {
        return day;
    }

    public Double getDistanceTraveledRoading() {
        return distanceTraveledRoading;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setDistanceTraveledRoading(Double distanceTraveledRoading) {
        this.distanceTraveledRoading = distanceTraveledRoading;
    }

    public DistanceTraveledRoading() {
        super();
    }

    public DistanceTraveledRoading(Date day, Double distanceTraveledRoading) {
        super();
        this.day = day;
        this.distanceTraveledRoading = distanceTraveledRoading;
    }

    @Override
    public String toString() {
        return "DistanceTraveledRoading [day=" + day + ", distanceTraveledRoading=" + distanceTraveledRoading + "]";
    }




}
