package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Setter
@Getter
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

}

