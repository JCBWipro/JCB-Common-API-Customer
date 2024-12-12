package com.wipro.jcb.livelink.app.dataprocess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.dataprocess.commonUtils.FuelHistoryDataId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Entity
@DynamicUpdate
@IdClass(FuelHistoryDataId.class)
@Table(name = "machinefuelhistorydata", indexes = {
        @Index(name = "machinefuelhistorydata_vin_dateTime", columnList = "vin,dateTime", unique = false) })
public class MachineFuelHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 6261448896395354611L;

    @Setter
    @Getter
    @Column(name = "vin", updatable = false, insertable = false)
    private String vin;

    @Setter
    @Getter
    private Double fuelLevel;
    @Setter
    @Getter
    @Id
    @JsonIgnore
    private String vinId;

    @Setter
    @Getter
    @Id
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    @Override
    public String toString() {
        return "MachineFuelHistory [ vin=" + vin + ", fuelLevel=" + fuelLevel + ", vinId=" + vinId + ", dateTime="
                + dateTime + "]";
    }

}
