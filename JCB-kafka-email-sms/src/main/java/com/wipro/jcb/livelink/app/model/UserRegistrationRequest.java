package com.wipro.jcb.livelink.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:26-06-2024
 * project: JCB_NewRepo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    private String role;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String emailId;

}
