package com.wipro.jcb.livelink.app.reports.report;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CanTelehandler
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CanTelehandler extends VisualizationReport {

	private String reportName = "CANTELEHANDLER";
	private String message;
	List<AdvanceReportChart> chartList;
	List<FuelConsumptionResponse> telehandlerFuelConsumption;
	List<FuelConsumptionResponse> averageFuelConsumption;
	List<MachinePowerBands> fuelPowerBand;
	
	public void setCompactionFuelConsumption(ArrayList<FuelConsumptionResponse> arrayList) {}

}
