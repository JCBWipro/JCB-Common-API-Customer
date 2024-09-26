package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
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

    public MachineServiceInfo(Double hours, Double overdue, Date overdueAt, Double due, Date dueAt, Double done,
                              Date doneAt, String thumbnail, String vin, String model, String platform, String tag, String image,
                              List<ServiceHistoryTimeline> history) {
        super();
        this.hours = hours;
        this.overdue = overdue;
        this.overdueAt = overdueAt;
        this.due = due;
        this.dueAt = dueAt;
        this.done = done;
        this.doneAt = doneAt;
        this.thumbnail = thumbnail;
        this.vin = vin;
        this.model = model;
        this.platform = platform;
        this.tag = tag;
        this.image = image;
        this.history = history;
    }

    public List<ServiceHistoryTimeline> getHistory() {
        return history;
    }

    public void setHistory(List<ServiceHistoryTimeline> history) {
        this.history = history;
    }

    public MachineServiceInfo() {
        super();
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Double getOverdue() {
        return overdue;
    }

    public void setOverdue(Double overdue) {
        this.overdue = overdue;
    }

    public Date getOverdueAt() {
        return overdueAt;
    }

    public void setOverdueAt(Date overdueAt) {
        this.overdueAt = overdueAt;
    }

    public Double getDue() {
        return due;
    }

    public void setDue(Double due) {
        this.due = due;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public void setDueAt(Date dueAt) {
        this.dueAt = dueAt;
    }

    public Double getDone() {
        return done;
    }

    public void setDone(Double done) {
        this.done = done;
    }

    public Date getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Date doneAt) {
        this.doneAt = doneAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

