package com.wipro.jcb.livelink.app.machines.service.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This MachineProfile is to handle Machine Profile related Details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineProfile {
	
	@Schema(description = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
	private String vin;
	@Schema(description = "Model", example = "3DX Super ecoXcellence", required = true)
	private String model;
	@Schema(description = "platform", example = "Backhoe Loader", required = true)
	private String platform;
	@Schema(description = "Reg. No/Location/Nickname", example = "-", required = true)
	private String tag;
	@Schema(description = "dealerName", example = "Leroy", required = true)
	private String dealerName;
	@Schema(description = "dealerContact", example = "12345567", required = true)
	private String dealerContact;
	@Schema(description = "operatorName", example = "Leroy", required = true)
	private String operatorName;
	@Schema(description = "phoneNumber", example = "1234567890", required = true)
	private String phoneNumber;
	@Schema(description = "hours", example = "16", required = true)
	private String hours;
	@Schema(description = "workStart", example = "2001-07-13", required = true)
	@JsonProperty("workStart")
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	private Date workStart;
	@Schema(description = "workEnd", example = "2017-07-13", required = true)
	@JsonProperty("workEnd")
	@JsonFormat(pattern = AppServerConstants.DateFormat, timezone = AppServerConstants.timezone)
	private Date workEnd;
	@Schema(description = "Operator is jcb certified", example = "false", allowableValues = "true,false", required = true)
	private Boolean jcbCertified;
	@Schema(description = "Machine image", example = "Image URL/Path", required = true)
	private String image;
	@Schema(description = "Machine thumbnail image", example = "Image URL/Path", required = true)
	private String thumbnail;
	@Schema(description = "Machine firmwareVersion ", example = "09.01.02", required = true)
	private String firmwareVersion;
	@Schema(description = "Machine imeiNumber", example = "12345671", required = true)
	private String imeiNumber;
	@Schema(description = "Machine imsiNumber", example = "1234567", required = true)
	private String imsiNumber;
	@Schema(description = "Machine transitMode ", example = "NORMAL", required = true)
	private TransitMode transitMode;
	@Schema(description = "Machine on which location currently working", example = "Any taluka place", required = true)
	private String site;
	private GeofenceParam geofenceParam = new GeofenceParam();
	private TimefenceParam timefenceParam= new TimefenceParam();

	private String machineType;
	
	private CustomerInfo customerInfo;
	
	private String renewalDate;
	
	private String firmwareType;
	
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

}
