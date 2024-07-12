package com.wipro.jcb.livelink.app.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "custom_asset_class")
public class CustomAssetClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Custom_Class_Id;
    
    @OneToOne(targetEntity = CustomAssetClassEntity.class)
    @JoinColumn(name = "Parent_Class_ID")
    private CustomAssetClassEntity Parent_Class_Id;
    
    private String Custom_class_Name;
    
    //private final Set<CustomAssetClassEntity> childCustomClass_list = new HashSet<CustomAssetClassEntity>();
    
    @OneToOne(targetEntity = ClientEntity.class)
    @JoinColumn(name = "Client_ID")
    private ClientEntity client_id;

}
