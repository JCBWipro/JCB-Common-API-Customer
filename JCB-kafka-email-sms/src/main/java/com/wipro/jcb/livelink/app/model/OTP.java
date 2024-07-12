package com.wipro.jcb.livelink.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:10-07-2024
 * project: JCB_NewRepo
 */
@Data
@AllArgsConstructor
public class OTP {

    private String mobileNumber;
    private String otp;

}
