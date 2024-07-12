package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
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
@Table(name = "asset_type")
public class AssetTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asset_type_id;
    private String asset_type_name;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    private String assetTypeCode;
    private String assetImage;
    private int asset_group_id;
    private String asset_type_group_name;
    private String Asset_Type_Master_Code;
    
	public int getAsset_type_id() {
		return asset_type_id;
	}
	public void setAsset_type_id(int asset_type_id) {
		this.asset_type_id = asset_type_id;
	}
	public String getAsset_type_name() {
		return asset_type_name;
	}
	public void setAsset_type_name(String asset_type_name) {
		this.asset_type_name = asset_type_name;
	}
	public ClientEntity getClient_id() {
		return client_id;
	}
	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}
	public String getAssetTypeCode() {
		return assetTypeCode;
	}
	public void setAssetTypeCode(String assetTypeCode) {
		this.assetTypeCode = assetTypeCode;
	}
	public String getAssetImage() {
		return assetImage;
	}
	public void setAssetImage(String assetImage) {
		this.assetImage = assetImage;
	}
	public int getAsset_group_id() {
		return asset_group_id;
	}
	public void setAsset_group_id(int asset_group_id) {
		this.asset_group_id = asset_group_id;
	}
	public String getAsset_type_group_name() {
		return asset_type_group_name;
	}
	public void setAsset_type_group_name(String asset_type_group_name) {
		this.asset_type_group_name = asset_type_group_name;
	}
	public String getAsset_Type_Master_Code() {
		return Asset_Type_Master_Code;
	}
	public void setAsset_Type_Master_Code(String asset_Type_Master_Code) {
		Asset_Type_Master_Code = asset_Type_Master_Code;
	}

    /*public AssetTypeEntity(int asset_type_id )
    {
        Object key;
        key = new Integer(asset_type_id);
        AssetTypeEntity e = (AssetTypeEntity)read(this);
        setAsset_type_name(e.getAsset_type_name());
        setClient_id(e.getClient_id());
    }*/
    
    
}
