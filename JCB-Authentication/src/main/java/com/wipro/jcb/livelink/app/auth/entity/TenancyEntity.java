package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

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
@Table(name = "tenancy")
public class TenancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tenancy_id;
    private String tenancy_name, parent_tenancy_name;
    @OneToOne(targetEntity = TenancyEntity.class)
    @JoinColumn(name = "Parent_Tenancy_ID")
    private TenancyEntity parent_tenancy_id;

//    private Set<TenancyEntity> child_tenancies = new HashSet<TenancyEntity>();
//    private Set<CustomAssetGroupEntity> asset_group_list;

    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

    @OneToOne(targetEntity = TenancyTypeEntity.class)
    @JoinColumn(name = "Tenancy_Type_ID")
    private TenancyTypeEntity tenancy_type_id;

    private Timestamp Operating_Start_Time;
    private Timestamp Operating_End_Time;
    private String createdBy;
    private Timestamp createdDate;
    private String tenancyCode;
    private String mappingCode;


}
