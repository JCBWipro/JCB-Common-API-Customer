package com.wipro.jcb.livelink.app.machines.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceRequest {
	
	private String mobileNumber;
	private String landmarkName;
	@ApiModelProperty(value = "Geofence Details -  Landmark", example = "2", allowableValues = "1", required = false)
	private Integer landmarkId;
	private Double radius ;
	private String address;
	private String latitude ;
	private String longitude ;
	private String isArrival;
	private String isDepature;
	private String vin ;
	private String machineType;
	
	private NotificationDetails notificationDetails;
	
}
