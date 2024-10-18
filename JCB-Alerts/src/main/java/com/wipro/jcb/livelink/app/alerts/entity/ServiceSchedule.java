package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-09-2024
 *
 */
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "machine_service_schedule")
public class ServiceSchedule implements Serializable {


    @Serial
    private static final long serialVersionUID = 8098638795243197012L;

    @Getter
    @Id
    @Column(name = "vin",unique=true, nullable = false)
    private String vin;

    /*
     * optional date in miliseconds in UTC Example : 0
     */
    @Getter
    @Schema(description = "Service job ID", example = "10005692", required = true)
    @Column(name = "dealer_name")
    private String dealerName;

    /*
     * optional Example : "string" string
     */
    @Getter
    @Schema(description = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "service_name")
    private String serviceName;

    /*
     * optional Example : "string" string
     */
    @Getter
    @Schema(description = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "schedule_name")
    private String scheduleName;


    /*
     * optional Example : "string" string
     */
    @Getter
    @Schema(description = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "current_cmh")
    private String currentCmh;

    @Getter
    @Schema(description = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "status")
    private String status;

    /*
     * optional Example : "string" string
     */
    @Getter
    @Schema(description = "overDueHours", example = "Beyond warranty", required = true)
    @Column(name = "over_due_hours")
    private String overDueHours;


    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM yyyy", timezone = AppServerConstants.timezone)
    @Schema(description = "Over Due Date", example = "2017-09-09", required = true)
    @Column(name = "over_due_date")
    private Date overDueDate;

    @Getter
    @Schema(description = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "due_hours")
    private String dueHours;


    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM yyyy", timezone = AppServerConstants.timezone)
    @Schema(description = "Due date", example = "2017-09-09", required = true)
    @Column(name = "due_date")
    private Date dueDate;




    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;


    public Date getCreatedAt() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        // Use Madrid's time zone to format the date in
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        dateformat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            date = df.parse(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
