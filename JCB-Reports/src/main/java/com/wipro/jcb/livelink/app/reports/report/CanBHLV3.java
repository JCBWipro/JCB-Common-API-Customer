package com.wipro.jcb.livelink.app.reports.report;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to CanBHLV3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CanBHLV3 extends VisualizationReport {

	private String reportName = "CANBHL";
	List<DutyCycleBHL> dutyCycleBHLList = new LinkedList<>();
	List<ExcavationModesBHL> excavationModesList = new LinkedList<>();
	List<GearTimeSpentBHL> gearTimeSpentBHLList = new LinkedList<>();
	List<MachineCompassBHL> machineCompassBHLList = new LinkedList<>();
	List<FuelConsumptionResponse> wlsFuelConsumption = new LinkedList<>();
	List<FuelConsumptionResponse> averageFuelConsumption = new LinkedList<>();
	List<FuelConsumptionDuty> FuelConsumptionDuty = new LinkedList<>();
	List<FuelConsumptionExcavation> FuelConsumptionExcavation = new LinkedList<>();
	List<DistanceTraveledRoading> DistanceRoading = new LinkedList<>();
	List<AverageSpeedRoading> AverageRoading = new LinkedList<>();
	private String message;
	List<AdvanceReportChart> chartList;

}
