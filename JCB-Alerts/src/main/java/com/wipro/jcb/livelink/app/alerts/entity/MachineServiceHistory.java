package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-09-2024
 *
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@ToString
@Table(name = "machine_service_history", indexes = {
        @Index(name = "machine_service_history_vin_jobCardNumber", columnList = "vin,job_card_number", unique = false) })
public class MachineServiceHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 8098638795243167012L;

    /*
     * optional date in miliseconds in UTC Example : 0
     */
    @Id
    @Column(name = "job_card_number")
    private String jobCardNumber;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    @Column(name = "service_done_at")
    private Date serviceDoneAt;

    /*
     * optional Example : "string" string
     */
    @Column(name = "service_done")
    private String serviceDone;

    /*
     * optional Example : "string" string
     */
    @Column(name = "comments")
    private String comments;

    @JsonIgnore
    @Column(name = "vin")
    private String vin;

    /*
     * optional Example : "string" string
     */

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at",columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

}

