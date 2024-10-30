package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.enums.EventLevel;
import com.wipro.jcb.livelink.app.reports.enums.EventType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Alert", uniqueConstraints = @UniqueConstraint(columnNames = { "eventGeneratedTime", "eventType",
        "vin" ,"eventName" }))
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert implements Serializable {
    @Serial
    private static final long serialVersionUID = -3118714757588194755L;
    
    @Id
    @Column(name = "alert_id", unique = true, nullable = false)
    private String id;
    
    @Schema(description = "Description", example = "Fuel Level is low and machine is outside operational hours", required = true)
    private String eventDescription;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Generated Time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date eventGeneratedTime;
   
    @Schema(description = "Latitude", example = "18.5204", required = true)
    private Double latitude;
   
    @Schema(description = "Longitude", example = "73.8567", required = true)
    private Double longitude;
   
    @Schema(description = "Location", example = "Pune, Maharashtra, India", required = true)
    private String location;
    
    @Schema(description = "Nature of Fault", example = "YELLOW", allowableValues = "YELLOW,RED", required = true)
    @Enumerated(EnumType.STRING)
    private EventLevel eventLevel;
    
    @Schema(description = "Name", example = "Fuel Level is low", required = true)
    private String eventName;
    
    @Schema(description = "Type", example = "Health", allowableValues = "Service,Health,Security,Utilization,Location", required = true)
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;

   
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;
   
    @Schema(description = "Specify wether alert is read or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean readFlag;
   
    @Schema(description = "Is alert is new(open) or hostorical", example = "true", allowableValues = "true,false", required = true)
    private Boolean isOpen;
   
    @JsonIgnore
    private Boolean isUpdated;
    
    @JsonIgnore
    private Boolean isGenerated = false;
    
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;
   
    @Schema(description = "Specify wether alert is dtc alert or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean isDtcAlert;
   
    @Schema(description = "Is alert visible on UI or not", example = "true", allowableValues = "true,false", required = true)
    private Boolean isCustomerVisible;
    
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date updatedAt;

    @Schema(description = "createdBy", example = "A/S", required = true)
    private String createdBy;

}
