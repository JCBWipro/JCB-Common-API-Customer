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
@Table(name = "asset_group")
public class AssetGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asset_group_id;
    private String asset_group_name;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    
	public int getAsset_group_id() {
		return asset_group_id;
	}
	public void setAsset_group_id(int asset_group_id) {
		this.asset_group_id = asset_group_id;
	}
	public String getAsset_group_name() {
		return asset_group_name;
	}
	public void setAsset_group_name(String asset_group_name) {
		this.asset_group_name = asset_group_name;
	}
	public ClientEntity getClient_id() {
		return client_id;
	}
	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}

    /*public AssetGroupEntity(int asset_group_id )
    {
        Object key;
        key = new Integer(asset_group_id);
        AssetGroupEntity e = (AssetGroupEntity)read(this);
        setAsset_group_name(e.getAsset_group_name());
        setClient_id(e.getClient_id());
    }*/
    
    
}
