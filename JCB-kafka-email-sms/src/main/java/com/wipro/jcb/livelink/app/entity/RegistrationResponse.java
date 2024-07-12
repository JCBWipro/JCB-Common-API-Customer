package com.wipro.jcb.livelink.app.entity;

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
public class RegistrationResponse {

    private boolean success;
    private String otp;
    private StringBuffer error;

    public RegistrationResponse() {
    }
}
