package com.wipro.jcb.livelink.app.alerts.enums;

import lombok.Getter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 *
 */
@Getter
public enum ServiceStatus {
    SERVICE_DUE_DATE("SERVICEDUE", "DATE"),
    SERVICE_DUE_HOUR("SERVICEDUE", "HMR"),
    SERVICE_OVERDUE_DATE("SERVICEOVERDUE", "DATE"),
    SERVICE_OVERDUE_HOUR("SERVICEOVERDUE", "HMR"),
    NORMAL("NORMAL", "NA");

    private final String serviceStatusName;
    private final String serviceStatusReason;

    ServiceStatus(String serviceStatusName, String serviceStatusReason) {
        this.serviceStatusName = serviceStatusName;
        this.serviceStatusReason = serviceStatusReason;
    }
}