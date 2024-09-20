package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Entity
@DynamicUpdate
@Table(name = "machine_address", indexes = {
        @Index(name = "machine_address_indexes", columnList = "vin")})
public class MachineAddress implements Serializable {
    @Serial
    private static final long serialVersionUID = 4468969413651265641L;
    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;

    @Column(columnDefinition="text")
    private String location = "-";

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;

    public MachineAddress(String vin, Date statusAsOnTime, String location, Date creationDate,
                          Date lastModifiedDate) {
        super();
        this.vin = vin;
        this.statusAsOnTime = statusAsOnTime;
        this.location = location;
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
    }
    public MachineAddress(String vin, Date statusAsOnTime,  Date creationDate) {
        super();
        this.vin = vin;
        this.creationDate = creationDate;
        this.statusAsOnTime = statusAsOnTime;
    }

    public MachineAddress() {
        super();
    }

    public MachineAddress(String vin, Date statusAsOnTime, String location,
                          Date lastModifiedDate) {
        super();
        this.vin = vin;
        this.statusAsOnTime = statusAsOnTime;
        this.location = location;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "MachineAddress [vin=" + vin + ", statusAsOnTime=" + statusAsOnTime + ", location=" + location
                + ", creationDate=" + creationDate + ", lastModifiedDate=" + lastModifiedDate + "]";
    }



}
