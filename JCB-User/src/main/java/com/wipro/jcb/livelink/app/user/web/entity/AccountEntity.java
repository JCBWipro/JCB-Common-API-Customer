package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_ID")
    private int account_id;
    @Column(name = "Account_Name")
    private String account_name;
    @Column(name = "Description")
    private String description;
    @Column(name = "Site")
    private String site_location;
    @Column(name = "Phone")
    private String phone_no;
    @Column(name = "Mobile")
    private String mobile_no;
    @Column(name = "Email_ID")
    private String emailId;
    @Column(name = "Status")
    private boolean status;
    @Column(name = "Number_of_employees")
    private Long no_of_employees;
    @OneToOne(targetEntity = AccountEntity.class)
    @JoinColumn(name = "Parent_ID")
    private AccountEntity parent_account_id;
    @Column(name = "Year_Started")
    private Date year_started;
    @Column(name = "Owner_ID")
    private int owner_id;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    @Column(name = "Account_Code")
    private String accountCode;
    @OneToOne(targetEntity = AddressEntity.class)
    @JoinColumn(name = "Address_ID")
    private AddressEntity addressId;
    @Column(name = "Fax")
    private String fax;
    @Column(name = "timeZone")
    private String timeZone;
    @Column(name = "CountryCode")
    private String countryCode;
    @Column(name = "MAFlag")
    private boolean MAFlag;
    @Column(name = "mapping_code")
    private String mappingCode;
    @Column(name = "created_on")
    private Timestamp createdOn;
    @Column(name = "last_updated_on")
    private Timestamp updatedOn;

    public AccountEntity() {
    }

}

