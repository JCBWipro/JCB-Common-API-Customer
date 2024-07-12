package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Data
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

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSite_location() {
		return site_location;
	}

	public void setSite_location(String site_location) {
		this.site_location = site_location;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getNo_of_employees() {
		return no_of_employees;
	}

	public void setNo_of_employees(Long no_of_employees) {
		this.no_of_employees = no_of_employees;
	}

	public AccountEntity getParent_account_id() {
		return parent_account_id;
	}

	public void setParent_account_id(AccountEntity parent_account_id) {
		this.parent_account_id = parent_account_id;
	}

	public Date getYear_started() {
		return year_started;
	}

	public void setYear_started(Date year_started) {
		this.year_started = year_started;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public ClientEntity getClient_id() {
		return client_id;
	}

	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public AddressEntity getAddressId() {
		return addressId;
	}

	public void setAddressId(AddressEntity addressId) {
		this.addressId = addressId;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isMAFlag() {
		return MAFlag;
	}

	public void setMAFlag(boolean mAFlag) {
		MAFlag = mAFlag;
	}

	public String getMappingCode() {
		return mappingCode;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
    
    
}

