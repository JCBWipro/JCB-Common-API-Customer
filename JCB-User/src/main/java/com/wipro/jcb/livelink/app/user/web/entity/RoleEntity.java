package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

    public RoleEntity(int role_id, String role_name, RoleEntity parent_role, ClientEntity client_id) {
        setRole_id(role_id);
        setRole_name(role_name);
        setClient_id(client_id);
    }

}

