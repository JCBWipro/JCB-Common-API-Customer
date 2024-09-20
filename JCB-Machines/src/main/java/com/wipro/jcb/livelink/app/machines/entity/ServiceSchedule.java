package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Setter;

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
 * project: JCB-Common-API-Customer
 */
@Setter
@Entity
@Table(name = "machine_service_schedule")
public class ServiceSchedule implements Serializable {


    private static final long serialVersionUID = 8098638795243197012L;

    @Id
    @Column(name = "vin",unique=true, nullable = false)
    private String vin;

    /*
     * optional date in miliseconds in UTC Example : 0
     */
    @ApiModelProperty(value = "Service job ID", example = "10005692", required = true)
    @Column(name = "dealer_name")
    private String dealerName;

    /*
     * optional Example : "string" string
     */
    @ApiModelProperty(value = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "service_name")
    private String serviceName;

    /*
     * optional Example : "string" string
     */
    @ApiModelProperty(value = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "schedule_name")
    private String scheduleName;


    /*
     * optional Example : "string" string
     */
    @ApiModelProperty(value = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "current_cmh")
    private String currentCmh;

    @ApiModelProperty(value = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "status")
    private String status;

    /*
     * optional Example : "string" string
     */
    @ApiModelProperty(value = "overDueHours", example = "Beyond warranty", required = true)
    @Column(name = "over_due_hours")
    private String overDueHours;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM yyyy", timezone = AppServerConstants.timezone)
    @ApiModelProperty(value = "Over Due Date", example = "2017-09-09", required = true)
    @Column(name = "over_due_date")
    private Date overDueDate;

    @ApiModelProperty(value = "Name of service", example = "Beyond warranty", required = true)
    @Column(name = "due_hours")
    private String dueHours;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM yyyy", timezone = AppServerConstants.timezone)
    @ApiModelProperty(value = "Due date", example = "2017-09-09", required = true)
    @Column(name = "due_date")
    private Date dueDate;




    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;



    public String getDealerName() {
        return dealerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getScheduleName() {
        return scheduleName;
    }


    public String getOverDueHours() {
        return overDueHours;
    }





    public Date getOverDueDate() {
        return overDueDate;
    }





    public String getDueHours() {
        return dueHours;
    }





    public Date getDueDate() {
        return dueDate;
    }


    public String getCurrentCmh() {
        return currentCmh;
    }

    public String getStatus() {
        return status;
    }


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

    public String getVin() {
        return vin;
    }

    public ServiceSchedule(String vin, String dealerName, String serviceName, String scheduleName, String currentCmh,
                           String status, String overDueHours, Date overDueDate, String dueHours, Date dueDate, Date createdAt) {
        super();
        this.vin = vin;
        this.dealerName = dealerName;
        this.serviceName = serviceName;
        this.scheduleName = scheduleName;
        this.currentCmh = currentCmh;
        this.status = status;
        this.overDueHours = overDueHours;
        this.overDueDate = overDueDate;
        this.dueHours = dueHours;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public ServiceSchedule() {
        super();
    }






}
