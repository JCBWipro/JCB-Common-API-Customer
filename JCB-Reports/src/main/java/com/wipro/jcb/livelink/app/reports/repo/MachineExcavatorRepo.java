package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.MachineExcavatorData;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorPowerModes;
import com.wipro.jcb.livelink.app.reports.report.ExcavatorTravelAndSwingTime;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumption;

@Repository
public interface MachineExcavatorRepo extends CrudRepository<MachineExcavatorData, String> {
	
	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_excavator_data md where md.vin =:vin and (md.day between :reportStartDate and :reportEndDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);
	
	@Query("select new com.wipro.jcb.livelink.app.reports.report.ExcavatorPowerModes(m.day,Coalesce(m.periodLBandHrs,0),Coalesce(m.periodGBandHrs,0),Coalesce(m.periodHBandHrs,0),Coalesce(m.periodHPlusBandHrs,0)) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<ExcavatorPowerModes> getPowerModes(String vin, Date startDate, Date endDate);
	
	@Query("select new com.wipro.jcb.livelink.app.reports.report.ExcavatorTravelAndSwingTime(m.day,Coalesce(m.totalHrs,0),Coalesce(m.travelHrs,0),Coalesce(m.slewHrs,0)) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<ExcavatorTravelAndSwingTime> getTravelAndSwingTime(String vin, Date startDate, Date endDate);
	
}
