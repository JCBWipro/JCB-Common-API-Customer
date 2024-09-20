package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.UserType;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Entity
@RequiredArgsConstructor
@XmlRootElement
@Table(name = "LiveLinkUser", indexes = { @Index(name = "LiveLinkUser_email", columnList = "email"),
        @Index(name = "LiveLinkUser_userId", columnList = "USER_ID") })
@DynamicUpdate
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 7647358402835262772L;
    @Id
    @ApiModelProperty(value = "unique identifier for user", example = "Lorey_Holland", required = true)
    @Column(name = "USER_ID", unique = true, columnDefinition = "VARCHAR(64)")
    private String userName;
    @Column(name = "email")
    @ApiModelProperty(value = "Email Id of user", example = "lorey@mymail.com", required = true)
    private String email;
    @ApiModelProperty(value = "Role/Type of user", example = "Customer", required = true)
    private UserType userType;
    @ApiModelProperty(value = "FirstName of user", example = "Lorey", required = true)
    private String firstName;
    @ApiModelProperty(value = "Users lastname", example = "Hollandr", required = true)
    private String lastName;
    @ApiModelProperty(value = "Contact of user", example = "1234567890", required = true)
    private String phoneNumber;
    @ApiModelProperty(value = "Address of User", example = "Pune", required = true)
    private String address;
    @ApiModelProperty(value = "User understandable language", example = "English", required = true)
    private String smsLanguage;
    @ApiModelProperty(value = "Time Zone is a region of the globe that observes a uniform standard time", example = "UTC+05:30", required = true)
    private String timeZone;
    @ApiModelProperty(value = "User Photo", example = "user.png", required = true)
    @JsonIgnore
    private String image;
    @ApiModelProperty(value = "User thumbnail Photo", example = "user.png", required = true)
    private String thumbnail;
    @ApiModelProperty(value = "Country in which user resides", example = "India", required = true)
    private String country;
    private String password;
    @ApiModelProperty(value = "true if it is sys gen password", example = "true", required = true)
    private Boolean sysGenPass;
    @JsonIgnore
    private String activityCompleted;
    @JsonIgnore
    private String serviceHistoryStatus;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<String> livelinkPersonName;
    @JsonIgnore
    private String roleName;
    @ApiModelProperty(value = "true if Secret Question is set ", example = "true", required = true)
    private Boolean isSecretQuestion;

    @JsonIgnore
    private Boolean machineUpdateNotificationEnabled= false;
    @JsonIgnore
    private String userAppVersion;

    @JsonIgnore
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="analytic_last_visited_time")
    private Date analyticLastVistedTime;

    @ApiModelProperty(value = "Day", example = "2017-07-13", required = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
    @Column(name = "feed_date")
    private Date feedDate;

    @Column(name = "language")
    @ApiModelProperty(value = "User Language", example = "English", required = true)
    private String language;

    public User(String userName, String email, UserType userType, String firstName, String lastName, String phoneNumber,
                String address, String smsLanguage, String timeZone, String image, String thumbnail, String country,
                String password, Boolean sysGenPass, String activityCompleted, List<String> livelinkPersonName,
                String roleName, Boolean isSecretQuestion,String serviceHistoryStatus,Boolean machineUpdateNotificationEnabled,String language) {
        super();
        this.userName = userName;
        this.email = email;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.smsLanguage = smsLanguage;
        this.timeZone = timeZone;
        this.image = image;
        this.thumbnail = thumbnail;
        this.country = country;
        this.password = password;
        this.sysGenPass = sysGenPass;
        this.activityCompleted = activityCompleted;
        this.livelinkPersonName = livelinkPersonName;
        this.roleName = roleName;
        this.isSecretQuestion = isSecretQuestion;
        this.serviceHistoryStatus= serviceHistoryStatus;
        this.machineUpdateNotificationEnabled = machineUpdateNotificationEnabled;
        this.language=language;
    }

    @Override
    public String toString() {
        return "User [userName=" + userName + ", email=" + email + ", userType=" + userType + ", firstName=" + firstName
                + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", address=" + address + ", smsLanguage="
                + smsLanguage + ", timeZone=" + timeZone + ", image=" + image + ", thumbnail=" + thumbnail
                + ", country=" + country + ", password=" + password + ", sysGenPass=" + sysGenPass
                + ", activityCompleted=" + activityCompleted + ", serviceHistoryStatus=" + serviceHistoryStatus
                + ", livelinkPersonName=" + livelinkPersonName + ", roleName=" + roleName + ", isSecretQuestion="
                + isSecretQuestion + ", machineUpdateNotificationEnabled=" + machineUpdateNotificationEnabled
                + ", userAppVersion=" + userAppVersion + ", createdAt=" + createdAt + "]";
    }

}
