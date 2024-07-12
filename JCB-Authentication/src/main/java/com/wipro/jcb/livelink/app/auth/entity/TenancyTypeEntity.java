package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tenancy_type")
public class TenancyTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tenancy_type_id;
    private String tenancy_type_name;
    //private Set<TenancyEntity> tenancy_list;
    
    
	public int getTenancy_type_id() {
		return tenancy_type_id;
	}
	public void setTenancy_type_id(int tenancy_type_id) {
		this.tenancy_type_id = tenancy_type_id;
	}
	public String getTenancy_type_name() {
		return tenancy_type_name;
	}
	public void setTenancy_type_name(String tenancy_type_name) {
		this.tenancy_type_name = tenancy_type_name;
	}

   /* public TenancyTypeEntity(int tenancyTypeId)
    {
        Object key;
        key = new Integer(tenancyTypeId);
        TenancyTypeEntity tenancyTypeEntity = (TenancyTypeEntity)read(this);

        if(tenancyTypeEntity!= null)
        {
            setTenancy_type_id(tenancyTypeEntity.getTenancy_type_id());
            setTenancy_type_name(tenancyTypeEntity.getTenancy_type_name());
        }
    }*/
    
    
}
