package com.wipro.jcb.livelink.app.machines.service.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;

import io.swagger.annotations.ApiModelProperty;

public class MachineProfile {
	
	@ApiModelProperty(value = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
	private String vin;
	@ApiModelProperty(value = "Model", example = "3DX Super ecoXcellence", required = true)
	private String model;
	@ApiModelProperty(value = "platform", example = "Backhoe Loader", required = true)
	private String platform;
	@ApiModelProperty(value = "Reg. No/Location/Nickname", example = "-", required = true)
	private String tag;
	@ApiModelProperty(value = "dealerName", example = "Leroy", required = true)
	private String dealerName;
	@ApiModelProperty(value = "dealerContact", example = "12345567", required = true)
	private String dealerContact;
	@ApiModelProperty(value = "operatorName", example = "Leroy", required = true)
	private String operatorName;
	@ApiModelProperty(value = "phoneNumber", example = "1234567890", required = true)
	private String phoneNumber;
	@ApiModelProperty(value = "hours", example = "16", required = true)
	private String hours;
	@ApiModelProperty(value = "workStart", example = "2001-07-13", required = true)
	@JsonProperty("workStart")
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	private Date workStart;
	@ApiModelProperty(value = "workEnd", example = "2017-07-13", required = true)
	@JsonProperty("workEnd")
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	private Date workEnd;
	@ApiModelProperty(value = "Operator is jcb certified", example = "false", allowableValues = "true,false", required = true)
	private Boolean jcbCertified;
	@ApiModelProperty(value = "Machine image", example = "Image URL/Path", required = true)
	private String image;
	@ApiModelProperty(value = "Machine thumbnail image", example = "Image URL/Path", required = true)
	private String thumbnail;
	@ApiModelProperty(value = "Machine firmwareVersion ", example = "09.01.02", required = true)
	private String firmwareVersion;
	@ApiModelProperty(value = "Machine imeiNumber", example = "12345671", required = true)
	private String imeiNumber;
	@ApiModelProperty(value = "Machine imsiNumber", example = "1234567", required = true)
	private String imsiNumber;
	@ApiModelProperty(value = "Machine transitMode ", example = "NORMAL", required = true)
	private TransitMode transitMode;
	@ApiModelProperty(value = "Machine on which location currently working", example = "Any taluka place", required = true)
	private String site;
	private GeofenceParam geofenceParam = new GeofenceParam();
	private TimefenceParam timefenceParam= new TimefenceParam();

	private String machineType;
	
	private CustomerInfo customerInfo;
	
	private String renewalDate;
	
	private String firmwareType;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public Date getWorkStart() {
		return workStart;
	}

	public void setWorkStart(Date workStart) {
		this.workStart = workStart;
	}

	public Date getWorkEnd() {
		return workEnd;
	}

	public void setWorkEnd(Date workEnd) {
		this.workEnd = workEnd;
	}

	public Boolean getJcbCertified() {
		return jcbCertified;
	}

	public void setJcbCertified(Boolean jcbCertified) {
		this.jcbCertified = jcbCertified;
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

	public String getDealerContact() {
		return dealerContact;
	}

	public void setDealerContact(String dealerContact) {
		this.dealerContact = dealerContact;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
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

	public TransitMode getTransitMode() {
		return transitMode;
	}

	public void setTransitMode(TransitMode transitMode) {
		this.transitMode = transitMode;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(String renewalDate) {
		this.renewalDate = renewalDate;
	}
	
	

	public String getFirmwareType() {
		return firmwareType;
	}

	public void setFirmwareType(String firmwareType) {
		this.firmwareType = firmwareType;
	}

	public MachineProfile() {
	}

	public MachineProfile(String vin, String model, String platform, String tag, String dealerName,
			String dealerContact, String operatorName, String phoneNumber, String hours, Date workStart, Date workEnd,
			Boolean jcbCertified, String image, String thumbnail, String firmwareVersion, String imeiNumber,
			String imsiNumber, TransitMode transitMode, String site, GeofenceParam geofenceParam,
			TimefenceParam timefenceParam, CustomerInfo customerInfo, String renewalDate) {
		super();
		this.vin = vin;
		this.model = model;
		this.platform = platform;
		this.tag = tag;
		this.dealerName = dealerName;
		this.dealerContact = dealerContact;
		this.operatorName = operatorName;
		this.phoneNumber = phoneNumber;
		this.hours = hours;
		this.workStart = workStart;
		this.workEnd = workEnd;
		this.jcbCertified = jcbCertified;
		this.image = image;
		this.thumbnail = thumbnail;
		this.firmwareVersion = firmwareVersion;
		this.imeiNumber = imeiNumber;
		this.imsiNumber = imsiNumber;
		this.transitMode = transitMode;
		this.site = site;
		this.geofenceParam = geofenceParam;
		this.timefenceParam = timefenceParam;
		this.customerInfo = customerInfo;
		this.renewalDate = renewalDate;
	}
	

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	@Override
	public String toString() {
		return "MachineProfile [vin=" + vin + ", model=" + model + ", platform=" + platform + ", tag=" + tag
				+ ", dealerName=" + dealerName + ", dealerContact=" + dealerContact + ", operatorName=" + operatorName
				+ ", phoneNumber=" + phoneNumber + ", hours=" + hours + ", workStart=" + workStart + ", workEnd="
				+ workEnd + ", jcbCertified=" + jcbCertified + ", image=" + image + ", thumbnail=" + thumbnail
				+ ", firmwareVersion=" + firmwareVersion + ", imeiNumber=" + imeiNumber + ", imsiNumber=" + imsiNumber
				+ ", transitMode=" + transitMode + ", site=" + site + ", geofenceParam=" + geofenceParam
				+ ", timefenceParam=" + timefenceParam + ", machineType=" + machineType + ", customerInfo="
				+ customerInfo + "]";
	}

	public GeofenceParam getGeofenceParam() {
		return geofenceParam;
	}

	public void setGeofenceParam(GeofenceParam geofenceParam) {
		this.geofenceParam = geofenceParam;
	}

	public TimefenceParam getTimefenceParam() {
		return timefenceParam;
	}

	public void setTimefenceParam(TimefenceParam timefenceParam) {
		this.timefenceParam = timefenceParam;
	}

}
