package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Entity
@DynamicUpdate
@Table(name = "machine_feedparser_location_data", indexes = {
        @Index(name = "location_data_indexes_vin", columnList = "vin,lastModifiedDate", unique = false),
        @Index(name = "location_data_indexes_statusAsOnTime", columnList = "statusAsOnTime", unique = false)})
public class MachineFeedLocation implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -407814975667341910L;

    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;

    private Double latitude = 0.0;

    /*@CreatedBy
    protected String createdBy;*/
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;
    /*@LastModifiedBy
    protected String lastModifiedBy;*/
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private MachineAddress machineAddress;
    /*
     * Latitude part of machine GPS co-ordinates Example : 0.0
     */
    private Double longitude = 0.0;

    public MachineFeedLocation(String vin, Date statusAsOnTime, Double latitude, Date creationDate,
                               Date lastModifiedDate, MachineAddress machineAddress, Double longitude) {
        super();
        this.vin = vin;
        this.statusAsOnTime = statusAsOnTime;
        this.latitude = latitude;
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.machineAddress = machineAddress;
        this.longitude = longitude;
    }


    public MachineFeedLocation(String vin, Date creationDate, MachineAddress machineAddress, Date statusAsOnTime) {
        super();
        this.vin = vin;
        this.creationDate = creationDate;
        this.machineAddress = machineAddress;
        this.statusAsOnTime = statusAsOnTime;
    }
    public MachineFeedLocation() {
        super();

    }
    public String getVin() {
        return vin;
    }
    public Date getStatusAsOnTime() {
        return statusAsOnTime;
    }
    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }

    public MachineAddress getMachineAddress() {
        return machineAddress;
    }


    public void setMachineAddress(MachineAddress machineAddress) {
        this.machineAddress = machineAddress;
    }


    @Override
    public String toString() {
        return "MachineFeedLocationInfo [vin=" + vin + ", statusAsOnTime=" + statusAsOnTime + ", latitude="
                + latitude + ", longitude=" + longitude + "]";
    }



}
