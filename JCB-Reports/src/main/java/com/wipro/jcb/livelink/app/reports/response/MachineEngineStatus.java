package com.wipro.jcb.livelink.app.reports.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineEngineStatus {
	
	private Double engineOn;
	private Double engineOff;
	private Integer fuel;

}
