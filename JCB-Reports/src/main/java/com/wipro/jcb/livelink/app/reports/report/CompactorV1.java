package com.wipro.jcb.livelink.app.reports.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CompactorV1
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class CompactorV1 extends VisualizationReport{
	
	private String reportName = "CANCOMPACTOR";
	private String message;
	List<AdvanceReportChart>  chartList;
	private List<FuelConsumptionResponse> compactionFuelConsumption;
	List<FuelConsumptionResponse> averageFuelConsumption ;
	private List<MachinePowerBands> fuelPowerBand;

}
