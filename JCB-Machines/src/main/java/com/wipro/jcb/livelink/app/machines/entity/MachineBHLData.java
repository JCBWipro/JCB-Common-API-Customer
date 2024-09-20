package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Entity
@IdClass(MachineBHLDataId.class)
@Table(name = "machine_BHL_data", indexes = {
        @Index(name = "machine_BHL_data_vin_day", columnList = "vin,day", unique = true)})
public class MachineBHLData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8173611194260296433L;
    /**
     *
     */

    @Id
    @JsonIgnore
    private String vinId;
    @ApiModelProperty(value = "Day", example = "2017-07-13", required = true)
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

    @ApiModelProperty(value = "travel attachment", example = "1.1", required = true)
    @JsonProperty("attachment")
    private Double attachment;


    @ApiModelProperty(value = "loading", example = "1.1", required = true)
    @JsonProperty("loading")
    private Double loading;

    //@CreatedDate
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
    protected Date creationAt;

    @ApiModelProperty(value = "excavation", example = "1.1", required = true)
    @JsonProperty("excavation")
    private Double excavation;

    @ApiModelProperty(value = "roading", example = "1.1", required = true)
    @JsonProperty("roading")
    private Double roading;

    @ApiModelProperty(value = "idling", example = "1.1", required = true)
    @JsonProperty("idling")
    private Double idling;

    @ApiModelProperty(value = "economyModeHrs", example = "1.1", required = true)
    @JsonProperty("economy_mode_hrs")
    private Double economyModeHrs;

    @ApiModelProperty(value = "powerModeHrs", example = "1.1", required = true)
    @JsonProperty("power_mode_hrs")
    private Double powerModeHrs;

    @ApiModelProperty(value = "activeModeHrs", example = "1.1", required = true)
    @JsonProperty("active_mode_hrs")
    private Double activeModeHrs;

    @ApiModelProperty(value = "gear1Utilization", example = "1.1", required = true)
    @JsonProperty("gear1Utilization")
    private Double gear1Utilization;

    @ApiModelProperty(value = "gear2Utilization", example = "1.1", required = true)
    @JsonProperty("gear2Utilization")
    private Double gear2Utilization;

    @ApiModelProperty(value = "gear3Utilization", example = "1.1", required = true)
    @JsonProperty("gear3Utilization")
    private Double gear3Utilization;

    @ApiModelProperty(value = "gear4Utilization", example = "1.1", required = true)
    @JsonProperty("gear4Utilization")
    private Double gear4Utilization;

    @ApiModelProperty(value = "forwardDirection", example = "1.1", required = true)
    @JsonProperty("forward_direction")
    private Double forwardDirection;

    @ApiModelProperty(value = "reverseDirection", example = "1.1", required = true)
    @JsonProperty("reverse_direction")
    private Double reverseDirection;

    @ApiModelProperty(value = "neutralDirection", example = "1.1", required = true)
    @JsonProperty("neutral_direction")
    private Double neutralDirection;


    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private Machine machine;

    @ApiModelProperty(value = "fuelUsedInLowIdle", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_low_idle")
    private Double fuelUsedInLowIdle;

    @ApiModelProperty(value = "fuelUsedInLowIdlePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_low_idle_perct")
    private Double fuelUsedInLowIdlePerct;

    @ApiModelProperty(value = "fuelUsedAtHighIdle", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_high_idle")
    private Double fuelUsedAtHighIdle;

    @ApiModelProperty(value = "fuelUsedAtHighIdlePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_highidle_perct")
    private Double fuelUsedAtHighIdlePerct;

    @ApiModelProperty(value = "fuelUsedAtExcavationEcoMode", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_excavation_eco_mode")
    private Double fuelUsedAtExcavationEcoMode;

    @ApiModelProperty(value = "fuelUsedAtExcavationEcoModePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_excavation_eco_mode_perct")
    private Double fuelUsedAtExcavationEcoModePerct;

    @ApiModelProperty(value = "fuelUsedAtLoadingMode", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_loading_mode")
    private Double fuelUsedAtLoadingMode;

    @ApiModelProperty(value = "fuelUsedAtLoadingModePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_loading_mode_perct")
    private Double fuelUsedAtLoadingModePerct;

    @ApiModelProperty(value = "fuelUsedAtRoadingMode", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_roading_mode")
    private Double fuelUsedAtRoadingMode;

    @ApiModelProperty(value = "fuelUsedAtRoadingModePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_at_roading_mode_perct")
    private Double fuelUsedAtRoadingModePerct;

    @ApiModelProperty(value = "totalFuelUsedInLtrs", example = "1.1", required = true)
    @JsonProperty("total_fuel_used_in_ltrs")
    private Double totalFuelUsedInLtrs;

    @ApiModelProperty(value = "averageFuelConsumption", example = "1.1", required = true)
    @JsonProperty("average_fuel_consumption")
    private Double averageFuelConsumption;

    @ApiModelProperty(value = "subidFuelUsedAtExcavationStandardMode", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavationstandard_mode")
    private Double subidFuelUsedAtExcavationStandardMode;

    @ApiModelProperty(value = "subidFuelUsedAtExcavationStandardModePerct", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavation_standard_mode_perct")
    private Double subidFuelUsedAtExcavationStandardModePerct;

    @ApiModelProperty(value = "subidFuelUsedAtExcavationPlusMode", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavation_plus_mode")
    private Double subidFuelUsedAtExcavationPlusMode;

    @ApiModelProperty(value = "subidFuelUsedAtExcavationPlusModePerct", example = "1.1", required = true)
    @JsonProperty("subid_fuel_used_at_excavation_plus_mode_perct")
    private Double subidFuelUsedAtExcavationPlusModePerct;

    //V6 API column

    @ApiModelProperty(value = "distanceTravelledInRoading", example = "1.1", required = true)
    @JsonProperty("distance_travelled_in_roading")
    private Double distanceTravelledInRoading;

    @ApiModelProperty(value = "averageSpeedInRoading", example = "1.1", required = true)
    @JsonProperty("average_speed_in_roading")
    private Double averageSpeedInRoading;

    @ApiModelProperty(value = "fuelUsedInExcavation", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_excavation")
    private Double fuelUsedInExcavation;

    @ApiModelProperty(value = "fuelUsedInExcavationPerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_excavation_perct")
    private Double fuelUsedInExcavationPerct;

    @ApiModelProperty(value = "fuelUsedInIdle", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_idle")
    private Double fuelUsedInIdle;

    @ApiModelProperty(value = "fuelUsedInIdlePerct", example = "1.1", required = true)
    @JsonProperty("fuel_used_in_idle_perct")
    private Double fuelUsedInIdlePerct;


    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;


    public String getVinId() {
        return vinId;
    }


    public void setVinId(String vinId) {
        this.vinId = vinId;
    }


    public Date getDay() {
        return day;
    }


    public void setDay(Date day) {
        this.day = day;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getPlatform() {
        return platform;
    }


    public void setPlatform(String platform) {
        this.platform = platform;
    }


    public Double getAttachment() {
        return attachment;
    }


    public void setAttachment(Double attachment) {
        this.attachment = attachment;
    }


    public Double getLoading() {
        return loading;
    }


    public void setLoading(Double loading) {
        this.loading = loading;
    }


    public Double getExcavation() {
        return excavation;
    }


    public void setExcavation(Double excavation) {
        this.excavation = excavation;
    }


    public Double getRoading() {
        return roading;
    }


    public void setRoading(Double roading) {
        this.roading = roading;
    }


    public Double getIdling() {
        return idling;
    }


    public void setIdling(Double idling) {
        this.idling = idling;
    }


    public Double getEconomyModeHrs() {
        return economyModeHrs;
    }


    public void setEconomyModeHrs(Double economyModeHrs) {
        this.economyModeHrs = economyModeHrs;
    }


    public Double getPowerModeHrs() {
        return powerModeHrs;
    }


    public void setPowerModeHrs(Double powerModeHrs) {
        this.powerModeHrs = powerModeHrs;
    }


    public Double getActiveModeHrs() {
        return activeModeHrs;
    }


    public void setActiveModeHrs(Double activeModeHrs) {
        this.activeModeHrs = activeModeHrs;
    }


    public Double getGear1Utilization() {
        return gear1Utilization;
    }


    public void setGear1Utilization(Double gear1Utilization) {
        this.gear1Utilization = gear1Utilization;
    }


    public Double getGear2Utilization() {
        return gear2Utilization;
    }


    public void setGear2Utilization(Double gear2Utilization) {
        this.gear2Utilization = gear2Utilization;
    }


    public Double getGear3Utilization() {
        return gear3Utilization;
    }


    public void setGear3Utilization(Double gear3Utilization) {
        this.gear3Utilization = gear3Utilization;
    }


    public Double getGear4Utilization() {
        return gear4Utilization;
    }


    public void setGear4Utilization(Double gear4Utilization) {
        this.gear4Utilization = gear4Utilization;
    }


    public Double getForwardDirection() {
        return forwardDirection;
    }


    public void setForwardDirection(Double forwardDirection) {
        this.forwardDirection = forwardDirection;
    }


    public Double getReverseDirection() {
        return reverseDirection;
    }


    public void setReverseDirection(Double reverseDirection) {
        this.reverseDirection = reverseDirection;
    }


    public Double getNeutralDirection() {
        return neutralDirection;
    }


    public void setNeutralDirection(Double neutralDirection) {
        this.neutralDirection = neutralDirection;
    }


    public Double getPrb1() {
        return prb1;
    }


    public void setPrb1(Double prb1) {
        this.prb1 = prb1;
    }


    public Double getPrb2() {
        return prb2;
    }


    public void setPrb2(Double prb2) {
        this.prb2 = prb2;
    }


    public Double getPrb3() {
        return prb3;
    }


    public void setPrb3(Double prb3) {
        this.prb3 = prb3;
    }


    public Double getPrb4() {
        return prb4;
    }


    public void setPrb4(Double prb4) {
        this.prb4 = prb4;
    }


    public Double getPrb5() {
        return prb5;
    }


    public void setPrb5(Double prb5) {
        this.prb5 = prb5;
    }


    public Double getPrb6() {
        return prb6;
    }


    public void setPrb6(Double prb6) {
        this.prb6 = prb6;
    }


    public Double getPrbem1() {
        return prbem1;
    }


    public void setPrbem1(Double prbem1) {
        this.prbem1 = prbem1;
    }


    public Double getPrbem2() {
        return prbem2;
    }


    public void setPrbem2(Double prbem2) {
        this.prbem2 = prbem2;
    }


    public Double getPrbem3() {
        return prbem3;
    }


    public void setPrbem3(Double prbem3) {
        this.prbem3 = prbem3;
    }


    public Double getPrbem4() {
        return prbem4;
    }


    public void setPrbem4(Double prbem4) {
        this.prbem4 = prbem4;
    }


    public Double getPrbem5() {
        return prbem5;
    }


    public void setPrbem5(Double prbem5) {
        this.prbem5 = prbem5;
    }


    public Double getPrbem6() {
        return prbem6;
    }


    public void setPrbem6(Double prbem6) {
        this.prbem6 = prbem6;
    }


    public Double getPdrbem1() {
        return pdrbem1;
    }


    public void setPdrbem1(Double pdrbem1) {
        this.pdrbem1 = pdrbem1;
    }


    public Double getPdrbem2() {
        return pdrbem2;
    }


    public void setPdrbem2(Double pdrbem2) {
        this.pdrbem2 = pdrbem2;
    }


    public Double getPdrbem3() {
        return pdrbem3;
    }


    public void setPdrbem3(Double pdrbem3) {
        this.pdrbem3 = pdrbem3;
    }


    public Double getPdrbem4() {
        return pdrbem4;
    }


    public void setPdrbem4(Double pdrbem4) {
        this.pdrbem4 = pdrbem4;
    }


    public Double getPdrbem5() {
        return pdrbem5;
    }


    public void setPdrbem5(Double pdrbem5) {
        this.pdrbem5 = pdrbem5;
    }


    public Double getPdrbem6() {
        return pdrbem6;
    }


    public void setPdrbem6(Double pdrbem6) {
        this.pdrbem6 = pdrbem6;
    }

		/*public Date getCreationAt() {
			return creationAt;
		}


		public void setCreationAt(Date creationAt) {
			this.creationAt = creationAt;
		}*/


    @ApiModelProperty(value = "prb1", example = "1.1", required = true)
    @JsonProperty("prb1")
    private Double prb1;

    @ApiModelProperty(value = "prb2", example = "1.1", required = true)
    @JsonProperty("prb2")
    private Double prb2;

    @ApiModelProperty(value = "prb3", example = "1.1", required = true)
    @JsonProperty("prb3")
    private Double prb3;

    @ApiModelProperty(value = "prb4", example = "1.1", required = true)
    @JsonProperty("prb4")
    private Double prb4;

    @ApiModelProperty(value = "prb5", example = "1.1", required = true)
    @JsonProperty("prb5")
    private Double prb5;

    @ApiModelProperty(value = "prb6", example = "1.1", required = true)
    @JsonProperty("prb6")
    private Double prb6;

    @ApiModelProperty(value = "prbem1", example = "1.1", required = true)
    @JsonProperty("prbem1")
    private Double prbem1;

    @ApiModelProperty(value = "prbem2", example = "1.1", required = true)
    @JsonProperty("prbem2")
    private Double prbem2;

    @ApiModelProperty(value = "prbem3", example = "1.1", required = true)
    @JsonProperty("prbem3")
    private Double prbem3;

    @ApiModelProperty(value = "prbem4", example = "1.1", required = true)
    @JsonProperty("prbem4")
    private Double prbem4;

    @ApiModelProperty(value = "prbem5", example = "1.1", required = true)
    @JsonProperty("prbem5")
    private Double prbem5;

    @ApiModelProperty(value = "prbem6", example = "1.1", required = true)
    @JsonProperty("prbem6")
    private Double prbem6;

    @ApiModelProperty(value = "pdrbem1", example = "1.1", required = true)
    @JsonProperty("pdrbem1")
    private Double pdrbem1;


    @ApiModelProperty(value = "pdrbem2", example = "1.1", required = true)
    @JsonProperty("pdrbem2")
    private Double pdrbem2;


    @ApiModelProperty(value = "pdrbem3", example = "1.1", required = true)
    @JsonProperty("pdrbem3")
    private Double pdrbem3;


    @ApiModelProperty(value = "pdrbem4", example = "1.1", required = true)
    @JsonProperty("pdrbem4")
    private Double pdrbem4;


    @ApiModelProperty(value = "pdrbem5", example = "1.1", required = true)
    @JsonProperty("pdrbem5")
    private Double pdrbem5;


    @ApiModelProperty(value = "pdrbem6", example = "1.1", required = true)
    @JsonProperty("pdrbem6")
    private Double pdrbem6;


    public String getVin() {
        return vin;
    }


    public void setVin(String vin) {
        this.vin = vin;
    }


    public Machine getMachine() {
        return machine;
    }


    public void setMachine(Machine machine) {
        this.machine = machine;
    }


    public Date getCreationAt() {
        return creationAt;
    }


    public void setCreationAt(Date creationAt) {
        this.creationAt = creationAt;
    }


    public Double getFuelUsedInLowIdle() {
        return fuelUsedInLowIdle;
    }


    public void setFuelUsedInLowIdle(Double fuelUsedInLowIdle) {
        this.fuelUsedInLowIdle = fuelUsedInLowIdle;
    }


    public Double getFuelUsedInLowIdlePerct() {
        return fuelUsedInLowIdlePerct;
    }


    public void setFuelUsedInLowIdlePerct(Double fuelUsedInLowIdlePerct) {
        this.fuelUsedInLowIdlePerct = fuelUsedInLowIdlePerct;
    }


    public Double getFuelUsedAtHighIdle() {
        return fuelUsedAtHighIdle;
    }


    public void setFuelUsedAtHighIdle(Double fuelUsedAtHighIdle) {
        this.fuelUsedAtHighIdle = fuelUsedAtHighIdle;
    }


    public Double getFuelUsedAtHighIdlePerct() {
        return fuelUsedAtHighIdlePerct;
    }


    public void setFuelUsedAtHighIdlePerct(Double fuelUsedAtHighIdlePerct) {
        this.fuelUsedAtHighIdlePerct = fuelUsedAtHighIdlePerct;
    }


    public Double getFuelUsedAtExcavationEcoMode() {
        return fuelUsedAtExcavationEcoMode;
    }


    public void setFuelUsedAtExcavationEcoMode(Double fuelUsedAtExcavationEcoMode) {
        this.fuelUsedAtExcavationEcoMode = fuelUsedAtExcavationEcoMode;
    }


    public Double getFuelUsedAtExcavationEcoModePerct() {
        return fuelUsedAtExcavationEcoModePerct;
    }


    public void setFuelUsedAtExcavationEcoModePerct(Double fuelUsedAtExcavationEcoModePerct) {
        this.fuelUsedAtExcavationEcoModePerct = fuelUsedAtExcavationEcoModePerct;
    }


    public Double getFuelUsedAtLoadingMode() {
        return fuelUsedAtLoadingMode;
    }


    public void setFuelUsedAtLoadingMode(Double fuelUsedAtLoadingMode) {
        this.fuelUsedAtLoadingMode = fuelUsedAtLoadingMode;
    }


    public Double getFuelUsedAtLoadingModePerct() {
        return fuelUsedAtLoadingModePerct;
    }


    public void setFuelUsedAtLoadingModePerct(Double fuelUsedAtLoadingModePerct) {
        this.fuelUsedAtLoadingModePerct = fuelUsedAtLoadingModePerct;
    }


    public Double getFuelUsedAtRoadingMode() {
        return fuelUsedAtRoadingMode;
    }


    public void setFuelUsedAtRoadingMode(Double fuelUsedAtRoadingMode) {
        this.fuelUsedAtRoadingMode = fuelUsedAtRoadingMode;
    }


    public Double getFuelUsedAtRoadingModePerct() {
        return fuelUsedAtRoadingModePerct;
    }


    public void setFuelUsedAtRoadingModePerct(Double fuelUsedAtRoadingModePerct) {
        this.fuelUsedAtRoadingModePerct = fuelUsedAtRoadingModePerct;
    }


    public Double getTotalFuelUsedInLtrs() {
        return totalFuelUsedInLtrs;
    }


    public void setTotalFuelUsedInLtrs(Double totalFuelUsedInLtrs) {
        this.totalFuelUsedInLtrs = totalFuelUsedInLtrs;
    }


    public Double getAverageFuelConsumption() {
        return averageFuelConsumption;
    }


    public void setAverageFuelConsumption(Double averageFuelConsumption) {
        this.averageFuelConsumption = averageFuelConsumption;
    }


    public Double getSubidFuelUsedAtExcavationStandardMode() {
        return subidFuelUsedAtExcavationStandardMode;
    }


    public void setSubidFuelUsedAtExcavationStandardMode(Double subidFuelUsedAtExcavationStandardMode) {
        this.subidFuelUsedAtExcavationStandardMode = subidFuelUsedAtExcavationStandardMode;
    }


    public Double getSubidFuelUsedAtExcavationStandardModePerct() {
        return subidFuelUsedAtExcavationStandardModePerct;
    }


    public void setSubidFuelUsedAtExcavationStandardModePerct(Double subidFuelUsedAtExcavationStandardModePerct) {
        this.subidFuelUsedAtExcavationStandardModePerct = subidFuelUsedAtExcavationStandardModePerct;
    }


    public Double getSubidFuelUsedAtExcavationPlusMode() {
        return subidFuelUsedAtExcavationPlusMode;
    }


    public void setSubidFuelUsedAtExcavationPlusMode(Double subidFuelUsedAtExcavationPlusMode) {
        this.subidFuelUsedAtExcavationPlusMode = subidFuelUsedAtExcavationPlusMode;
    }


    public Double getSubidFuelUsedAtExcavationPlusModePerct() {
        return subidFuelUsedAtExcavationPlusModePerct;
    }


    public void setSubidFuelUsedAtExcavationPlusModePerct(Double subidFuelUsedAtExcavationPlusModePerct) {
        this.subidFuelUsedAtExcavationPlusModePerct = subidFuelUsedAtExcavationPlusModePerct;
    }


    public Double getDistanceTravelledInRoading() {
        return distanceTravelledInRoading;
    }


    public Double getAverageSpeedInRoading() {
        return averageSpeedInRoading;
    }


    public Double getFuelUsedInExcavation() {
        return fuelUsedInExcavation;
    }


    public Double getFuelUsedInExcavationPerct() {
        return fuelUsedInExcavationPerct;
    }


    public Double getFuelUsedInIdle() {
        return fuelUsedInIdle;
    }


    public Double getFuelUsedInIdlePerct() {
        return fuelUsedInIdlePerct;
    }


    public void setDistanceTravelledInRoading(Double distanceTravelledInRoading) {
        this.distanceTravelledInRoading = distanceTravelledInRoading;
    }


    public void setAverageSpeedInRoading(Double averageSpeedInRoading) {
        this.averageSpeedInRoading = averageSpeedInRoading;
    }


    public void setFuelUsedInExcavation(Double fuelUsedInExcavation) {
        this.fuelUsedInExcavation = fuelUsedInExcavation;
    }


    public void setFuelUsedInExcavationPerct(Double fuelUsedInExcavationPerct) {
        this.fuelUsedInExcavationPerct = fuelUsedInExcavationPerct;
    }


    public void setFuelUsedInIdle(Double fuelUsedInIdle) {
        this.fuelUsedInIdle = fuelUsedInIdle;
    }


    public void setFuelUsedInIdlePerct(Double fuelUsedInIdlePerct) {
        this.fuelUsedInIdlePerct = fuelUsedInIdlePerct;
    }


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

