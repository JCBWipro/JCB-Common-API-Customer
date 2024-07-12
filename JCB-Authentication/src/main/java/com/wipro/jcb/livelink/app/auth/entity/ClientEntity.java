package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Data
@Entity
@Table(name = "clients")

public class ClientEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int client_id;
    private String client_name;
    @OneToOne(targetEntity = IndustryEntity.class)
    @JoinColumn(name = "Industy_ID")
    private IndustryEntity industry_id;
    
    public ClientEntity() {}

	public ClientEntity(int client_id, String client_name, IndustryEntity industry_id) {
		super();
		this.client_id = client_id;
		this.client_name = client_name;
		this.industry_id = industry_id;
	}
    
    
    
//    private Set<AccountEntity> account_list;
//    private Set<ContactEntity> contact_list;
//    private Set<RoleEntity> role_list;
//    private Set<TenancyEntity> tenancy_list;
//    private Set<CustomAssetGroupEntity> custom_asset_group_list;
//    private Set<AssetClassEntity> asset_class_list;
//    private Set<AssetTypeEntity> asset_type_list;
//    private Set<AssetGroupEntity> asset_group_list;
//    private Set<CustomAssetClassEntity> custom_asset_class_list;
    
}
