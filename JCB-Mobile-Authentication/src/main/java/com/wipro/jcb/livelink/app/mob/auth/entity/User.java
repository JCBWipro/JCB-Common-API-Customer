package com.wipro.jcb.livelink.app.mob.auth.entity;

import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "LiveLinkUser")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 7647358402835262772L;
    @Id
    @Column(name = "USER_ID", unique = true, columnDefinition = "VARCHAR(64)")
    private String userName;
    @Column(name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String smsLanguage;
    private String timeZone;
    private String image;
    private String thumbnail;
    private String country;
    private String password;
    private int sysGenPass;
    private String activityCompleted;
    private String serviceHistoryStatus;
    private List<String> livelinkPersonName;
    private String roleName;
    private Boolean isSecretQuestion;

    private Boolean machineUpdateNotificationEnabled = false;
    private String userAppVersion;

    private Date createdAt;

    @Column(name = "analytic_last_visited_time")
    private Date analyticLastVistedTime;

    @Column(name = "feed_date")
    private Date feedDate;

    @Column(name = "language")
    private String language;

    @Column(name = "reset_pass_count")
    private int resetPassCount;

    @Column(name = "login_failed_count")
    private int loginFailedCount;

    public User() {
    }

    public User(String userName, String email, UserType userType, String firstName, String lastName, String phoneNumber,
                String address, String smsLanguage, String timeZone, String image, String thumbnail, String country,
                String password, int sysGenPass, String activityCompleted, List<String> livelinkPersonName,
                String roleName, Boolean isSecretQuestion, String serviceHistoryStatus, Boolean machineUpdateNotificationEnabled, String language, int resetPassCount, int loginFailedCount) {
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
        this.serviceHistoryStatus = serviceHistoryStatus;
        this.machineUpdateNotificationEnabled = machineUpdateNotificationEnabled;
        this.language = language;
        this.resetPassCount = resetPassCount;
        this.loginFailedCount = loginFailedCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", smsLanguage='" + smsLanguage + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", image='" + image + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", country='" + country + '\'' +
                ", password='" + password + '\'' +
                ", sysGenPass=" + sysGenPass +
                ", activityCompleted='" + activityCompleted + '\'' +
                ", serviceHistoryStatus='" + serviceHistoryStatus + '\'' +
                ", livelinkPersonName=" + livelinkPersonName +
                ", roleName='" + roleName + '\'' +
                ", isSecretQuestion=" + isSecretQuestion +
                ", machineUpdateNotificationEnabled=" + machineUpdateNotificationEnabled +
                ", userAppVersion='" + userAppVersion + '\'' +
                ", createdAt=" + createdAt +
                ", analyticLastVistedTime=" + analyticLastVistedTime +
                ", feedDate=" + feedDate +
                ", language='" + language + '\'' +
                ", resetPassCount=" + resetPassCount +
                ", loginFailedCount=" + loginFailedCount +
                '}';
    }
}
