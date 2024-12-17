package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompactorReport {
	
	private List<FuelConsumptionResponse> compactionFuelConsumption;
	List<FuelConsumptionResponse> averageFuelConsumption ;
	private List<MachinePowerBands> fuelPowerBand;

}
