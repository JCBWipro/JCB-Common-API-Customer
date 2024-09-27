package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.FuelConsumptionDataId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Entity
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
    /*
     * optional Example : "string" string
     */
    @Schema(description = "Fuel consumed", example = "150.0", required = true)
    @JsonProperty("fuel")
    private Double fuelConsumed;
    /*
     * optional Example : 0.0 number (double)
     */
    @JsonIgnore
    private String model;

    @JsonIgnore
    private String fuelLevel;

    /*
     * optional Example : "string" string
     */
    @JsonIgnore
    private String platform;
    /*
     * optional Example : "string" string
     */
    private String machineType;
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;
    @JsonIgnore
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;
    /*
     * optional Example : "string" string
     */

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable=false)
    private Date createdAt;

    public MachineFuelConsumptionData() {
    }

    public MachineFuelConsumptionData(Date day, Double fuelConsumed, String model, String platform, String machineType,
                                      Machine machine) {
        super();
        this.day = day;
        this.fuelConsumed = fuelConsumed;
        this.model = model;
        this.platform = platform;
        this.machineType = machineType;
        this.machine = machine;
        this.vinId = machine.getVin();
    }

    public MachineFuelConsumptionData(Date day, Double fuelConsumed, String machineType) {
        super();
        this.day = day;
        this.fuelConsumed = fuelConsumed;
        this.machineType = machineType;
    }

    /*
     * public MachineFuelConsumptionData(Date day, Double fuelConsumed, String
     * model, String platform, Machine machine) { super(); this.day = day;
     * this.fuelConsumed = fuelConsumed; this.model = model; this.platform =
     * platform; this.machine = machine; }
     */
    @Override
    public String toString() {
        return "MachineFuelConsumptionData [ day=" + day + ", fuelConsumed=" + fuelConsumed + ", model=" + model
                + ", platform=" + platform + ", vin=" + vin + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + ((fuelConsumed == null) ? 0 : fuelConsumed.hashCode());
        result = prime * result + ((machine == null) ? 0 : machine.hashCode());
        result = prime * result + ((machineType == null) ? 0 : machineType.hashCode());
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + ((platform == null) ? 0 : platform.hashCode());
        result = prime * result + ((vin == null) ? 0 : vin.hashCode());
        result = prime * result + ((vinId == null) ? 0 : vinId.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MachineFuelConsumptionData other = (MachineFuelConsumptionData) obj;
        if (!vin.equals(other.getVin()))
            return false;
        if (day != other.getDay())
            return false;
        return true;
    }

}
