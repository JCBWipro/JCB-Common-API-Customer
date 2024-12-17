package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-10-2024
 */
@Setter
@Getter
@Entity
@Table(name = "machine_live_location", indexes = {
        @Index(name = "machine_live_location_vin_link", columnList = "vin,uniqueId", unique = true) })
public class MachineLiveLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1293838L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty(value = "userId", example = "test544454", required = true)
    @JsonProperty("userId")
    private String userId;

    @Column(name = "vin", nullable = false)
    private String vin;

    @JsonProperty("link")
    private String link;

    private Integer hitCount;

    @JsonProperty("slot")
    private String slot;

    @JsonProperty("uniqueId")
    private String uniqueId;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("expiryTime")
    private Date expiryTime;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @JsonProperty("day")
    private Date day;

    @JsonProperty("status")
    private Boolean status;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="timestamp default current_timestamp", insertable=false, updatable=false)
    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("updatedAt")
    private Date updatedAt;

}
