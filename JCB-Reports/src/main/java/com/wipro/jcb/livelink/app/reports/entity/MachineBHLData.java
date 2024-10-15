package com.wipro.jcb.livelink.app.reports.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.reports.dto.MachineBHLDataId;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@IdClass(MachineBHLDataId.class)
@Table(name = "machine_BHL_data", indexes = {
        @Index(name = "machine_BHL_data_vin_day", columnList = "vin,day", unique = true)})
public class MachineBHLData implements Serializable {
    @Serial
    private static final long serialVersionUID = 8173611194260296433L;
    
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

    @Schema(description = "travel attachment", example = "1.1", required = true)
    @JsonProperty("attachment")
    private Double attachment;


    @Schema(description = "loading", example = "1.1", required = true)
    @JsonProperty("loading")
    private Double loading;

    //@CreatedDate
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
    protected Date creationAt;

    @Schema(description = "excavation", example = "1.1", required = true)
    @JsonProperty("excavation")
    private Double excavation;

    @Schema(description = "roading", example = "1.1", required = true)
    @JsonProperty("roading")
    private Double roading;

    @Schema(description = "idling", example = "1.1", required = true)
    @JsonProperty("idling")
    private Double idling;

    @Schema(description = "economyModeHrs", example = "1.1", required = true)
    @JsonProperty("economy_mode_hrs")
    private Double economyModeHrs;

    @Schema(description = "powerModeHrs", example = "1.1", required = true)
    @JsonProperty("power_mode_hrs")
    private Double powerModeHrs;

    @Schema(description = "activeModeHrs", example = "1.1", required = true)
    @JsonProperty("active_mode_hrs")
    private Double activeModeHrs;

    @Schema(description = "gear1Utilization", example = "1.1", required = true)
    @JsonProperty("gear1Utilization")
    private Double gear1Utilization;

    @Schema(description = "gear2Utilization", example = "1.1", required = true)
    @JsonProperty("gear2Utilization")
    private Double gear2Utilization;

    @Schema(description = "gear3Utilization", example = "1.1", required = true)
    @JsonProperty("gear3Utilization")
    private Double gear3Utilization;

    @Schema(description = "gear4Utilization", example = "1.1", required = true)
    @JsonProperty("gear4Utilization")
    private Double gear4Utilization;

    @Schema(description = "forwardDirection", example = "1.1", required = true)
    @JsonProperty("forward_direction")
    private Double forwardDirection;

    @Schema(description = "reverseDirection", example = "1.1", required = true)
    @JsonProperty("reverse_direction")
    private Double reverseDirection;

    @Schema(description = "neutralDirection", example = "1.1", required = true)
    @JsonProperty("neutral_direction")
    private Double neutralDirection;


    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;

