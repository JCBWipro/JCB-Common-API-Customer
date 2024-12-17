package com.wipro.jcb.livelink.app.reports.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilizationDetails {
	
	private Double utilization;
	private Double utilizationTill;
	private Double idleHrs;
	private Integer idlePercentage;
	private long alertCount;

}
