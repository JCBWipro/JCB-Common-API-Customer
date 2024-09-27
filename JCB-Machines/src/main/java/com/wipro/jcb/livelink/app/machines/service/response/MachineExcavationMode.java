package com.wipro.jcb.livelink.app.machines.service.response;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class MachineExcavationMode {

    private Double economyMode;
    private Double powerMode;
    private Double activeMode;

    private Integer economyHrs;
    private Integer powerHrs;
    private Integer activeHrs;

    public Double getEconomyMode() {
        return economyMode;
    }
    public void setEconomyMode(Double economyMode) {
        this.economyMode = economyMode;
    }
    public Double getPowerMode() {
        return powerMode;
    }
    public void setPowerMode(Double powerMode) {
        this.powerMode = powerMode;
    }
    public Double getActiveMode() {
        return activeMode;
    }
    public void setActiveMode(Double activeMode) {
        this.activeMode = activeMode;
    }
    public MachineExcavationMode(Double economyMode, Double powerMode, Double activeMode) {
        super();
        this.economyMode = economyMode;
        this.powerMode = powerMode;
        this.activeMode = activeMode;
    }
    public Integer getEconomyHrs() {
        return economyHrs;
    }
    public void setEconomyHrs(Integer economyHrs) {
        this.economyHrs = economyHrs;
    }
    public Integer getPowerHrs() {
        return powerHrs;
    }
    public void setPowerHrs(Integer powerHrs) {
        this.powerHrs = powerHrs;
    }
    public Integer getActiveHrs() {
        return activeHrs;
    }
    public void setActiveHrs(Integer activeHrs) {
        this.activeHrs = activeHrs;
    }
    public MachineExcavationMode(Integer economyHrs, Integer powerHrs, Integer activeHrs) {
        super();
        this.economyHrs = economyHrs;
        this.powerHrs = powerHrs;
        this.activeHrs = activeHrs;
    }
}
