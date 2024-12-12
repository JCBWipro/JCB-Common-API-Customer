package com.wipro.jcb.livelink.app.dataprocess.constants;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
public final class Constant {
    /* Constant used in reading time and Geo fencing query result map */
    public static final String VIN = "vin";
    public static final String USER_ID = "user_id";
    public static final String RADIUS = "radius";
    public static final String DISTANCE = "distance";

    public static final String DateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String timezone = "IST";
    /*
     * Constant used in query generation (in class MachineDAO ) Time and Geo fencing
     */
    public static final String ID = "ids";
    public static final String TODAY = "today";
    /* case to be evaluated during execution sendnotification */
    public static final String TIME_FENCE_ALERT_CASE = "Time_Fence_Alert";
    public static final String GEO_FENCE_ALERT_CASE = "Geo_Fence_Alert";
    /* Event description for TimeFence and GeoFence alert */
    public static final String TIME_FENCE_EVENT_DESCRIPTION = "Machine is used outside Operating Hours";
    public static final String GEO_FENCE_EVENT_DESCRIPTION = "Machine is deviated from specified range";

    public static final String MESSAGE_SEPARATOR = "\\|inputPacketString:";

}
