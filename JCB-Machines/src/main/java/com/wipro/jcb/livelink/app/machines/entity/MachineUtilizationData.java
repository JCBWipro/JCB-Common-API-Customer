package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.UtilizationDataId;
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
@Setter
@Entity
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
    /*
     * optional Example : "string" string
     */
    @Schema(description = "Idle hours", example = "20.0", required = true)
    @JsonProperty("idle")
    private Double idleHours;
    /*
     * optional Example : 0.0 number (double)
     */
    private String model;
    /*
     * optional Example : "string" string
     */
    @Schema(description = "Off hours", example = "10.0", required = true)
    @JsonProperty("off")
    private Double offHours;
    /*
     * optional Example : 0.0 number (double)
     */
    private String platform;
    /*
     * optional Example : "string" string
     */
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;
    /*
     * optional Example : "string" string
     */
    @Schema(description = "Working hours", example = "120.0", required = true)
    @JsonProperty("working")
    private Double workingHours;

    /*
     * optional Example : 0.0 number (double)
     */

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable=false)
    private Date createdAt;

    public MachineUtilizationData() {
    }

    @Id
    @JsonIgnore
    private String vinId;

    public MachineUtilizationData(Date day, Double idleHours, String model, Double offHours, String platform,
                                  Machine machine, Double workingHours) {
        super();
        this.day = day;
        this.idleHours = idleHours;
        this.model = model;
        this.offHours = offHours;
        this.platform = platform;
        this.machine = machine;
        this.workingHours = workingHours;
        this.vinId = machine.getVin();
    }

    @Override
    public String toString() {
        return "MachineUtilizationData [day=" + day + ", idleHours=" + idleHours + ", model=" + model + ", offHours="
                + offHours + ", platform=" + platform + ", vin=" + vin + ", workingHours=" + workingHours + "]";
    }

    public Date getDay() {
        return day;
    }

    public Double getIdleHours() {
        return idleHours;
    }

    @JsonIgnore
    public String getModel() {
        return model;
    }

    public Double getOffHours() {
        return offHours;
    }

    @JsonIgnore
    public String getPlatform() {
        return platform;
    }

    @JsonIgnore
    public String getVin() {
        return vin;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    @JsonIgnore
    public Machine getMachine() {
        return machine;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vin == null) ? 0 : vin.hashCode());
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        return result;
    }

    public String getVinId() {
        return vinId;
    }

    public Date getCreatedAt() {
        return createdAt;
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
        if (day != other.getDay())
            return false;
        return true;
    }
}