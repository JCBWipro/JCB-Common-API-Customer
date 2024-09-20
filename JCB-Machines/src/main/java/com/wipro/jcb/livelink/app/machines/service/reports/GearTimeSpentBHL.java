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
public class GearTimeSpentBHL {
    @ApiModelProperty(value = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @ApiModelProperty(value = "firstGear", example = "1.1", required = true)
    @JsonProperty("firstGear")
    private Double firstGear;

    @ApiModelProperty(value = "secoundGear", example = "1.1", required = true)
    @JsonProperty("secoundGear")
    private Double secoundGear;

    @ApiModelProperty(value = "thirdGear", example = "1.1", required = true)
    @JsonProperty("thirdGear")
    private Double thirdGear;

    @ApiModelProperty(value = "forthGear", example = "1.1", required = true)
    @JsonProperty("forthGear")
    private Double forthGear;


    public GearTimeSpentBHL() {
        super();
    }


    public GearTimeSpentBHL(Date day, Double firstGear, Double secoundGear, Double thirdGear, Double forthGear) {
        super();
        this.day = day;
        this.firstGear = firstGear;
        this.secoundGear = secoundGear;
        this.thirdGear = thirdGear;
        this.forthGear = forthGear;
    }


    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Double getFirstGear() {
        return firstGear;
    }

    public void setFirstGear(Double firstGear) {
        this.firstGear = firstGear;
    }

    public Double getSecoundGear() {
        return secoundGear;
    }

    public void setSecoundGear(Double secoundGear) {
        this.secoundGear = secoundGear;
    }

    public Double getThirdGear() {
        return thirdGear;
    }

    public void setThirdGear(Double thirdGear) {
        this.thirdGear = thirdGear;
    }

    public Double getForthGear() {
        return forthGear;
    }

    public void setForthGear(Double forthGear) {
        this.forthGear = forthGear;
    }

    @Override
    public String toString() {
        return "GearTimeSpentBHL [day=" + day + ", firstGear=" + firstGear + ", secoundGear=" + secoundGear
                + ", thirdGear=" + thirdGear + ", forthGear=" + forthGear + "]";
    }



}
