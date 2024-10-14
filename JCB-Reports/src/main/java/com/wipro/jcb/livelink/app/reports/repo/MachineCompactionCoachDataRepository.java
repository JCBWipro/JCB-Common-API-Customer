package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineCompactionCoachData;
import com.wipro.jcb.livelink.app.reports.report.CompactionDutyCycleData;
import com.wipro.jcb.livelink.app.reports.report.CompactionVibrationOnOffData;
import com.wipro.jcb.livelink.app.reports.report.CompactionWeeklyDutyCycleData;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumption;

@Component
public interface MachineCompactionCoachDataRepository extends PagingAndSortingRepository<MachineCompactionCoachData, String> {
	
	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_compactor_data md where md.vin =:vin and (md.day between :startDate and :endDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date startDate, Date endDate);
	
	@Query("select new com.wipro.jcb.livelink.app.reports.report.CompactionDutyCycleData(sum(m.highVibrationHrs) as high_vibration,sum(m.lowVibrationHrs) as low_vibration,sum(m.staticPassHrs) as static_pass) from MachineCompactionCoachData m where ?1 = m.vinId")
	public CompactionDutyCycleData findByVinForLifeBetweenDay(String vin);
	
	@Query("select new com.wipro.jcb.livelink.app.reports.report.CompactionWeeklyDutyCycleData(m.highVibrationHrs,m.lowVibrationHrs,m.staticPassHrs,m.day) from MachineCompactionCoachData m where ?1 = m.vinId and m.day between ?2 and ?3 order by m.day asc")
	public List<CompactionWeeklyDutyCycleData> findByVinAndDayBetweenOrderByDayAsc(String vin, Date startDate,Date endDate);
	
	@Query("select new com.wipro.jcb.livelink.app.reports.report.CompactionVibrationOnOffData(m.day,m.vibrationOnHrs,m.vibrationOffHrs) from MachineCompactionCoachData m where ?1 = m.vinId and m.day between ?2 and ?3 order by m.day asc")
	public List<CompactionVibrationOnOffData> findVibrationData(String vin,Date startDate, Date endDate);
	
}
