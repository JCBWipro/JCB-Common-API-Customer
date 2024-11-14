package com.wipro.jcb.livelink.app.machines.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {

	@ApiModelProperty(value = "Enable/Disbale SMS Notification", example = "true",  required = false)
	private String  sms;
	
	@ApiModelProperty(value = "Enable/Disbale Push Notification", example = "true",  required = false)
	private String pushNotification;

}
