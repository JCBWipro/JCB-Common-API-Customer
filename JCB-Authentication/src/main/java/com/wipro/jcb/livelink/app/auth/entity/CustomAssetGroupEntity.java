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
@Table(name = "custom_asset_group")
public class CustomAssetGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int group_id;
    
    private int level;
    
    private String group_name,parent_group_name;
    
    private String group_description;
    
//    @OneToOne(targetEntity = CustomAssetGroupEntity.class)
//    @JoinColumn(name = "Group_Description")
//    private CustomAssetGroupEntity asset_group_type;
    
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    
    @OneToOne(targetEntity = TenancyEntity.class)
    @JoinColumn(name = "Tenancy_ID")
    private TenancyEntity tenancy_id;
    
    private int active_status;
    
    
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getParent_group_name() {
		return parent_group_name;
	}
	public void setParent_group_name(String parent_group_name) {
		this.parent_group_name = parent_group_name;
	}
	public String getGroup_description() {
		return group_description;
	}
	public void setGroup_description(String group_description) {
		this.group_description = group_description;
	}
	public ClientEntity getClient_id() {
		return client_id;
	}
	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}
	public TenancyEntity getTenancy_id() {
		return tenancy_id;
	}
	public void setTenancy_id(TenancyEntity tenancy_id) {
		this.tenancy_id = tenancy_id;
	}
	public int getActive_status() {
		return active_status;
	}
	public void setActive_status(int active_status) {
		this.active_status = active_status;
	}

   /* public CustomAssetGroupEntity(int group_id){
        Object key;
        key = new Integer(group_id);
        CustomAssetGroupEntity customAssetGroupEntity=(CustomAssetGroupEntity) *//*read()*//*;    ;

        if(customAssetGroupEntity!=null)
        {
            setGroup_id(customAssetGroupEntity.group_id);
            setLevel(customAssetGroupEntity.level);
            setGroup_name(customAssetGroupEntity.group_name);
            setGroup_description(customAssetGroupEntity.group_description);
            setParent_group_name(customAssetGroupEntity.parent_group_name);
            setAsset_group_type(customAssetGroupEntity.asset_group_type);
            setClient_id(customAssetGroupEntity.client_id);
            setTenancy_id(customAssetGroupEntity.tenancy_id);
            setActive_status(customAssetGroupEntity.active_status);
        }

    }*/
    
    
}
