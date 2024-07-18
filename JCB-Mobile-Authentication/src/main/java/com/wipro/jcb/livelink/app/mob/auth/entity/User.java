package com.wipro.jcb.livelink.app.mob.auth.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LiveLinkUser")
public class User implements Serializable {
    
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
    private Boolean sysGenPass;
    private String activityCompleted;
    private String serviceHistoryStatus;
    private List<String> livelinkPersonName;
    private String roleName;
    private Boolean isSecretQuestion;

    private Boolean machineUpdateNotificationEnabled= false;
    private String userAppVersion;

    private Date createdAt;

    @Column(name="analytic_last_visited_time")
    private Date analyticLastVistedTime;

    @Column(name = "feed_date")
    private Date feedDate;

    @Column(name = "language")
    private String language;
    
    public User() {}

    public User(String userName, String email, UserType userType, String firstName, String lastName, String phoneNumber,
                String address, String smsLanguage, String timeZone, String image, String thumbnail, String country,
                String password, Boolean sysGenPass, String activityCompleted, List<String> livelinkPersonName,
                String roleName, Boolean isSecretQuestion, String serviceHistoryStatus, Boolean machineUpdateNotificationEnabled, String language) {
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
    
    

	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public UserType getUserType() {
		return userType;
	}



	public void setUserType(UserType userType) {
		this.userType = userType;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getSmsLanguage() {
		return smsLanguage;
	}



	public void setSmsLanguage(String smsLanguage) {
		this.smsLanguage = smsLanguage;
	}



	public String getTimeZone() {
		return timeZone;
	}



	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
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



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Boolean getSysGenPass() {
		return sysGenPass;
	}



	public void setSysGenPass(Boolean sysGenPass) {
		this.sysGenPass = sysGenPass;
	}



	public String getActivityCompleted() {
		return activityCompleted;
	}



	public void setActivityCompleted(String activityCompleted) {
		this.activityCompleted = activityCompleted;
	}



	public String getServiceHistoryStatus() {
		return serviceHistoryStatus;
	}



	public void setServiceHistoryStatus(String serviceHistoryStatus) {
		this.serviceHistoryStatus = serviceHistoryStatus;
	}



	public List<String> getLivelinkPersonName() {
		return livelinkPersonName;
	}



	public void setLivelinkPersonName(List<String> livelinkPersonName) {
		this.livelinkPersonName = livelinkPersonName;
	}



	public String getRoleName() {
		return roleName;
	}



	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}



	public Boolean getIsSecretQuestion() {
		return isSecretQuestion;
	}



	public void setIsSecretQuestion(Boolean isSecretQuestion) {
		this.isSecretQuestion = isSecretQuestion;
	}



	public Boolean getMachineUpdateNotificationEnabled() {
		return machineUpdateNotificationEnabled;
	}



	public void setMachineUpdateNotificationEnabled(Boolean machineUpdateNotificationEnabled) {
		this.machineUpdateNotificationEnabled = machineUpdateNotificationEnabled;
	}



	public String getUserAppVersion() {
		return userAppVersion;
	}



	public void setUserAppVersion(String userAppVersion) {
		this.userAppVersion = userAppVersion;
	}



	public Date getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}



	public Date getAnalyticLastVistedTime() {
		return analyticLastVistedTime;
	}



	public void setAnalyticLastVistedTime(Date analyticLastVistedTime) {
		this.analyticLastVistedTime = analyticLastVistedTime;
	}



	public Date getFeedDate() {
		return feedDate;
	}



	public void setFeedDate(Date feedDate) {
		this.feedDate = feedDate;
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public String toString() {
		return "User [userName=" + userName + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + ", smsLanguage=" + smsLanguage
				+ ", timeZone=" + timeZone + ", image=" + image + ", thumbnail=" + thumbnail + ", country=" + country
				+ ", password=" + password + ", sysGenPass=" + sysGenPass + ", activityCompleted=" + activityCompleted
				+ ", serviceHistoryStatus=" + serviceHistoryStatus + ", livelinkPersonName=" + livelinkPersonName
				+ ", roleName=" + roleName + ", isSecretQuestion=" + isSecretQuestion
				+ ", machineUpdateNotificationEnabled=" + machineUpdateNotificationEnabled + ", userAppVersion="
				+ userAppVersion + ", createdAt=" + createdAt + ", analyticLastVistedTime=" + analyticLastVistedTime
				+ ", feedDate=" + feedDate + ", language=" + language + "]";
	}

   

}
