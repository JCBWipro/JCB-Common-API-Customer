package com.wipro.jcb.livelink.app.reports.report;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CanExcavatorV3
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class CanExcavatorV3 extends VisualizationReport {

	private String reportName = "CANEXCAVATOR";

	private List<FuelConsumptionResponse> excavatorFuelConsumption;

	private List<ExcavatorTravelAndSwingTime> travelAndSwingTime;

	private List<ExcavatorPowerModes> powerModes;

	private List<HammerUsedHours> hammerUsedHours;

	private List<HammerAbuseEventCount> hammerAbuseEventCount;

	private String message;

	List<AdvanceReportChart> chartList;

	List<FuelConsumptionResponse> averageFuelConsumption = new LinkedList<>();

}
