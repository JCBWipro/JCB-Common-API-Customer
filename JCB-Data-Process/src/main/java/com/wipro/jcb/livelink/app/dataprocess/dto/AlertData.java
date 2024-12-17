package com.wipro.jcb.livelink.app.dataprocess.dto;

import lombok.*;

import java.sql.Timestamp;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertData {

    private String vin;
    private Double latitude;
    private Double longitude;
    private Boolean isOpen;
    private String alertId;
    private Timestamp statusAsOnTime;

    public AlertData(String vin,  String alertId) {
        super();
        this.vin = vin;
        this.alertId = alertId;

    }
}
