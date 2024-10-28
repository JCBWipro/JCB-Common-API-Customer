package com.wipro.jcb.livelink.app.reports.report;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcavatorReport {
	
	private List<FuelConsumptionResponse> excavatorFuelConsumption;
	private List<ExcavatorTravelAndSwingTime> travelAndSwingTime;
	private List<ExcavatorPowerModes> powerModes;
	List<FuelConsumptionResponse> averageFuelConsumption = new LinkedList<>();
	private List<HammerUsedHours> hammerUsedHours;
	private List<HammerAbuseEventCount> hammerAbuseEventCount;

}
