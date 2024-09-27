package com.wipro.jcb.livelink.app.machines.enums;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
public enum ServiceStatus {
    SERVICEDUE_DATE("SERVICEDUE", "DATE"), SERVICEDUE_HOUR("SERVICEDUE", "HMR"), SERVICEOVERDUE_DATE("SERVICEOVERDUE",
            "DATE"), SERVICEOVERDUE_HOUR("SERVICEOVERDUE", "HMR"), NORMAL("NORMAL", "NA");
    private String serviceStatusName;
    private String serviceStatusReason;

    ServiceStatus(String serviceStatusName, String serviceStatusReason) {
        this.serviceStatusName = serviceStatusName;
        this.serviceStatusReason = serviceStatusReason;
    }

    public String getServiceStatusName() {
        return serviceStatusName;
    }

    public String getServiceStatusReason() {
        return serviceStatusReason;
    }
}
