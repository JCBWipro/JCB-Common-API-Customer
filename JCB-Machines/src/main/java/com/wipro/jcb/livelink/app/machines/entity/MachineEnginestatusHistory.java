package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.EngineStatusHistoryDataId;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Entity
@DynamicUpdate
@IdClass(EngineStatusHistoryDataId.class)
@Table(name = "machineenginestatushistorydata", indexes = {
        @Index(name = "machineenginestatushistorydata_vin_dateTime", columnList = "vin,dateTime", unique = false) })
public class MachineEnginestatusHistory implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2699233137907509189L;

    @Id
    @JsonIgnore
    private String vinId;

    @Column(name = "vin", updatable = false, insertable = true)
    private String vin;

    private Boolean isEngineOn;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "Generated Time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date dateTime;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public MachineEnginestatusHistory(){
        super();
    }
    public MachineEnginestatusHistory(String vin, Date dateTime){
        super();
        this.vin= vin ;
        this.dateTime=dateTime;
    }
    public MachineEnginestatusHistory(String vin,Boolean isEngineOn, Date dateTime){
        super();
        this.vin= vin ;
        this.dateTime=dateTime;
        this.isEngineOn= isEngineOn;
        this.vinId=vin;
    }
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Boolean getIsEngineOn() {
        return isEngineOn;
    }

    public void setIsEngineOn(Boolean isEngineOn) {
        this.isEngineOn = isEngineOn;
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
        return "MachineEnginestatusHistory [ vinId=" + vinId + ", vin=" + vin + ", isEngineOn="
                + isEngineOn + ", dateTime=" + dateTime + "]";
    }


}