package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "machine_feedparser_data", indexes = {
        @Index(name = "machine_feedparser_indexes",columnList = "vin,lastModifiedDate", unique = false)})
public class MachineFeedParserData implements Serializable {

    @Serial
    private static final long serialVersionUID = -2928089151474049618L;

    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date hmrPacketTime;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date fuelPacketTime;


    private Double fuelLevel ;

    private Double totalMachineHours = 0.0;

    private Double batteryVoltage= 0.0;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    private String engineStatus = "Off";


    public MachineFeedParserData() {
        super();
    }
    public MachineFeedParserData (Builder build) {
        this.vin = build.vin;
        this.statusAsOnTime = build.statusAsOnTime;
        this.fuelLevel = build.fuelLevel;
        this.totalMachineHours = build.totalMachineHours;
        this.batteryVoltage = build.batteryVoltage;
    }


    @Override
    public String toString() {
        return "MachineFeedParserData [vin=" + vin + ", statusAsOnTime=" + statusAsOnTime + ", fuelLevel=" + fuelLevel
                + ", totalMachineHours=" + totalMachineHours + ", batteryVoltage=" + batteryVoltage + ", creationDate="
                + creationDate + ", lastModifiedDate=" + lastModifiedDate + "]";
    }



    public static class Builder{

        private final String vin ;


        private Date statusAsOnTime;

        private final Double fuelLevel ;


        private Double totalMachineHours = 0.0;

        private Double batteryVoltage= 0.0;

        private String engineStatus = "Off";

        public Builder (JSONObject machineobj) {
            this.vin= machineobj.getString("vin");
            this.fuelLevel = machineobj.optDouble("fuelLevel", 0.0);
            this.totalMachineHours= machineobj.optDouble("totalMachineHours", 0.0);
            this.batteryVoltage = machineobj.optDouble("batteryVoltage", 0.0);
            this.engineStatus =machineobj.optString("engineStatus", "-");

        }
        public Builder statusAsOnTime(Date statusAsOnTime) {
            this.statusAsOnTime= statusAsOnTime;
            return this;

        }

        public MachineFeedParserData build() {
            return new MachineFeedParserData(this);
        }
    }

    public void setHmrPacketTime(Date hmrPacketTime) {
        this.hmrPacketTime = hmrPacketTime;
    }

    public void setFuelPacketTime(Date fuelPacketTime) {
        this.fuelPacketTime = fuelPacketTime;
    }
}
