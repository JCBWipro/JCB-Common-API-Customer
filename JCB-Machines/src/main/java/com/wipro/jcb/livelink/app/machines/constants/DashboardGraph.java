package com.wipro.jcb.livelink.app.machines.constants;

import lombok.Getter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-11-2024
 */
@Getter
public enum DashboardGraph {
    PLATFORM("platformDist"),
    ALERT("alerts"),
    SERVICE_STATUS("serviceStatus") ,
    UTILIZATION("utilization"),
    MACHINE_LOCATER("machineLocator"),
    CONNECTIVITY("connectivity"),
    RENEWAL_STATUS("renewalStatus"),
    WARRANTY("warranty"),
    ALL("ALL");

    private final String name ;
    DashboardGraph(String name){
        this.name=name;

    }
}
