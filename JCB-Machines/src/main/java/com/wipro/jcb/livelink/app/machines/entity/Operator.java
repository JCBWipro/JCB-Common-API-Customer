package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "operater")
@DynamicUpdate
public class Operator implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 7647358402835262772L;
    @Id
    @Column(name = "operatorid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String operatorName;
    private String phoneNumber;
    private String hours;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date workStart;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date workEnd;
    private Boolean jcbCertified;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public Operator(String s, String s1, String s2, Object o, Object o1, boolean b) {
    }
}

