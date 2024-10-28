package com.wipro.jcb.livelink.app.reports.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineExcavationMode {

	private Double economyModeHrs;
	private Double powerModeHrs;
	private Double activeModeHrs;
	private Integer economyHrs;
	private Integer powerHrs;
	private Integer activeHrs;
	
	public MachineExcavationMode(Double economyModeHrs, Double powerModeHrs, Double activeModeHrs) {
		super();
		this.economyModeHrs = economyModeHrs;
		this.powerModeHrs = powerModeHrs;
		this.activeModeHrs = activeModeHrs;
	}
	
	
}
