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

/*@Component
public interface MachineWheelLoaderDataRepository extends PagingAndSortingRepository<MachineWheelLoaderData, String> {
	
	// below method to get data from database
	
	public List<MachineWheelLoaderData> findByVinAndDayBetweenOrderByDayAsc(String vin, Date startDate,
				Date endDate);
	
	public List<MachineWheelLoaderData> findByVin(String vin);
		
	public MachineWheelLoaderData findByDayAndVin(Date date, String vin);
	
	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.WeekelyBucketCount(m.day,Coalesce(m.numberOfBuckets,0)) from MachineWheelLoaderData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<WeekelyBucketCount> getBucketCountData(String vin,Date startDate, Date endDate);

	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.WeekelyDutyCycle(m.day,Coalesce(m.cumulativeLoadedWeight,0)) from MachineWheelLoaderData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<WeekelyDutyCycle> getDutyCycleData(String vin,Date startDate, Date endDate);
	
	@Async
	public <S extends MachineWheelLoaderData> S save(S entity);

	public <S extends MachineWheelLoaderData> Iterable<S> save(Iterable<S> entities);
	
	@Transactional
	@Modifying
	@Query("delete from MachineWheelLoaderData wls where wls.day < ?1 and wls.vin in (select vin from Machine where premiumFeature=false) )")
	public void deletByDay(Date date);
	
	@Query(value="select day\\:\\:date from generate_series(?2, ?3, interval '1 day') as day where day not in("
			+ "select day from machine_wls_data where day between ?2 and ?3 and vin = ?1)",nativeQuery=true)
	public List<Date> getDataMissingDates(String vin, Date startDate, Date endDate);

	@Query("select count(*) from MachineWheelLoaderData com where com.day = ?1")
	public Long getWlsCountByYesterdate(Date startDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs,md.averageFuelConsumption) from MachineWheelLoaderData md where md.vin =?1  and (md.day between ?2 and ?3) and totalFuelUsedInLtrs is not null order by day")
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineWheelLoaderData md where md.vin =?1 and md.day=?2 and md.totalFuelUsedInLtrs>0")
	public List<FuelConsumption> getFuelConsumptionDetailsReport(String vin, Date reportStartDate);

	@Query("select md.totalFuelUsedInLtrs from MachineWheelLoaderData md where md.vin =?1 and md.day =?2")
	public Double getFuelConsumption(String vin, Date yesterday);

	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.GearUtilizationWLS(md.day,Coalesce(md.gear1FwdUtilization,0),Coalesce(md.gear1BkwdUtilization,0),Coalesce(md.gear2FwdUtilization,0),Coalesce(md.gear2BkwdUtilization,0),Coalesce(md.gear3FwdUtilization,0),Coalesce(md.gear3BkwdUtilization,0),Coalesce(md.gear4FwdUtilization,0),Coalesce(md.gear4BkwdUtilization,0),Coalesce(md.totalFuelUsedInLtrs,0),Coalesce(md.averageFuelConsumption,0)) from MachineWheelLoaderData md where md.vin =?1  and md.day between ?2 and ?3 order by day")
	public List<GearUtilizationWLS> getGearUtilizationData(String vin, Date startDate, Date endDate);
	
	
	@Query("select new com.jcb.livelinkappserver.api.response.visualization.report.GearUtilizationWLS(md.day,md.gear1FwdUtilization,md.gear1BkwdUtilization,md.gear2FwdUtilization,md.gear2BkwdUtilization,md.gear3FwdUtilization,md.gear3BkwdUtilization,md.gear4FwdUtilization,md.gear4BkwdUtilization,md.totalFuelUsedInLtrs,md.averageFuelConsumption,md.fuelUsedInLPBLtrs,md.fuelUsedInMPBLtrs,md.fuelUsedInHPBLtrs,md.fuelLoss) from MachineWheelLoaderData md where md.vin =?1  and md.day between ?2 and ?3 order by day")
	public List<GearUtilizationWLS> getGearUtilizationDataV3(String vin, Date startDate, Date endDate);
	

}*/
