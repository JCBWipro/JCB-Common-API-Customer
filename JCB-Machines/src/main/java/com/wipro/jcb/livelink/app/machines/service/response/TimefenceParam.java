package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.*;

/**
 * This TimefenceParam is to handle start and endTime related Details
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimefenceParam {

	String startTime = "08:00 AM";
	String endTime = "08:00 PM";

}
