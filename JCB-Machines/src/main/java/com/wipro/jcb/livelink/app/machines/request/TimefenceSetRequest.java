package com.wipro.jcb.livelink.app.machines.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This TimefenceSetRequest is to set start and end time
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimefenceSetRequest {

	private String startTime;
	private String endTime;
	private String vin;

}
