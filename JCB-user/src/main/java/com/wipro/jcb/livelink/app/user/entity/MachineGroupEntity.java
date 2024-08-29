package com.wipro.jcb.livelink.app.user.entity;

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
@Table(name = "machine_group_dimension")
public class MachineGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Machine_Group_ID")
    private int machineGroupId;
    @Column(name = "Machine_Group_Name")
    private String machineGroupName;
    @Column(name = "Level")
    private  int level;
    @Column(name = "Parent_ID")
    private int parentId;
    @Column(name = "Contact_ID")
    private String contactId;
    @Column(name = "Tenancy_ID")
    private int tenancyId;

}
