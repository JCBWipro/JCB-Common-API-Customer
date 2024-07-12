package com.wipro.jcb.livelink.app.auth.entity;

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
@Table(name = "custom_asset_class")
public class CustomAssetClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Custom_Class_Id;
    
    @OneToOne(targetEntity = CustomAssetClassEntity.class)
    @JoinColumn(name = "Parent_Class_ID")
    private CustomAssetClassEntity Parent_Class_Id;
    
    private String Custom_class_Name;
    
    //private final Set<CustomAssetClassEntity> childCustomClass_list = new HashSet<CustomAssetClassEntity>();
    
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    
	public int getCustom_Class_Id() {
		return Custom_Class_Id;
	}
	public void setCustom_Class_Id(int custom_Class_Id) {
		Custom_Class_Id = custom_Class_Id;
	}
	public CustomAssetClassEntity getParent_Class_Id() {
		return Parent_Class_Id;
	}
	public void setParent_Class_Id(CustomAssetClassEntity parent_Class_Id) {
		Parent_Class_Id = parent_Class_Id;
	}
	public String getCustom_class_Name() {
		return Custom_class_Name;
	}
	public void setCustom_class_Name(String custom_class_Name) {
		Custom_class_Name = custom_class_Name;
	}
	public ClientEntity getClient_id() {
		return client_id;
	}
	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}
    
}
