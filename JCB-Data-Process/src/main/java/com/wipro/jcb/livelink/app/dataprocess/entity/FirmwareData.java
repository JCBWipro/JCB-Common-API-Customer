package com.wipro.jcb.livelink.app.dataprocess.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.dataprocess.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Setter
@Getter
@Table
@Entity(name="firmware_data")
public class FirmwareData implements Serializable {

    @Serial
    private static final long serialVersionUID = -1391423561597534702L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "day")
    private Date day;

    @Column(name = "firmware")
    private String firmware;

    @Column(name = "count")
    private long count= 0;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="timestamp default current_timestamp")
    private Date last_updated_at;

}
