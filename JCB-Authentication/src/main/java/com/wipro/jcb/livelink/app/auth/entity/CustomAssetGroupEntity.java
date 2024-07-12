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
@Table(name = "custom_asset_group")
public class CustomAssetGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int group_id;

    private int level;

    private String group_name, parent_group_name;

    private String group_description;

    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

    @OneToOne(targetEntity = TenancyEntity.class)
    @JoinColumn(name = "Tenancy_ID")
    private TenancyEntity tenancy_id;

    private int active_status;
}
