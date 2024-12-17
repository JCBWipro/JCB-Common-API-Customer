package com.wipro.jcb.livelink.app.dataprocess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MachineLocationHistoryUpdateData {

    private String vin;
    private Double latitude;
    private Double longitude;
    private Timestamp oldStatusAsOn;
    private Timestamp newStatusAsOn;
    private Date createdAt;
}
