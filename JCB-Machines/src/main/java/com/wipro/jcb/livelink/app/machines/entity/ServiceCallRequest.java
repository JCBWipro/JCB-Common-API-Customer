package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/17/2024
 */
@Setter
@Getter
@Entity
@Table(name = "service_call_request")
public class ServiceCallRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String vin;
    private String customerName;
    private String customerPhone;
    private String customerAlternativePhone;
    private String contactName;
    private String machineHmr;
    private String serviceDealerName;
    private String model;
    private String machineLocation;
    private String warrantyStatus;
    private String contractStatus;
    private String machineStatus;
    private String remarks;
    private String images;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;


}
