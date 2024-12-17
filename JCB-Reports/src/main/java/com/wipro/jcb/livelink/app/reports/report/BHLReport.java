package com.wipro.jcb.livelink.app.reports.report;

import java.util.LinkedList;
import java.util.List;

import com.wipro.jcb.livelink.app.reports.response.MachineDutyCycle;
import com.wipro.jcb.livelink.app.reports.response.MachineExcavationMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BHLReport {
	
	List<FuelConsumptionResponse> fuelConsumption = new LinkedList<>();
	List<FuelConsumptionResponse> dailyFuelAverage = new LinkedList<>();
	List<GearTimeSpentBHL> gearTimeSpentBHLList = new LinkedList<>();
	List<MachineCompassBHL> machineCompassBHLList= new LinkedList<>();
	
	List<FuelConsumptionDuty> FuelConsumptionDuty = new LinkedList<>();
	List<FuelConsumptionExcavation> FuelConsumptionExcavation = new LinkedList<>();
	List<DistanceTraveledRoading> DistanceRoading = new LinkedList<>();
	List<AverageSpeedRoading> AverageRoading = new LinkedList<>();
	
	private MachineDutyCycle machineDutyCycle ;
	private MachineExcavationMode machineExcavationMode;

}
