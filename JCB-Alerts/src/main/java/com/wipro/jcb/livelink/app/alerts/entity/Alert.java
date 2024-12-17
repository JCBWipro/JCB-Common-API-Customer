package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.alerts.constants.EventLevel;
import com.wipro.jcb.livelink.app.alerts.constants.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 *
 */
@Entity
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Alert", uniqueConstraints = @UniqueConstraint(columnNames = { "eventGeneratedTime", "eventType",
        "vin" ,"eventName" }))
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert implements Serializable {
    @Serial
    private static final long serialVersionUID = -3118714757588194755L;
    @Setter
    @Getter
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
    @Setter
    @Getter
    @Schema(description = "Description", example = "Fuel Level is low and machine is outside operational hours", required = true)
    private String eventDescription;
    /*
     * Example : "string"
     */
    @Setter
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Generated Time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date eventGeneratedTime;
    /*
     * date in miliseconds in UTC Example : 0
     */
    @Setter
    @Getter
    @Schema(description = "Latitude", example = "18.5204", required = true)
    private Double latitude;
    /*
     * Latitude part of machine GPS co-ordinates Example : 0.0
     */
    @Setter
    @Getter
    @Schema(description = "Longitude", example = "73.8567", required = true)
    private Double longitude;
    /*
     * Longitude part of machine GPS co-ordinates Example : 0.0
     */
    @Setter
    @Getter
    @Schema(description = "Location", example = "Pune, Maharashtra, India", required = true)
    private String location;
    /*
     * GPS address in words Example : "string"
     */
    @Setter
    @Getter
    @Schema(description = "Nature of Fault", example = "YELLOW", allowableValues = "YELLOW,RED", required = true)
    @Enumerated(EnumType.STRING)
    private EventLevel eventLevel;
    /*
     * Event level/severity. Need constant data for this attribute Example :
     * "string"
     */
    @Getter
    @Setter
    @Schema(description = "Name", example = "Fuel Level is low", required = true)
    private String eventName;
    /*
     * Example : "string"
     */
    @Getter
    @Setter
    @Schema(description = "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    /*
     * Event type. Need constant data for this attribute Example : "string"
     */
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;

    @Getter
    @Setter
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;
    @Getter
    @Setter
    @Schema(description = "Specify wether alert is read or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean readFlag;
    @Getter
    @Setter
    @Schema(description = "Is alert is new(open) or hostorical", example = "true", allowableValues = "true,false", required = true)
    private Boolean isOpen;
    @Getter
    @Setter
    @JsonIgnore
    private Boolean isUpdated;
    @Getter
    @Setter
    @JsonIgnore
    private Boolean isGenerated = false;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;
    @Getter
    @Setter
    @Schema(description = "Specify wether alert is dtc alert or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;
    @Getter
    @Setter
    @Schema(description = "Is alert visible on UI or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean isCustomerVisible;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date updatedAt;

    @Getter
    @Setter
    @Schema(description = "createdBy", example = "A/S", required = true)
    private String createdBy;

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
