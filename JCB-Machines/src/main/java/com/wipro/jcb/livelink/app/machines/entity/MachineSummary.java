package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Entity
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "machine_summary", indexes = {
        @Index(name = "summaryindexes", columnList = "location,platform,model,tag", unique = false) })
public class MachineSummary implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -1359054509851104711L;

    private Double fuelLevel = 0.0;
    private Double latitude = 0.0;
    /*
     * Latitude part of machine GPS co-ordinates Example : 0.0
     */
    private Double longitude = 0.0;
    /*
     * Longitude part of machine GPS co-ordinates Example : 0.0
     */
    @Column(name = "location")
    private String location = "-";
    /*
     * GPS address in words Example : "string"
     */
    @Column(name = "model")
    private String model = "-";
    /*
     * Model of machine e.g 2DX, 3DX Example : "string"
     */
    @Column(name = "platform")
    private String platform = "-";
    /*
     * Type of platform. e.g BHL, Compactors Example : "string"
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;
    /*
     * Datetime when machine has last reported on to livelinkserver. Date in
     * miliseconds in UTCExample : 0
     */
    private Double totalMachineHours = 0.0;
    /*
     * Total Machine hours Example : 0.0
     */
    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    private String image = "";
    private String thumbnail = "";
    @Column(name = "tag")
    private String tag = "";
    private String site = "";
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public Double getFuelLevel() {
        return fuelLevel;
    }
    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
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
    public Date getStatusAsOnTime() {
        return statusAsOnTime;
    }
    public void setStatusAsOnTime(Date statusAsOnTime) {
        this.statusAsOnTime = statusAsOnTime;
    }
    public Double getTotalMachineHours() {
        return totalMachineHours;
    }
    public void setTotalMachineHours(Double totalMachineHours) {
        this.totalMachineHours = totalMachineHours;
    }
    public String getVin() {
        return vin;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }

    public Machine getMachine() {
        return machine;
    }
    public void setMachine(Machine machine) {
        this.machine = machine;
    }


    @Override
    public String toString() {
        return "MachineSummary [fuelLevel=" + fuelLevel + ", latitude=" + latitude + ", longitude=" + longitude
                + ", location=" + location + ", model=" + model + ", platform=" + platform + ", statusAsOnTime="
                + statusAsOnTime + ", totalMachineHours=" + totalMachineHours + ", vin=" + vin + ", image=" + image
                + ", thumbnail=" + thumbnail + ", tag=" + tag + ", site=" + site + ", machine=" + machine
                + "]";
    }
    public MachineSummary(Double fuelLevel, Double latitude, Double longitude, String location, String model,
                          String platform, Date statusAsOnTime, Double totalMachineHours, String vin, String image, String thumbnail,
                          String tag, String site) {
        super();
        this.fuelLevel = fuelLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.model = model;
        this.platform = platform;
        this.statusAsOnTime = statusAsOnTime;
        this.totalMachineHours = totalMachineHours;
        this.vin = vin;
        this.image = image;
        this.thumbnail = thumbnail;
        this.tag = tag;
        this.site = site;
    }



}
