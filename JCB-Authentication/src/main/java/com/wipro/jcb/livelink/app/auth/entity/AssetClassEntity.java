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
@Table(name = "asset_class")
public class AssetClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AssetClassId;
    private String AssetClassName;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    
	public int getAssetClassId() {
		return AssetClassId;
	}
	public void setAssetClassId(int assetClassId) {
		AssetClassId = assetClassId;
	}
	public String getAssetClassName() {
		return AssetClassName;
	}
	public void setAssetClassName(String assetClassName) {
		AssetClassName = assetClassName;
	}
	public ClientEntity getClient_id() {
		return client_id;
	}
	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}
    
    

    /*public AssetClassEntity(int AssetClassId )
    {
        Object key;
        key = new Integer(AssetClassId);
        AssetClassEntity e = (AssetClassEntity)read(this);
        setAssetClassName(e.getAssetClassName());
        setClient_id(e.getClient_id());
    }*/
}
