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
public class DutyCycleBHL {
    @ApiModelProperty(value = "Day", example = "2017-07-13", required = true)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    private Date day;

    @ApiModelProperty(value = "travel attachment", example = "1.1", required = true)
    @JsonProperty("attachment")
    private Double attachment;

    @ApiModelProperty(value = "idling", example = "1.1", required = true)
    @JsonProperty("idling")
    private Double idling;

    @ApiModelProperty(value = "excavation", example = "1.1", required = true)
    @JsonProperty("excavation")
    private Double excavation;

    @ApiModelProperty(value = "loading", example = "1.1", required = true)
    @JsonProperty("loading")
    private Double loading;

    @ApiModelProperty(value = "roading", example = "1.1", required = true)
    @JsonProperty("roading")
    private Double roading;

    public DutyCycleBHL(Date day, Double attachment, Double idling, Double excavation, Double loading, Double roading) {
        super();
        this.day = day;
        this.attachment = attachment;
        this.idling = idling;
        this.excavation = excavation;
        this.loading = loading;
        this.roading = roading;
    }

    public DutyCycleBHL() {
        super();

    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Double getAttachment() {
        return attachment;
    }

    public void setAttachment(Double attachment) {
        this.attachment = attachment;
    }

    public Double getIdling() {
        return idling;
    }

    public void setIdling(Double idling) {
        this.idling = idling;
    }

    public Double getExcavation() {
        return excavation;
    }

    public void setExcavation(Double excavation) {
        this.excavation = excavation;
    }

    public Double getLoading() {
        return loading;
    }

    public void setLoading(Double loading) {
        this.loading = loading;
    }

    public Double getRoading() {
        return roading;
    }

    public void setRoading(Double roading) {
        this.roading = roading;
    }

    @Override
    public String toString() {
        return "DutyCycleBHL [day=" + day + ", attachment=" + attachment + ", idling=" + idling + ", excavation="
                + excavation + ", loading=" + loading + ", roading=" + roading + "]";
    }
}
