package com.wipro.jcb.livelink.app.mob.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:29-07-2024
 * project: JCB-Common-API-Customer
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotUsernameRequest {
    private String mobileNumber;
    private String emailId;
}