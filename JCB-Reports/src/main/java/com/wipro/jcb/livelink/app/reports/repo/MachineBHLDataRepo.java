package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineBHLData;
import com.wipro.jcb.livelink.app.reports.report.DutyCycleBHL;
import com.wipro.jcb.livelink.app.reports.report.ExcavationModesBHL;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumption;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionBHL;
import com.wipro.jcb.livelink.app.reports.report.GearTimeSpentBHL;
import com.wipro.jcb.livelink.app.reports.report.MachineCompassBHL;

@Component
public interface MachineBHLDataRepo extends CrudRepository<MachineBHLData, String> {
	
	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_wls_data md where md.vin =:vin and  (md.day between :reportStartDate and :reportEndDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);

	@Query(value = "select  count(*) from machine_bhl_data  where day=?2  and average_fuel_consumption >=?1", nativeQuery = true)
	public long getBhlAverageCountYesterday(Double range, Date yesterDay);

	@Query(value = "select  count(*) from machine_bhl_data  where day=?2  and total_fuel_used_in_ltrs >=?1", nativeQuery = true)
	public long getBhlFuelCountYesterday(Double range, Date yesterDay);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.DutyCycleBHL(md.day,Coalesce(md.attachment,0),Coalesce(md.idling,0),Coalesce(md.excavation,0),Coalesce(md.loading,0),Coalesce(md.roading,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<DutyCycleBHL> getDutyCycleData(String vin,Date startDay, Date endDay);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.ExcavationModesBHL(md.day,Coalesce(md.economyModeHrs,0),Coalesce(md.powerModeHrs,0),Coalesce(md.activeModeHrs,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<ExcavationModesBHL> getExcavationModesData(String vin,Date startDay, Date endDay);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.GearTimeSpentBHL(md.day,Coalesce(md.gear1Utilization,0),Coalesce(md.gear2Utilization,0),Coalesce(md.gear3Utilization,0),Coalesce(md.gear4Utilization,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<GearTimeSpentBHL> getGearTimeSpentData(String vin,Date startDay, Date endDay);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.MachineCompassBHL(md.day,Coalesce(md.forwardDirection,0), Coalesce(md.reverseDirection,0), Coalesce(md.neutralDirection,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<MachineCompassBHL> getMachineCompassBHLData(String vin,Date startDay, Date endDay);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.FuelConsumptionBHL(md.day,Coalesce(md.totalFuelUsedInLtrs,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumptionBHL> getFuelConsumptionData(String vin, Date startDate, Date endDate);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.FuelConsumptionBHL(md.day,Coalesce(md.averageFuelConsumption,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumptionBHL> getAverageFuelConsumption(String vin, Date startDate, Date endDate);
}
