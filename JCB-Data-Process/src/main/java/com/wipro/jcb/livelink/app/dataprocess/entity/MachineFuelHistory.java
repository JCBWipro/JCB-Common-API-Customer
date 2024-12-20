package com.wipro.jcb.livelink.app.dataprocess.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.dataprocess.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.dataprocess.commonUtils.FuelHistoryDataId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Entity
@DynamicUpdate
@IdClass(FuelHistoryDataId.class)
@Table(name = "machinefuelhistorydata", indexes = {
        @Index(name = "machinefuelhistorydata_vin_dateTime", columnList = "vin,dateTime")})
public class MachineFuelHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 6261448896395354611L;

    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;

    private Double fuelLevel;
    @Id
    @JsonIgnore
    private String vinId;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Generated Time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date dateTime;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getVinId() {
        return vinId;
    }

    public void setVinId(String vinId) {
        this.vinId = vinId;
    }

    @Override
    public String toString() {
        return "MachineFuelHistory [ vin=" + vin + ", fuelLevel=" + fuelLevel + ", vinId=" + vinId + ", dateTime="
                + dateTime + "]";
    }

}
