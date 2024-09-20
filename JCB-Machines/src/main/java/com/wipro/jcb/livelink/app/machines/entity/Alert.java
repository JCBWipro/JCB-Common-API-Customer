package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.constants.EventType;
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
@Table(name = "Alert", uniqueConstraints = @UniqueConstraint(columnNames = { "eventGeneratedTime", "eventType",
        "vin" ,"eventName" }))
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert implements Serializable {
    @Serial
    private static final long serialVersionUID = -3118714757588194755L;
    @Id
    @Column(name = "alert_id", unique = true, nullable = false)
    private String id;
    /*
     * Unique Id for Alert Example : "string"
     */
    /*
     * @ManyToOne
     *
     * @JoinColumn(name="vin") private Machine machine;
     */
    @ApiModelProperty(value = "Description", example = "Fuel Level is low and machine is outside operational hours", required = true)
    private String eventDescription;
    /*
     * Example : "string"
     */
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "Generated Time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date eventGeneratedTime;
    /*
     * date in miliseconds in UTC Example : 0
     */
    @ApiModelProperty(value = "Latitude", example = "18.5204", required = true)
    private Double latitude;
    /*
     * Latitude part of machine GPS co-ordinates Example : 0.0
     */
    @ApiModelProperty(value = "Longitude", example = "73.8567", required = true)
    private Double longitude;
    /*
     * Longitude part of machine GPS co-ordinates Example : 0.0
     */
    @ApiModelProperty(value = "Location", example = "Pune, Maharashtra, India", required = true)
    private String location;
    /*
     * GPS address in words Example : "string"
     */
    @ApiModelProperty(value = "Nature of Fault", example = "YELLOW", allowableValues = "YELLOW,RED", required = true)
    @Enumerated(EnumType.STRING)
    private EventLevel eventLevel;
    /*
     * Event level/severity. Need constant data for this attribute Example :
     * "string"
     */
    @ApiModelProperty(value = "Name", example = "Fuel Level is low", required = true)
    private String eventName;
    /*
     * Example : "string"
     */
    @ApiModelProperty(value = "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    /*
     * Event type. Need constant data for this attribute Example : "string"
     */
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;
    @ApiModelProperty(value = "Specify wether alert is read or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean readFlag;
    @ApiModelProperty(value = "Is alert is new(open) or hostorical", example = "true", allowableValues = "true,false", required = true)
    private Boolean isOpen;
    @JsonIgnore
    private Boolean isUpdated;
    @JsonIgnore
    private Boolean isGenerated = false;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;
    @ApiModelProperty(value = "Specify wether alert is dtc alert or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;
    @ApiModelProperty(value = "Is alert visible on UI or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean isCustomerVisible;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date updatedAt;

    @ApiModelProperty(value = "createdBy", example = "A/S", required = true)
    private String createdBy;

    public Alert() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Date getEventGeneratedTime() {
        return eventGeneratedTime;
    }

    public void setEventGeneratedTime(Date eventGeneratedTime) {
        this.eventGeneratedTime = eventGeneratedTime;
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

    public EventLevel getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(EventLevel eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Boolean getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Boolean readFlag) {
        this.readFlag = readFlag;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Boolean getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(Boolean isGenerated) {
        this.isGenerated = isGenerated;
    }

    public Boolean getIsDtcAlert() {
        return isDtcAlert;
    }

    public void setIsDtcAlert(Boolean isDtcAlert) {
        this.isDtcAlert = isDtcAlert;
    }

    public Boolean getIsCustomerVisible() {
        return isCustomerVisible;
    }

    public void setIsCustomerVisible(Boolean isCustomerVisible) {
        this.isCustomerVisible = isCustomerVisible;
    }



    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Alert [id=" + id + ", eventDescription=" + eventDescription + ", eventGeneratedTime="
                + eventGeneratedTime + ", latitude=" + latitude + ", longitude=" + longitude + ", location=" + location
                + ", eventLevel=" + eventLevel + ", eventName=" + eventName + ", eventType=" + eventType + ", machine="
                + machine + ", vin=" + vin + ", readFlag=" + readFlag + ", isOpen=" + isOpen + ", isUpdated="
                + isUpdated + ", is_generated=" + isGenerated + "]";
    }

    public Alert(String id, String eventDescription, Date eventGeneratedTime, Double latitude, Double longitude,
                 String location, EventLevel eventLevel, String eventName, EventType eventType, Machine machine, String vin,
                 Boolean readFlag, Boolean isOpen, Boolean isUpdated, Boolean isGenerated) {
        super();
        this.id = id;
        this.eventDescription = eventDescription;
        this.eventGeneratedTime = eventGeneratedTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.eventLevel = eventLevel;
        this.eventName = eventName;
        this.eventType = eventType;
        this.machine = machine;
        this.vin = vin;
        this.readFlag = readFlag;
        this.isOpen = isOpen;
        this.isUpdated = isUpdated;
        this.isGenerated = isGenerated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventGeneratedTime == null) ? 0 : eventGeneratedTime.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((vin == null) ? 0 : vin.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alert other = (Alert) obj;
        if (eventGeneratedTime == null) {
            if (other.eventGeneratedTime != null) {
                return false;
            }
        } else if (!eventGeneratedTime.equals(other.eventGeneratedTime)) {
            return false;
        }
        if (eventType != other.eventType) {
            return false;
        }
        if (vin == null) {
            if (other.vin != null) {
                return false;
            }
        } else if (!vin.equals(other.vin)) {
            return false;
        }
        return true;
    }
}
