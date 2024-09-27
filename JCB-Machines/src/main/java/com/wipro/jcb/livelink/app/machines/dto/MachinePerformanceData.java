package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineUtilizationData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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

/*Machine performance information of machines per day.*/
@Setter
@Entity
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
    /*
     * optional Example : "string" string
     */
    private String model;
    /*
     * optional Example : "string" string
     */
    private String platform;
    /*
     * optional Example : "string" string
     */
    @Schema(description = "Powerband high in hours", example = "12.0", required = true)
    @JsonProperty("high")
    private Double powerBandHighInHours;
    /*
     * optional Example : 0.0 number (double)
     */
    @Schema(description = "Powerband low in hours", example = "13.0", required = true)
    @JsonProperty("low")
    private Double powerBandLowInHours;
    /*
     * optional Example : 0.0 number (double)
     */
    @Schema(description = "Powerband medium in hours", example = "40.0", required = true)
    @JsonProperty("medium")
    private Double powerBandMediumInHours;
    /*
     * optional Example : 0.0 number (double)
     */
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

    /*
     * optional Example : "string" string
     */
    public MachinePerformanceData() {
    }

    public MachinePerformanceData(Date day, String model, String platform, Double powerBandHighInHours,
                                  Double powerBandLowInHours, Double powerBandMediumInHours, Machine machine) {
        super();
        this.day = day;
        this.model = model;
        this.platform = platform;
        this.powerBandHighInHours = powerBandHighInHours;
        this.powerBandLowInHours = powerBandLowInHours;
        this.powerBandMediumInHours = powerBandMediumInHours;
        this.machine = machine;
        this.vinId = machine.getVin();
    }

    @Override
    public String toString() {
        return "MachinePerformanceData [ day=" + day + ", model=" + model + ", platform=" + platform
                + ", powerBandHighInHours=" + powerBandHighInHours + ", powerBandLowInHours=" + powerBandLowInHours
                + ", powerBandMediumInHours=" + powerBandMediumInHours + ", vin=" + vin + "]";
    }

    public Date getDay() {
        return day;
    }

    @JsonIgnore
    public String getModel() {
        return model;
    }

    @JsonIgnore
    public String getPlatform() {
        return platform;
    }

    public Double getPowerBandHighInHours() {
        return powerBandHighInHours;
    }

    public Double getPowerBandLowInHours() {
        return powerBandLowInHours;
    }

    public Double getPowerBandMediumInHours() {
        return powerBandMediumInHours;
    }

    @JsonIgnore
    public String getVin() {
        return vin;
    }

    public String getVinId() {
        return vinId;
    }

    public Machine getMachine() {
        return machine;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vin == null) ? 0 : vin.hashCode());
        result = prime * result + ((day == null) ? 0 : day.hashCode());
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
        MachineUtilizationData other = (MachineUtilizationData) obj;
        if (!vin.equals(other.getVin()))
            return false;
        return day == other.getDay();
    }
}
