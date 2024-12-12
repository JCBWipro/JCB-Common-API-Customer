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
public class MachineLocation {
    private String vin;
    private Timestamp statusAsOn;
    private Double latitude;
    private Double longitude;
}
