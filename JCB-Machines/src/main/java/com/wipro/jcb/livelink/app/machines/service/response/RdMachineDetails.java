package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RdMachineDetails {
    private String vin;
    private String imei;
    private String mobileNumber;
    private String customerName;
    private String dealerName;
    private String dealerNumber;
    private Double latitude;
    private Double longitude;
    private String address;
}
