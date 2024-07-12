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
@Table(name = "asset_type")
public class AssetTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asset_type_id;
    private String asset_type_name;
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;
    private String assetTypeCode;
    private String assetImage;
    private int asset_group_id;
    private String asset_type_group_name;
    private String Asset_Type_Master_Code;

    /*public AssetTypeEntity(int asset_type_id )
    {
        Object key;
        key = new Integer(asset_type_id);
        AssetTypeEntity e = (AssetTypeEntity)read(this);
        setAsset_type_name(e.getAsset_type_name());
        setClient_id(e.getClient_id());
    }*/
    
    
}
