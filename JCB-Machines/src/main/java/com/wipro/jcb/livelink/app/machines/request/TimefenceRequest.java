package com.wipro.jcb.livelink.app.machines.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimefenceRequest {

	private String mobileNumber;
	private String operatingStartTime;
	private String operatingEndTime;
	private String vin;
	private String notificationPattern;
	private String notificationDate;
	private String recurrenceType;
	private String recurrence;
	private String recurrenceStartDate;
	private String recurrenceEndDate;
	
	private NotificationDetails notificationDetails;
	
}
