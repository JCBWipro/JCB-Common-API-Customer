package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelehandlerReport {
	
	List<AdvanceReportChart>  chartList;
	List<FuelConsumptionResponse> telehandlerFuelConsumption;
	List<FuelConsumptionResponse> averageFuelConsumption ;
	List<MachinePowerBands> fuelPowerBand;

}
