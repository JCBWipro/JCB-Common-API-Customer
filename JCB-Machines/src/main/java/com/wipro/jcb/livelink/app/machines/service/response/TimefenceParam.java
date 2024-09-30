package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This TimefenceParam is to handle start and endTime related Details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimefenceParam {

	String startTime = "08:00 AM";
	String endTime = "08:00 PM";

}
