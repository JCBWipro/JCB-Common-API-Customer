package com.wipro.jcb.livelink.app.reports.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachinePowerBand {
	
	private Double high;
	private Double low;
	private Double medium;
	private Integer highSpeed;
	private Integer lowSpeed;
	private Integer mediumSpeed;
	private Double powerBandLowInHours;
	private Double powerBandMediumInHours;
	private Double powerBandHighInHours;
}
