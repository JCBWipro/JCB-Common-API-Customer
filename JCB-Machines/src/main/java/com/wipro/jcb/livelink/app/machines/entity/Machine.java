package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
@Entity
@DynamicUpdate
@Table(name = "machine", indexes = {
        @Index(name = "machineindexes", columnList = "location,platform,model,tag"),
        @Index(name = "vin_platform_indexes", columnList = "vin,platform") })
public class Machine implements Serializable {
    @Serial
    private static final long serialVersionUID = -1391422651545394702L;
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat(".##");
    private Boolean airFilterAlertStatus = false;
    /*
     * Flag to indicate if air filter is blocked or not Example : true
     */
    private Boolean batteryChargeHighStatus = false;
    /*
     * Indicates Battery high charge status of machine Example : true
     */
    private Boolean batteryChargeLowStatus = false;
    /*
     * Indicates Battery low charge status of machine Example : true
     */
    private Boolean batteryChargingStatus = false;
    /*
     * Indicates Battery charging status of machine Example : true
     */
    private Boolean batteryConnectedStatus = false;
    /*
     * Indicates Battery connected status of machine Example : true
     */
    private Double batteryVoltage = 0.0;
    /*
     * Battery voltage output Example : 0.0
     */
    private Boolean connectivity = false;
    /*
     * Indicates if machine is connected to livelink or not for this moment Example
     * : true
     */
    private Boolean coolantTemperatureAlertStatus = false;
    /*
     * Indicates if machine is connected to livelink or not for this moment Example
     * : true
     */
    private Boolean engineOilPressureAlertStatus = false;
    /*
     * Flag to indicate oil pressure alert Example : true
     */
    private String engineStatus = "Off";
    /*
     * Engine on/off status Example : "string"
     */
    private String firmwareVersion = "-";
    /*
     * Example : "string"
     */
    private Double fuelCapacity = 0.0;
    /*
     * Fuel capacity Example : 0.0
     */
    private Double fuelLevel = 0.0;
    /*
     * Fuel level Example : 0.0
     */
    private Boolean fuelLevelStatus = false;
    /*
     * Flag to indicate fuel level status alert Example : true
     */
    private String imeiNumber = "-";
    /*
     * Example : "string"
     */
    private String imsiNumber = "-";
    /*
     * Example : "string"
     */
    private String machineType = "-";
    /*
     * Example : "string"
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date lastCommunicationTime;
    /*
     * Datetime when machine has last comminicated to livelinkserver. Date in
     * miliseconds in UTCExample : 0
     */
    private Double latitude = 0.0;
    /*
     * Latitude part of machine GPS co-ordinates Example : 0.0
     */
    private Double longitude = 0.0;
    /*
     * Longitude part of machine GPS co-ordinates Example : 0.0
     */
    @Column(name = "location",columnDefinition="text")
    private String location = "-";
    /*
     * GPS address in words Example : "string"
     */
    @Column(name = "model")
    private String model = "-";
    /*
     * Model of machine e.g 2DX, 3DX Example : "string"
     */
    @Column(name = "platform")
    private String platform = "-";
    /*
     * Type of platform. e.g BHL, Compactors Example : "string"
     */
    private Double serviceDueHours = 0.0;
    /*
     * Service due/overdue hours. Negative number will indicate overdue hours
     * Example : 0.0
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppServerConstants.DateTimeFormat, timezone = AppServerConstants.timezone)
    private Date statusAsOnTime;
    /*
     * Datetime when machine has last reported on to livelinkserver. Date in
     * miliseconds in UTCExample : 0
     */
    private Double totalMachineHours = 0.0;
    /*
     * Total Machine hours Example : 0.0
     */
    private TransitMode transitMode;
    /*
     * Example : "string"
     */
    private String zone = "-";
    /*
     * Zone of the machine Example : "string"
     */
    /*
     * @GeneratedValue(generator="system-uuid")
     *
     * @GenericGenerator(name="system-uuid", strategy = "uuid")
     */
    @Id
    @Column(name = "vin", unique = true, nullable = false)
    private String vin;
    /*
     * This string represents unique id associated with machine. Example : "string"
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_username", referencedColumnName = "id")
    private Customer customer;
    /*
     * Example : Customer
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dealer_username", referencedColumnName = "id")
    private Dealer dealer;
    /*
     * Example : Dealer
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vin", referencedColumnName = "vin")
    private MachineFeedParserData machineFeedParserData;

    private String image = "";
    private String thumbnail = "";
    @Column(name = "tag")
    private String tag = "";
    private Operator operator;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "MACHIN_USER", joinColumns = {
            @JoinColumn(name = "vin", referencedColumnName = "vin") }, inverseJoinColumns = {
            @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID") },indexes = {
            @Index(name = "idx_user_id", columnList = "USER_ID")
    })
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
    // centerLatitude
    private Double centerLat = 0.0;
    // centerLongitude
    private Double centerLong = 0.0;
    private Long radius = 0L;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    Date createdAt;

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
    private Boolean premiumFeature=false;

    private String createdBy;

    public Machine() {
    }

    public Date getLastModified() {
        if(lastModified != null) {
            try {
                return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(lastModified);
            } catch(ParseException ignored) {}
        }
        return null;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getAirFilterAlertStatus() {
        return airFilterAlertStatus;
    }

    public void setAirFilterAlertStatus(Boolean airFilterAlertStatus) {
        this.airFilterAlertStatus = airFilterAlertStatus;
    }

    public Boolean getBatteryChargeHighStatus() {
        return batteryChargeHighStatus;
    }

    public void setBatteryChargeHighStatus(Boolean batteryChargeHighStatus) {
        this.batteryChargeHighStatus = batteryChargeHighStatus;
    }

    public Boolean getBatteryChargeLowStatus() {
        return batteryChargeLowStatus;
    }

    public void setBatteryChargeLowStatus(Boolean batteryChargeLowStatus) {
        this.batteryChargeLowStatus = batteryChargeLowStatus;
    }

    public Boolean getBatteryChargingStatus() {
        return batteryChargingStatus;
    }

    public void setBatteryChargingStatus(Boolean batteryChargingStatus) {
        this.batteryChargingStatus = batteryChargingStatus;
    }

    public Boolean getBatteryConnectedStatus() {
        return batteryConnectedStatus;
    }

    public void setBatteryConnectedStatus(Boolean batteryConnectedStatus) {
        this.batteryConnectedStatus = batteryConnectedStatus;
    }

    public Double getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(Double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public Boolean getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(Boolean connectivity) {
        this.connectivity = connectivity;
    }

    public Boolean getCoolantTemperatureAlertStatus() {
        return coolantTemperatureAlertStatus;
    }

    public void setCoolantTemperatureAlertStatus(Boolean coolantTemperatureAlertStatus) {
        this.coolantTemperatureAlertStatus = coolantTemperatureAlertStatus;
    }

    public Boolean getEngineOilPressureAlertStatus() {
        return engineOilPressureAlertStatus;
    }

    public void setEngineOilPressureAlertStatus(Boolean engineOilPressureAlertStatus) {
        this.engineOilPressureAlertStatus = engineOilPressureAlertStatus;
    }

    public String getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(String engineStatus) {
        this.engineStatus = engineStatus;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(Double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public Double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(Double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Boolean getFuelLevelStatus() {
        return fuelLevelStatus;
    }

    public void setFuelLevelStatus(Boolean fuelLevelStatus) {
        this.fuelLevelStatus = fuelLevelStatus;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getImsiNumber() {
        return imsiNumber;
    }

    public void setImsiNumber(String imsiNumber) {
        this.imsiNumber = imsiNumber;
    }

    public Date getLastCommunicationTime() {
        return lastCommunicationTime;
    }

    public void setLastCommunicationTime(Date lastCommunicationTime) {
        this.lastCommunicationTime = lastCommunicationTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Double getServiceDueHours() {
        return serviceDueHours;
    }

    public void setServiceDueHours(Double serviceDueHours) {
        this.serviceDueHours = serviceDueHours;
    }

    public Date getStatusAsOnTime() {
        return statusAsOnTime;
    }

    public void setStatusAsOnTime(Date statusAsOnTime) {
        this.statusAsOnTime = statusAsOnTime;
    }

    public Double getTotalMachineHours() {
        return Double.parseDouble(DOUBLE_FORMAT.format(totalMachineHours));
    }

    public void setTotalMachineHours(Double totalMachineHours) {
        this.totalMachineHours = Double.parseDouble(DOUBLE_FORMAT.format(totalMachineHours));
    }

    public TransitMode getTransitMode() {
        return transitMode;
    }

    public void setTransitMode(TransitMode transitMode) {
        this.transitMode = transitMode;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Date getServiceDoneDate() {
        return serviceDoneDate;
    }

    public void setServiceDoneDate(Date serviceDoneDate) {
        this.serviceDoneDate = serviceDoneDate;
    }

    public Double getServiceDoneHours() {
        return serviceDoneHours;
    }

    public void setServiceDoneHours(Double serviceDoneHours) {
        this.serviceDoneHours = serviceDoneHours;
    }

    public Date getServiceDueDate() {
        return serviceDueDate;
    }

    public void setServiceDueDate(Date serviceDueDate) {
        this.serviceDueDate = serviceDueDate;
    }

    public Double getServiceOverDueHours() {
        return serviceOverDueHours;
    }

    public void setServiceOverDueHours(Double serviceOverDueHours) {
        this.serviceOverDueHours = serviceOverDueHours;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Double getCenterLong() {
        return centerLong;
    }

    public void setCenterLong(Double centerLong) {
        this.centerLong = centerLong;
    }

    public Long getRadius() {
        return radius;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    @PreUpdate
    public void onUpdate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        this.lastModified = format.format(new Date());
    }

    public Boolean isRenewalFlag() {
        return renewalFlag;
    }

    public void setRenewalFlag(Boolean renewalFlag) {
        this.renewalFlag = renewalFlag;
    }



    public Date getRolloffDate() {
        return rolloffDate;
    }

    public void setRolloffDate(Date rolloffDate) {
        this.rolloffDate = rolloffDate;
    }



    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }



    public Date getPremiumStartDate() {
        return premiumStartDate;
    }

    public Date getPremiumEndDate() {
        return premiumEndDate;
    }

    public String getPremiumFlag() {
        return premiumFlag;
    }

    public void setPremiumStartDate(Date premiumStartDate) {
        this.premiumStartDate = premiumStartDate;
    }

    public void setPremiumEndDate(Date premiumEndDate) {
        this.premiumEndDate = premiumEndDate;
    }

    public void setPremiumFlag(String premiumFlag) {
        this.premiumFlag = premiumFlag;
    }



    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }


    public String getDealerId() {
        return dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public String getDealerLastName() {
        return dealerLastName;
    }

    public String getDealerFirstName() {
        return dealerFirstName;
    }

    public String getDealerNumber() {
        return dealerNumber;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public void setDealerLastName(String dealerLastName) {
        this.dealerLastName = dealerLastName;
    }

    public void setDealerFirstName(String dealerFirstName) {
        this.dealerFirstName = dealerFirstName;
    }

    public void setDealerNumber(String dealerNumber) {
        this.dealerNumber = dealerNumber;
    }



    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getDealerEmail() {
        return dealerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setDealerEmail(String dealerEmail) {
        this.dealerEmail = dealerEmail;
    }

    public MachineFeedParserData getMachineFeedParserData() {
        return machineFeedParserData;
    }

    public void setMachineFeedParserData(MachineFeedParserData machineFeedParserData) {
        this.machineFeedParserData = machineFeedParserData;
    }



    public Boolean getPremiumFeature() {
        return premiumFeature;
    }

    public void setPremiumFeature(Boolean premiumFeature) {
        this.premiumFeature = premiumFeature;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Machine(Boolean airFilterAlertStatus, Boolean batteryChargeHighStatus, Boolean batteryChargeLowStatus,
                   Boolean batteryChargingStatus, Boolean batteryConnectedStatus, Double batteryVoltage, Boolean connectivity,
                   Boolean coolantTemperatureAlertStatus, Boolean engineOilPressureAlertStatus, String engineStatus,
                   String firmwareVersion, Double fuelCapacity, Double fuelLevel, Boolean fuelLevelStatus, String imeiNumber,
                   String imsiNumber, Date lastCommunicationTime, Double latitude, Double longitude, String location,
                   String model, String platform, Double serviceDueHours, Date statusAsOnTime, Double totalMachineHours,
                   TransitMode transitMode, String zone, String vin, Customer customer, Dealer dealer, String image,
                   String thumbnail, String tag, Operator operator, Set<User> users, Date serviceDoneDate,
                   Double serviceDoneHours, Date serviceDueDate, Double serviceOverDueHours, String site, Date renewalDate,
                   String startTime, String endTime, Boolean renewalFlag,String customerNumber,String customerId,String customerName,String customerFirstName,String customerLastName,String customerAddress,
                   String dealerId,String dealerName,String dealerNumber,String dealerFirstName,String dealerLastName,String dealerAddress,String dealerEmail,String customerEmail,String premiumFlag,Boolean premiumFeature) {
        super();
        this.airFilterAlertStatus = airFilterAlertStatus;
        this.batteryChargeHighStatus = batteryChargeHighStatus;
        this.batteryChargeLowStatus = batteryChargeLowStatus;
        this.batteryChargingStatus = batteryChargingStatus;
        this.batteryConnectedStatus = batteryConnectedStatus;
        this.batteryVoltage = batteryVoltage;
        this.connectivity = connectivity;
        this.coolantTemperatureAlertStatus = coolantTemperatureAlertStatus;
        this.engineOilPressureAlertStatus = engineOilPressureAlertStatus;
        this.engineStatus = engineStatus;
        this.firmwareVersion = firmwareVersion;
        this.fuelCapacity = fuelCapacity;
        this.fuelLevel = fuelLevel;
        this.fuelLevelStatus = fuelLevelStatus;
        this.imeiNumber = imeiNumber;
        this.imsiNumber = imsiNumber;
        this.lastCommunicationTime = lastCommunicationTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.model = model;
        this.platform = platform;
        this.serviceDueHours = serviceDueHours;
        this.statusAsOnTime = statusAsOnTime;
        this.totalMachineHours = totalMachineHours;
        this.transitMode = transitMode;
        this.zone = zone;
        this.vin = vin;
        this.customer = customer;
        this.dealer = dealer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.tag = tag;
        this.operator = operator;
        this.users = users;
        this.serviceDoneDate = serviceDoneDate;
        this.serviceDoneHours = serviceDoneHours;
        this.serviceDueDate = serviceDueDate;
        this.serviceOverDueHours = serviceOverDueHours;
        this.site = site;
        this.renewalDate = renewalDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.renewalFlag = renewalFlag;
        this.customerNumber = customerNumber;
        this.customerId = customerId;
        this.customerName =  customerName;
        this.customerFirstName =  customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAddress = customerAddress;
        this.dealerId = dealerId;
        this.dealerName = dealerName;
        this.dealerAddress = dealerAddress;
        this.dealerNumber = dealerNumber;
        this.dealerFirstName = dealerFirstName;
        this.dealerLastName = dealerLastName;
        this.customerEmail = customerEmail;
        this.dealerEmail =  dealerEmail;
        this.premiumFlag = premiumFlag;
        this.premiumFeature = premiumFeature;
    }

    @Override
    public String toString() {
        return "Machine [airFilterAlertStatus=" + airFilterAlertStatus + ", batteryChargeHighStatus="
                + batteryChargeHighStatus + ", batteryChargeLowStatus=" + batteryChargeLowStatus
                + ", batteryChargingStatus=" + batteryChargingStatus + ", batteryConnectedStatus="
                + batteryConnectedStatus + ", batteryVoltage=" + batteryVoltage + ", connectivity=" + connectivity
                + ", coolantTemperatureAlertStatus=" + coolantTemperatureAlertStatus + ", engineOilPressureAlertStatus="
                + engineOilPressureAlertStatus + ", engineStatus=" + engineStatus + ", firmwareVersion="
                + firmwareVersion + ", fuelCapacity=" + fuelCapacity + ", fuelLevel=" + fuelLevel + ", fuelLevelStatus="
                + fuelLevelStatus + ", imeiNumber=" + imeiNumber + ", imsiNumber=" + imsiNumber
                + ", lastCommunicationTime=" + lastCommunicationTime + ", latitude=" + latitude + ", longitude="
                + longitude + ", location=" + location + ", model=" + model + ", platform=" + platform
                + ", serviceDueHours=" + serviceDueHours + ", statusAsOnTime=" + statusAsOnTime + ", totalMachineHours="
                + totalMachineHours + ", transitMode=" + transitMode + ", zone=" + zone + ", vin=" + vin + ", customer="
                + customer + ", dealer=" + dealer + ", image=" + image + ", thumbnail=" + thumbnail + ", tag=" + tag
                + ", operator=" + operator + ", users=" + users + ", serviceDoneDate=" + serviceDoneDate
                + ", serviceDoneHours=" + serviceDoneHours + ", serviceDueDate=" + serviceDueDate
                + ", serviceOverDueHours=" + serviceOverDueHours + ", site=" + site + ", renewalDate=" + renewalDate
                + ", startTime=" + startTime + ", endTime=" + endTime + ", renewalFlag=" + renewalFlag + ", customerNumber=" + customerNumber + ", customerId=" + customerId + ", customerName=" + customerName + ", customerFirstName=" + customerFirstName +
                ",customerLastName=" + customerLastName +", dealerId=" + dealerId +", dealerName=" + dealerName +", dealerNumber=" + dealerNumber +", dealerFirstName=" + dealerFirstName +", dealerLastName=" + dealerLastName +", dealerAddress=" + dealerAddress +", dealerEmail=" + dealerEmail +", customerEmail=" + customerEmail +"]";
    }

}

