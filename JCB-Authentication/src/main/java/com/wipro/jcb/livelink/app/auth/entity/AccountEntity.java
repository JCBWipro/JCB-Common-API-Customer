package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int account_id;
    private String account_name, description, site_location, phone_no, mobile_no, emailId;
    private boolean status;
    private Long no_of_employees;
    @OneToOne(targetEntity = AccountEntity.class)
    @JoinColumn(name = "Parent_ID")
    private AccountEntity parent_account_id;
    private Date year_started;
    private int owner_id;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    private String accountCode;
    @OneToOne(targetEntity = AddressEntity.class)
    @JoinColumn(name = "Address_ID")
    private AddressEntity addressId;
    private String fax;
    private String timeZone;
    private String countryCode;
    private boolean MAFlag;

    private String mappingCode;
    private Timestamp createdOn;
    private Timestamp updatedOn;

    public AccountEntity() {
    }

    public AccountEntity getAccountEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccount_id(123);
        accountEntity.setAccount_name("Rituraj");
        accountEntity.setDescription("Descrption");
        accountEntity.setPhone_no("999999999");
        accountEntity.setMobile_no("9999999999");
        accountEntity.setEmailId("abc@gmail.com");
        accountEntity.setStatus(true);
        accountEntity.setAccountCode("123");
        accountEntity.setParent_account_id(new AccountEntity());
        accountEntity.setOwner_id(123);
        accountEntity.setAddressId(new AddressEntity());
        accountEntity.setFax("Fax");
        accountEntity.setClient_id(new ClientEntity());
        accountEntity.setMappingCode("mapping123");
        return accountEntity;
    }
    
}

