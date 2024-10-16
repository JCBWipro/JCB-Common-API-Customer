package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.dto.MachinePerformanceDataId;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(MachinePerformanceDataId.class)
@Table(name = "machineperformancedata", indexes = {
        @Index(name = "machineperformancedata_vin_day", columnList = "vin,day", unique = true) })
public class MachinePerformanceData implements Serializable {
    @Serial
    private static final long serialVersionUID = -2324951393312834755L;
    @Id
    @JsonIgnore
    private String vinId;
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "day")
    private Date day;
   
    private String model;
    
    private String platform;
    
    @Schema(description = "Powerband high in hours", example = "12.0", required = true)
    @JsonProperty("high")
    private Double powerBandHighInHours;
   
    @Schema(description = "Powerband low in hours", example = "13.0", required = true)
    @JsonProperty("low")
    private Double powerBandLowInHours;
    
    @Schema(description = "Powerband medium in hours", example = "40.0", required = true)
    @JsonProperty("medium")
    private Double powerBandMediumInHours;
   
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="timestamp default current_timestamp", insertable=false, updatable=false)
    private Date createdAt;

}
