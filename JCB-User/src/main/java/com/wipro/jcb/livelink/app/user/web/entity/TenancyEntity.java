package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


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
