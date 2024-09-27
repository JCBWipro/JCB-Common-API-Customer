package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.service.reports.FuelConsumption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/*@Component
public interface MachineExcavatorRepo extends CrudRepository<MachineExcavatorData, String> {
	
	@Async
	public <S extends MachineExcavatorData> Iterable<S> save(Iterable<S> entities);

	@Override
	public <S extends MachineExcavatorData> S save(S entity);
	
	public MachineExcavatorData findByDayAndVin(Date day, String vin);
	
	public List<MachineExcavatorData> findByVin(String vin);
	
	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(m.day,Coalesce(m.totalFuelUsedInLtrs,0)) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<FuelConsumption> getFuelConsumptionData(String vin, Date startDate, Date endDate);
	
	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.ExcavatorPowerModes(m.day,Coalesce(m.periodLBandHrs,0),Coalesce(m.periodGBandHrs,0),Coalesce(m.periodHBandHrs,0),Coalesce(m.periodHPlusBandHrs,0)) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<ExcavatorPowerModes> getPowerModes(String vin, Date startDate, Date endDate);
	
	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.ExcavatorTravelAndSwingTime(m.day,Coalesce(m.totalHrs,0),Coalesce(m.travelHrs,0),Coalesce(m.slewHrs,0)) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<ExcavatorTravelAndSwingTime> getTravelAndSwingTime(String vin, Date startDate, Date endDate);
	
	@Transactional
	@Modifying
	@Query("delete from MachineExcavatorData excv where excv.day < ?1 and excv.vin in (select vin from Machine where premiumFeature=false) ")
	public void deletByDay(Date date);
	
	@Query(value="select day\\:\\:date from generate_series(?2, ?3, interval '1 day') as day where day not in("
			+ "select day from machine_excavator_data where day between ?2 and ?3 and vin = ?1)",nativeQuery=true)
	public List<Date> getDataMissingDates(String vin, Date startDate, Date endDate);
	
	@Query("select count(*) from MachineExcavatorData excv where excv.day = ?1")
	public Long getExcavatorCountByYesterday(Date startDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs,md.averageFuelConsumption) from MachineExcavatorData md where md.vin =?1 and (md.day between ?2 and ?3) and totalFuelUsedInLtrs is not null order by day")
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineExcavatorData md where md.vin =?1 and md.day=?2 and md.totalFuelUsedInLtrs>0")
	public List<FuelConsumption> getFuelConsumptionDetailsReport(String vin, Date reportStartDate);

	@Query("select md.totalFuelUsedInLtrs from MachineExcavatorData md where md.vin =?1 and md.day =?2")
	public Double getFuelConsumption(String vin, Date yesterday);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(m.day,m.averageFuelConsumption) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<FuelConsumption> getAverageConsumptionData(String vin, Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.MachineFuelConsumption(m.day,m.totalFuelUsedInLtrs) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<MachineFuelConsumption> getFuelConsumptionDataV2(String vin, Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(m.day,m.totalFuelUsedInLtrs) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<FuelConsumption> getFuelConsumptionDataV3(String vin, Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.ExcavatorPowerModes(m.day,m.periodLBandHrs,m.periodGBandHrs,m.periodHBandHrs,m.periodHPlusBandHrs) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<ExcavatorPowerModes> getPowerModesV3(String vin, Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.ExcavatorTravelAndSwingTime(m.day,m.totalHrs,m.travelHrs,m.slewHrs) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<ExcavatorTravelAndSwingTime> getTravelAndSwingTimeV3(String vin, Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.HammerUsedHours(m.day,m.hammerUsedTimeHrs) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<HammerUsedHours> getHammerUserHours(String vin, Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.HammerAbuseEventCount(m.day,m.hammerAbuseCount) from MachineExcavatorData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<HammerAbuseEventCount> getHammerAbuseEventCount(String vin, Date startDate, Date endDate);

}*/
