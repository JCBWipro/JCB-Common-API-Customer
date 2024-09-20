package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.service.reports.FuelConsumption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/*
@Component
public interface MachineCompactionCoachDataRepository extends PagingAndSortingRepository<MachineCompactionCoachData, String> {
	
	// below method to get data from database
	
	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.CompactionDutyCycleData(sum(m.highVibrationHrs) as high_vibration,sum(m.lowVibrationHrs) as low_vibration,sum(m.staticPassHrs) as static_pass) from MachineCompactionCoachData m where ?1 = m.vinId")
	public CompactionDutyCycleData findByVinForLifeBetweenDay(String vin);
	
	
	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.CompactionWeeklyDutyCycleData(m.highVibrationHrs,m.lowVibrationHrs,m.staticPassHrs,m.day) from MachineCompactionCoachData m where ?1 = m.vinId and m.day between ?2 and ?3 order by m.day asc")
	public List<CompactionWeeklyDutyCycleData> findByVinAndDayBetweenOrderByDayAsc(String vin, Date startDate,Date endDate);
	
	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.CompactionVibrationOnOffData(m.day,m.vibrationOnHrs,m.vibrationOffHrs) from MachineCompactionCoachData m where ?1 = m.vinId and m.day between ?2 and ?3 order by m.day asc")
	public List<CompactionVibrationOnOffData> findVibrationData(String vin,Date startDate, Date endDate);
	
	public MachineCompactionCoachData findByDayAndVin(Date date, String vin);
	
	public List<MachineCompactionCoachData> findByVin(String vin);

	@Async
	public <S extends MachineCompactionCoachData> S save(S entity);

	public <S extends MachineCompactionCoachData> Iterable<S> save(Iterable<S> entities);

	@Transactional
	@Modifying
	@Query("delete from MachineCompactionCoachData comp where comp.day < ?1 and comp.vin in (select vin from Machine where premiumFeature=false) ")
	public void deletByDay(Date date);
	
	@Query(value="select day\\:\\:date from generate_series(?2, ?3, interval '1 day') as day where day not in("
			+ "select day from machine_compactor_data where day between ?2 and ?3 and vin = ?1)",nativeQuery=true)
	public List<Date> getDataMissingDates(String vin, Date startDate, Date endDate);

	@Query("select count(*) from MachineCompactionCoachData com where com.day = ?1")
	public Long getCompactionCountByYesterdate(Date startDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineCompactionCoachData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumption> getFuelConsumptionData(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.averageFuelConsumption) from MachineCompactionCoachData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumption> getAverageFuelConsumption(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.MachinePowerBands(md.day,md.fuelUsedInLowPowerBandLtrs,md.fuelUsedInMediumPowerBandLtrs,md.fuelUsedInHighPowerBandLtrs,md.fuelUsedInIdlePowerBandLtrs) from MachineCompactionCoachData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<MachinePowerBands> getFuelPowerBand(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs,md.averageFuelConsumption) from MachineCompactionCoachData md where md.vin =?1 and (md.day between ?2 and ?3) and totalFuelUsedInLtrs is not null order by day")
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date startDate, Date endDate);

	@Query("select md.totalFuelUsedInLtrs from MachineCompactionCoachData md where md.vin =?1 and md.day =?2")
	public Double getFuelConsumption(String vin, Date yesterday);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineCompactionCoachData md where md.vin =?1 and md.day=?2 and md.totalFuelUsedInLtrs>0 ")
	public List<FuelConsumption> getFuelConsumptionDetailsReport(String vin, Date reportStartDate);
	
	
	
}
*/
