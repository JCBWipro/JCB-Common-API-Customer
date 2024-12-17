package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.alerts.enums.TransitMode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 *
 */
@Entity
@DynamicUpdate
@Data
@Table(name = "machine", indexes = {
        @Index(name = "machineindexes", columnList = "location,platform,model,tag"),
        @Index(name = "vin_platform_indexes", columnList = "vin,platform") })
public class Machine implements Serializable {
    @Serial
    private static final long serialVersionUID = -1391422651545394702L;
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat(".##");

    private Boolean airFilterAlertStatus = false;
    private Boolean batteryChargeHighStatus = false;
    private Boolean batteryChargeLowStatus = false;
    private Boolean batteryChargingStatus = false;
    private Boolean batteryConnectedStatus = false;
    private Double batteryVoltage = 0.0;
    private Boolean connectivity = false;
    private Boolean coolantTemperatureAlertStatus = false;
    private Boolean engineOilPressureAlertStatus = false;
    private String engineStatus = "Off";
    private String firmwareVersion = "-";
    private Double fuelCapacity = 0.0;
    private Double fuelLevel = 0.0;
    private Boolean fuelLevelStatus = false;
    private String imeiNumber = "-";
    private String imsiNumber = "-";
    private String machineType = "-";

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date lastCommunicationTime;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    @Column(name = "location", columnDefinition = "text")
    private String location = "-";

    @Column(name = "model")
    private String model = "-";

    @Column(name = "platform")
    private String platform = "-";

    private Double serviceDueHours = 0.0;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;

    private Double totalMachineHours = 0.0;
    private TransitMode transitMode;
    private String zone = "-";

    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_username", referencedColumnName = "id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dealer_username", referencedColumnName = "id")
    private Dealer dealer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    private MachineFeedParserData machineFeedParserData;

    private String image = "";
    private String thumbnail = "";

    @Column(name = "tag")
    private String tag = "";

    private Operator operator;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "MACHIN_USER",
            joinColumns = @JoinColumn(name = "vin", referencedColumnName = "vin"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            indexes = @Index(name = "idx_user_id", columnList = "USER_ID"))
    private Set<User> users = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date serviceDoneDate;

    private Double serviceDoneHours = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date serviceDueDate;

    private Double serviceOverDueHours = 0.0;
    private String site = "";

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date renewalDate;

    private String startTime = "08:00 AM";
    private String endTime = "08:00 PM";

    private Double centerLat = 0.0;
    private Double centerLong = 0.0;
    private Long radius = 0L;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false, updatable = false)
    private Date createdAt;

    @JsonIgnore
    private String lastModified = null;

    @Column(columnDefinition = "boolean default true")
    private Boolean renewalFlag;

    private String customerNumber;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerLastName;
    private String customerFirstName;
    private String customerEmail;

    private String dealerId;
    private String dealerName;
    private String dealerAddress;
    private String dealerLastName;
    private String dealerFirstName;
    private String dealerNumber;
    private String dealerEmail;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date rolloffDate;

    @Column(name = "serviceType")
    private String serviceType = "-";

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "premiumStartDate")
    private Date premiumStartDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "premiumEndDate")
    private Date premiumEndDate;

    @Column(name = "premiumFlag")
    private String premiumFlag;

    @Column(columnDefinition = "boolean default false")
    private Boolean premiumFeature = false;

    private String createdBy;
}