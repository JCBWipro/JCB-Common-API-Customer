package com.wipro.jcb.livelink.app.auth.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
public class ContactEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String contact_id;
    @Column(name="Password")
    private String password;
    private boolean active_status;
    private String department, primary_email_id, secondary_email_id;
    @Column(name="First_Name")
    private String first_name;
    private String last_name, primary_phone_number, secondary_phone_number, primary_mobile_number, secondary_mobile_number;
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
    private String country, dob, nativeState, newPassword;
    private String countryCode, language, timezone;
    private int sysGeneratedPassword;
    private Timestamp LastUpdatedTime;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
}
