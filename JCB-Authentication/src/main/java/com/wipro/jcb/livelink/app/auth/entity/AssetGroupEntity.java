package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.*;
import lombok.*;


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
@Table(name = "asset_group")
public class AssetGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asset_group_id;
    private String asset_group_name;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

    /*public AssetGroupEntity(int asset_group_id )
    {
        Object key;
        key = new Integer(asset_group_id);
        AssetGroupEntity e = (AssetGroupEntity)read(this);
        setAsset_group_name(e.getAsset_group_name());
        setClient_id(e.getClient_id());
    }*/
    
    
}
