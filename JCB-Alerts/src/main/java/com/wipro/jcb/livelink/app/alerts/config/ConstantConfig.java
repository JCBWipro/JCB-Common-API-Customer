package com.wipro.jcb.livelink.app.alerts.config;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:20-11-2024
 */
public class ConstantConfig {
    public static final int REQUEST_TIMEOUT =500;

    /* case to be evaluated during execution sendnotification */
    public static final String TIME_FENCE_ALERT_CASE = "Time_Fence_Alert";
    public static final String GEO_FENCE_ALERT_CASE = "Geo_Fence_Alert";
    /* Event description for TimeFence and GeoFence alert */
    public static final String TIME_FENCE_EVENT_DESCRIPTION = "Machine is used outside Operating Hours";
    public static final String GEO_FENCE_EVENT_DESCRIPTION = "Machine is deviated from specified range";
}
