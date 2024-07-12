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
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int role_id;
    private String role_name;
    //private Set<ContactEntity> contact_list;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

   /* public RoleEntity(int role_id)
    {
        super.key = new Integer(role_id);
        RoleEntity r= (RoleEntity)read(this);

        setRole_id(r.role_id);
        setRole_name(r.role_name);
        setClient_id(r.client_id);
    }*/

    public RoleEntity(int role_id, String role_name, RoleEntity parent_role, ClientEntity client_id) {
        setRole_id(role_id);
        setRole_name(role_name);
        setClient_id(client_id);
    }

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public ClientEntity getClient_id() {
		return client_id;
	}

	public void setClient_id(ClientEntity client_id) {
		this.client_id = client_id;
	}
    
    

}

