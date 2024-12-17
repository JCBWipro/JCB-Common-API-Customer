package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 */
@Entity
@DynamicUpdate
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "machine_feedparser_location_data", indexes = {
        @Index(name = "location_data_indexes_vin", columnList = "vin,lastModifiedDate"),
        @Index(name = "location_data_indexes_statusAsOnTime", columnList = "statusAsOnTime")})
public class MachineFeedLocation implements Serializable {
    @Serial
    private static final long serialVersionUID = -407814975667341910L;

    @Getter
    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;

    @Getter
    private Double latitude = 0.0;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

    @Setter
    @Getter
    @OneToOne
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    @JsonIgnore
    private MachineAddress machineAddress;
    /*
     * Latitude part of machine GPS co-ordinates Example : 0.0
     */
    @Getter
    private Double longitude = 0.0;


    public MachineFeedLocation(String vin, Date creationDate, MachineAddress machineAddress, Date statusAsOnTime) {
        super();
        this.vin = vin;
        this.creationDate = creationDate;
        this.machineAddress = machineAddress;
        this.statusAsOnTime = statusAsOnTime;
    }
}
