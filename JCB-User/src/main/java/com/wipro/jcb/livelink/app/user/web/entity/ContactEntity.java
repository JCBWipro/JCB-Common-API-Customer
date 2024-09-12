package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
public class ContactEntity {

    @Id
    @Column(name = "Contact_ID")
    private String contactId;
    @Column(name = "Password")
    private String password;
    @Column(name = "Status")
    private boolean active_status;
    @Column(name = "Department")
    private String department;
    @Column(name = "Primary_Email_ID")
    private String primary_email_id;
    @Column(name = "Secondary_Email_ID")
    private String secondary_email_id;
    @Column(name = "First_Name")
    private String first_name;
    @Column(name = "Last_Name")
    private String last_name;
    @Column(name = "Primary_Phone_Number")
    private String primary_phone_number;
    @Column(name = "Secondary_Phone_Number")
    private String secondary_phone_number;
    @Column(name = "Primary_Moblie_Number")
    private String primary_mobile_number;
    @Column(name = "Secondary_Mobile_Number")
    private String secondary_mobile_number;
    @OneToOne(targetEntity = RoleEntity.class)
    @JoinColumn(name = "Role_ID")
    private RoleEntity role;
    @OneToOne(targetEntity = ContactEntity.class)
    @JoinColumn(name = "Manager_ID")
    private ContactEntity manager_id;
    private int is_tenancy_admin;
    @OneToOne(targetEntity = AddressEntity.class)
    @JoinColumn(name = "Address_ID")
    private AddressEntity Address_ID;
    @Column(name = "countrycode")
    private String countrycode;
    @Column(name = "Language")
    private String language;
    @Column(name = "TimeZone")
    private String timezone;
    @Column(name = "sys_gen_password")
    private int sysGeneratedPassword;
    @Column(name = "lockedOutTime")
    private Timestamp lockedOutTime;
    @Column(name = "LastUpdatedTime")
    private Timestamp LastUpdatedTime;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    @Column(name = "reset_pass_count")
    private int resetPassCount;
}