    @Schema(description = "fuelUsedInLowIdle", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_low_idle")
    private Double fuelUsedInLowIdle;

    @Schema(description = "fuelUsedInLowIdlePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_low_idle_perct")
    private Double fuelUsedInLowIdlePerct;

    @Schema(description = "fuelUsedAtHighIdle", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_high_idle")
    private Double fuelUsedAtHighIdle;

    @Schema(description = "fuelUsedAtHighIdlePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_highidle_perct")
    private Double fuelUsedAtHighIdlePerct;

    @Schema(description = "fuelUsedAtExcavationEcoMode", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_excavation_eco_mode")
    private Double fuelUsedAtExcavationEcoMode;

    @Schema(description = "fuelUsedAtExcavationEcoModePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_excavation_eco_mode_perct")
    private Double fuelUsedAtExcavationEcoModePerct;

    @Schema(description = "fuelUsedAtLoadingMode", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_loading_mode")
    private Double fuelUsedAtLoadingMode;

    @Schema(description = "fuelUsedAtLoadingModePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_loading_mode_perct")
    private Double fuelUsedAtLoadingModePerct;

    @Schema(description = "fuelUsedAtRoadingMode", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_roading_mode")
    private Double fuelUsedAtRoadingMode;

    @Schema(description = "fuelUsedAtRoadingModePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_roading_mode_perct")
    private Double fuelUsedAtRoadingModePerct;

    @Schema(description = "totalFuelUsedInLtrs", example = "1.1", required = true)
    @JsonProperty("total_fuel_used_in_ltrs")
    private Double totalFuelUsedInLtrs;

    @Schema(description = "averageFuelConsumption", example = "1.1", required = true)
    @JsonProperty("average_fuel_consumption")
    private Double averageFuelConsumption;

    @Schema(description = "subidFuelUsedAtExcavationStandardMode", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavationstandard_mode")
    private Double subidFuelUsedAtExcavationStandardMode;

    @Schema(description = "subidFuelUsedAtExcavationStandardModePerct", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavation_standard_mode_perct")
    private Double subidFuelUsedAtExcavationStandardModePerct;

    @Schema(description = "subidFuelUsedAtExcavationPlusMode", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavation_plus_mode")
    private Double subidFuelUsedAtExcavationPlusMode;

    @Schema(description = "subidFuelUsedAtExcavationPlusModePerct", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavation_plus_mode_perct")
    private Double subidFuelUsedAtExcavationPlusModePerct;

    //V6 API column

    @Schema(description = "distanceTravelledInRoading", example = "1.1", required = true)
    @JsonProperty("distance_travelled_in_roading")
    private Double distanceTravelledInRoading;

    @Schema(description = "averageSpeedInRoading", example = "1.1", required = true)
    @JsonProperty("average_speed_in_roading")
    private Double averageSpeedInRoading;

    @Schema(description = "fuelUsedInExcavation", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_excavation")
    private Double fuelUsedInExcavation;

    @Schema(description = "fuelUsedInExcavationPerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_excavation_perct")
    private Double fuelUsedInExcavationPerct;

    @Schema(description = "fuelUsedInIdle", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_idle")
    private Double fuelUsedInIdle;

    @Schema(description = "fuelUsedInIdlePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_idle_perct")
    private Double fuelUsedInIdlePerct;


    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;


    @Schema(description = "prb1", example = "1.1", required = true)
    @JsonProperty("prb1")
    private Double prb1;

    @Schema(description = "prb2", example = "1.1", required = true)
    @JsonProperty("prb2")
    private Double prb2;

    @Schema(description = "prb3", example = "1.1", required = true)
    @JsonProperty("prb3")
    private Double prb3;

    @Schema(description = "prb4", example = "1.1", required = true)
    @JsonProperty("prb4")
    private Double prb4;

    @Schema(description = "prb5", example = "1.1", required = true)
    @JsonProperty("prb5")
    private Double prb5;

    @Schema(description = "prb6", example = "1.1", required = true)
    @JsonProperty("prb6")
    private Double prb6;

    @Schema(description = "prbem1", example = "1.1", required = true)
    @JsonProperty("prbem1")
    private Double prbem1;

    @Schema(description = "prbem2", example = "1.1", required = true)
    @JsonProperty("prbem2")
    private Double prbem2;

    @Schema(description = "prbem3", example = "1.1", required = true)
    @JsonProperty("prbem3")
    private Double prbem3;

    @Schema(description = "prbem4", example = "1.1", required = true)
    @JsonProperty("prbem4")
    private Double prbem4;

    @Schema(description = "prbem5", example = "1.1", required = true)
    @JsonProperty("prbem5")
    private Double prbem5;

    @Schema(description = "prbem6", example = "1.1", required = true)
    @JsonProperty("prbem6")
    private Double prbem6;

    @Schema(description = "pdrbem1", example = "1.1", required = true)
    @JsonProperty("pdrbem1")
    private Double pdrbem1;


    @Schema(description = "pdrbem2", example = "1.1", required = true)
    @JsonProperty("pdrbem2")
    private Double pdrbem2;


    @Schema(description = "pdrbem3", example = "1.1", required = true)
    @JsonProperty("pdrbem3")
    private Double pdrbem3;


    @Schema(description = "pdrbem4", example = "1.1", required = true)
    @JsonProperty("pdrbem4")
    private Double pdrbem4;


    @Schema(description = "pdrbem5", example = "1.1", required = true)
    @JsonProperty("pdrbem5")
    private Double pdrbem5;


    @Schema(description = "pdrbem6", example = "1.1", required = true)
    @JsonProperty("pdrbem6")
    private Double pdrbem6;


    @Override
    public String toString() {
        return "MachineBHLData [vinId=" + vinId + ", day=" + day + ", model=" + model + ", platform=" + platform
                + ", attachment=" + attachment + ", loading=" + loading + ", creationAt=" + creationAt
                + ", excavation=" + excavation + ", roading=" + roading + ", idling=" + idling + ", economyModeHrs="
                + economyModeHrs + ", powerModeHrs=" + powerModeHrs + ", activeModeHrs=" + activeModeHrs
                + ", gear1Utilization=" + gear1Utilization + ", gear2Utilization=" + gear2Utilization
                + ", gear3Utilization=" + gear3Utilization + ", gear4Utilization=" + gear4Utilization
                + ", forwardDirection=" + forwardDirection + ", reverseDirection=" + reverseDirection
                + ", neutralDirection=" + neutralDirection + ", machine=" + machine + ", vin=" + vin + ", prb1="
                + prb1 + ", prb2=" + prb2 + ", prb3=" + prb3 + ", prb4=" + prb4 + ", prb5=" + prb5 + ", prb6="
                + prb6 + ", prbem1=" + prbem1 + ", prbem2=" + prbem2 + ", prbem3=" + prbem3 + ", prbem4=" + prbem4
                + ", prbem5=" + prbem5 + ", prbem6=" + prbem6 + ", pdrbem1=" + pdrbem1 + ", pdrbem2=" + pdrbem2
                + ", pdrbem3=" + pdrbem3 + ", pdrbem4=" + pdrbem4 + ", pdrbem5=" + pdrbem5 + ", pdrbem6=" + pdrbem6
                + "]";
    }


}

