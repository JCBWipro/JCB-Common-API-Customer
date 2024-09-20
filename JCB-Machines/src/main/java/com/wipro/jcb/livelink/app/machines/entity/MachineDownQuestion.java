package com.wipro.jcb.livelink.app.machines.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "machine_down_question")
public class MachineDownQuestion implements Serializable {

    @Serial
    private static final long serialVersionUID = -5748214236509282789L;
    @Id
    private String identification;
    private String question;

}
