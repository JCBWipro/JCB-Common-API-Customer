package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Getter
@Setter
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

    public ClientEntity() {
    }

    public ClientEntity(int client_id, String client_name, IndustryEntity industry_id) {
        super();
        this.client_id = client_id;
        this.client_name = client_name;
        this.industry_id = industry_id;
    }

}
