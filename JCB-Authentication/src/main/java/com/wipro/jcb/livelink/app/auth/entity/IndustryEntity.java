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
@Setter
@Getter
@Entity
@Table(name = "industry")
public class IndustryEntity {
    //private Set<ClientEntity> client_list;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int industry_id;
    private String industry_name;

}
