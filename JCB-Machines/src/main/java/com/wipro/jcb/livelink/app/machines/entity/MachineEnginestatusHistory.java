package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.EngineStatusHistoryDataId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
@ToString
@Entity
@DynamicUpdate
@IdClass(EngineStatusHistoryDataId.class)
@Table(name = "machineenginestatushistorydata", indexes = {
        @Index(name = "machineenginestatushistorydata_vin_dateTime", columnList = "vin,dateTime", unique = false) })
public class MachineEnginestatusHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = -2699233137907509189L;

    @Id
    @JsonIgnore
    private String vinId;

    @Column(name = "vin", updatable = false, insertable = true)
    private String vin;

    private Boolean isEngineOn;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Generated Time", example = "2017-07-13 12:44:20", required = true)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date dateTime;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

}
