package com.wipro.jcb.livelink.app.reports.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.dto.FuelConsumptionDataId;

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
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@IdClass(FuelConsumptionDataId.class)
@Table(name = "machinefuelconsumptionData", indexes = {
        @Index(name = "machinefuelconsumptionData_vin_day", columnList = "vin,day", unique = true) })
public class MachineFuelConsumptionData implements Serializable {
    @Serial
    private static final long serialVersionUID = -8449237240132153770L;
    @Id
    @JsonIgnore
    private String vinId;
    @Schema(description = "Day", example = "2017-07-13", required = true)
    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "day")
    private Date day;
    
    @Schema(description = "Fuel consumed", example = "150.0", required = true)
    @JsonProperty("fuel")
    private Double fuelConsumed;
    
    @JsonIgnore
    private String model;

    @JsonIgnore
    private String fuelLevel;

    @JsonIgnore
    private String platform;
    
    private String machineType;
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;
    @JsonIgnore
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable=false)
    private Date createdAt;
    
    public MachineFuelConsumptionData(Date day, Double fuelConsumed, String machineType) {
        super();
        this.day = day;
        this.fuelConsumed = fuelConsumed;
        this.machineType = machineType;
    }

}
