package com.wipro.jcb.livelink.app.auth.entity;


import jakarta.persistence.*;
import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asset_class")
public class AssetClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Asset_Class_ID")
    private int AssetClassId;
    @Column(name = "Asset_Class_Name")
    private String AssetClassName;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

}
