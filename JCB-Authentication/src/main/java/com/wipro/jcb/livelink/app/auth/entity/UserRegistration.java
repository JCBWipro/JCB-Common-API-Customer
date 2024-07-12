package com.wipro.jcb.livelink.app.auth.entity;

import com.wipro.jcb.livelink.app.auth.enums.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:10-07-2024
 * project: JCB-New-Common-API
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_registration")
public class UserRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String emailId;
    private String password;

}
