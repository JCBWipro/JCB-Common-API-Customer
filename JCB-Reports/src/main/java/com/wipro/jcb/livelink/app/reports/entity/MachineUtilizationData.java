package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.dto.UtilizationDataId;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UtilizationDataId.class)
@Table(name = "machineutilizationdata", indexes = {
        @Index(name = "machineutilizationdata_vin_day", columnList = "vin,day", unique = true),
        @Index(name = "machineutilizationdata_vin_day_workingHour", columnList = "vin,day,workingHours", unique = false)})
public class MachineUtilizationData implements Serializable {
    @Serial
    private static final long serialVersionUID = -5689331260927188209L;
    @Id
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("date")
    @Column(name = "day")
    private Date day;
    
    @Schema(description = "Idle hours", example = "20.0", required = true)
    @JsonProperty("idle")
    private Double idleHours;
    
    private String model;
    
    @Schema(description = "Off hours", example = "10.0", required = true)
    @JsonProperty("off")
    private Double offHours;
    
    private String platform;
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;
    
    @Schema(description = "Working hours", example = "120.0", required = true)
    @JsonProperty("working")
    private Double workingHours;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable=false)
    private Date createdAt;

    @Id
    @JsonIgnore
    private String vinId;

}