package com.wipro.jcb.livelink.app.entity;

import com.wipro.jcb.livelink.app.enums.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:09-07-2024
 * project: JCB_NewRepo
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
public class Contact {

    @Id
    @Column(name = "Contact_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Enumerated(EnumType.STRING)
    private ROLE role;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String mobileNumber;
    @NonNull
    private String emailId;
    private String password;

}
