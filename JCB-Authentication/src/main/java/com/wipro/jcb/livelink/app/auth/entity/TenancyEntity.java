package com.wipro.jcb.livelink.app.auth.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tenancy")
public class TenancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tenancy_id;
    private String tenancy_name,parent_tenancy_name;
    @OneToOne(targetEntity = TenancyEntity.class)
    @JoinColumn(name = "Parent_Tenancy_ID")
    private TenancyEntity parent_tenancy_id;
    
//    private Set<TenancyEntity> child_tenancies = new HashSet<TenancyEntity>();
//    private Set<CustomAssetGroupEntity> asset_group_list;
    
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    
    @OneToOne(targetEntity = TenancyTypeEntity.class)
    @JoinColumn(name = "Tenancy_Type_ID")
    private TenancyTypeEntity tenancy_type_id;
    
    private Timestamp Operating_Start_Time;
    private Timestamp Operating_End_Time;
    private String createdBy;
    private Timestamp createdDate;
    private String tenancyCode;
    private String mappingCode;

	public int getTenancy_id() {
		return tenancy_id;
	}

	public void setTenancy_id(int tenancy_id) {
		this.tenancy_id = tenancy_id;
	}

	public String getTenancy_name() {
		return tenancy_name;
	}

	public void setTenancy_name(String tenancy_name) {
		this.tenancy_name = tenancy_name;
	}

	public String getParent_tenancy_name() {
		return parent_tenancy_name;
	}

	public void setParent_tenancy_name(String parent_tenancy_name) {
		this.parent_tenancy_name = parent_tenancy_name;
	}

	public TenancyEntity getParent_tenancy_id() {
		return parent_tenancy_id;
	}

	public void setParent_tenancy_id(TenancyEntity parent_tenancy_id) {
		this.parent_tenancy_id = parent_tenancy_id;
	}

	public ClientEntity getClient_id() {
		return client_id;
	}

	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}

	public TenancyTypeEntity getTenancy_type_id() {
		return tenancy_type_id;
	}

	public void setTenancy_type_id(TenancyTypeEntity tenancy_type_id) {
		this.tenancy_type_id = tenancy_type_id;
	}

	public Timestamp getOperating_Start_Time() {
		return Operating_Start_Time;
	}

	public void setOperating_Start_Time(Timestamp operating_Start_Time) {
		Operating_Start_Time = operating_Start_Time;
	}

	public Timestamp getOperating_End_Time() {
		return Operating_End_Time;
	}

	public void setOperating_End_Time(Timestamp operating_End_Time) {
		Operating_End_Time = operating_End_Time;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getTenancyCode() {
		return tenancyCode;
	}

	public void setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
	}

	public String getMappingCode() {
		return mappingCode;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}
    
    


}
