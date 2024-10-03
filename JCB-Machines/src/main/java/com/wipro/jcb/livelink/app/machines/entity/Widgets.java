package com.wipro.jcb.livelink.app.machines.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="widgets")
public class Widgets implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "machineType")
    private String machine_type;

    @Column(name = "value")
    private String value;
}
