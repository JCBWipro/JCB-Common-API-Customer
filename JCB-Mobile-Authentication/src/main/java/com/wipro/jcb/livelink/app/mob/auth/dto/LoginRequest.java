package com.wipro.jcb.livelink.app.mob.auth.dto;

import io.swagger.annotations.ApiModelProperty;

public class LoginRequest {
    @ApiModelProperty(value = "Inique identifier for user", example = "Leroy_Holland", required = true)
    private String userName;
    @ApiModelProperty(value = "Role/Type of user", example = "Customer", required = true)
    private String userType;
    @ApiModelProperty(value = "Credential of User", example = "*********", required = true)
    private String password;
    @ApiModelProperty(value = "Unique device/app identifier on which notification to send", example = "AGKJHFJAHFLW", required = true)
    private String pushNotificationToken;
    @ApiModelProperty(value = "Operating system on device", example = "Android", allowableValues = "Android,iOS", required = true)
    private String os;
    @ApiModelProperty(value = "Enable/Disbale notification", example = "true", allowableValues = "true,false", required = false)
    private Boolean enableNotification = true;
    @ApiModelProperty(value = "Enable/Disbale notification", example = "true", allowableValues = "true,false", required = false)
    private Boolean enableMachineUpdate = false;
    @ApiModelProperty(value = "mobile app version details", example = "1.0.2", allowableValues = "2.0.0,3.0.0", required = false)
    private String userAppVersion;
    @ApiModelProperty(value = "User Language", example = "English", required = false)
    private String language;
    
    public LoginRequest() {}

	public LoginRequest(String userName, String userType, String password, String pushNotificationToken, String os,
			Boolean enableNotification, Boolean enableMachineUpdate, String userAppVersion, String language) {
		super();
		this.userName = userName;
		this.userType = userType;
		this.password = password;
		this.pushNotificationToken = pushNotificationToken;
		this.os = os;
		this.enableNotification = enableNotification;
		this.enableMachineUpdate = enableMachineUpdate;
		this.userAppVersion = userAppVersion;
		this.language = language;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPushNotificationToken() {
		return pushNotificationToken;
	}

	public void setPushNotificationToken(String pushNotificationToken) {
		this.pushNotificationToken = pushNotificationToken;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Boolean getEnableNotification() {
		return enableNotification;
	}

	public void setEnableNotification(Boolean enableNotification) {
		this.enableNotification = enableNotification;
	}

	public Boolean getEnableMachineUpdate() {
		return enableMachineUpdate;
	}

	public void setEnableMachineUpdate(Boolean enableMachineUpdate) {
		this.enableMachineUpdate = enableMachineUpdate;
	}

	public String getUserAppVersion() {
		return userAppVersion;
	}

	public void setUserAppVersion(String userAppVersion) {
		this.userAppVersion = userAppVersion;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
    
    

}