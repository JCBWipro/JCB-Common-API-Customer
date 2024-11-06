package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * DealerDashboard is a data model class that represents the dashboard information for a dealer.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class DealerDashboard {
    @Getter
    @ApiModelProperty(value = "All platforms with platform and total machine count", example = "", required = true)
    private CategoryMachineCountList platformDist;
    @Getter
    @ApiModelProperty(value = "All alerts with alerts level count and total machine count", example = "", required = true)
    private CategoryMachineCountList alerts;
    @ApiModelProperty(value = "All machines with serviceStatus and total machine count", example = "", required = true)
    private CategoryMachineCountList serviceStatus;
    @ApiModelProperty(value = "All utilization status with utilization and total machine count", example = "", required = true)
    private CategoryMachineCountList utilization;
    @ApiModelProperty(value = "All machineLocator with locator status and total machine count", example = "", required = true)
    private CategoryMachineCountList machineLocator;
    @ApiModelProperty(value = "All machines connectivity with platform count and total machine count", example = "", required = true)
    private CategoryMachineCountList connectivity;
    @Getter
    @ApiModelProperty(value = "All machines connectivity with platform count and total machine count", example = "", required = true)
    private TopCustomerRecords topCustomers;
    @ApiModelProperty(value = "All machines warranty status with warranty and total machine count", example = "", required = true)
    private CategoryMachineCountList warranty;
    @ApiModelProperty(value = "All machines renewalStatus with renewalStatus and total machine count", example = "", required = true)
    private CategoryMachineCountList renewalStatus;

    public DealerDashboard() {
        this.topCustomers = new TopCustomerRecords();
        this.platformDist = new CategoryMachineCountList();
        this.alerts = new CategoryMachineCountList();
        this.utilization = new CategoryMachineCountList();
        this.serviceStatus = new CategoryMachineCountList();
        this.machineLocator = new CategoryMachineCountList();
        this.connectivity = new CategoryMachineCountList();
        this.warranty = new CategoryMachineCountList();
        this.renewalStatus = new CategoryMachineCountList();
    }
}
