package com.wipro.jcb.livelink.app.reports.report;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WLSReport {
	
	List<FuelConsumptionResponse> fuelConsumption = new LinkedList<>();
	List<FuelConsumptionResponse> dailyFuelAverage = new LinkedList<>();
	private List<WheelLoaderGearUtilization> wlsGearUtilization;
	List<MachinePowerBands> fuelPowerBand = new ArrayList<>();

}
