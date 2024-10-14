package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.MachineWheelLoaderData;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumption;
import com.wipro.jcb.livelink.app.reports.report.WeekelyBucketCount;
import com.wipro.jcb.livelink.app.reports.report.WeekelyDutyCycle;

@Repository
public interface MachineWheelLoaderDataRepository extends PagingAndSortingRepository<MachineWheelLoaderData, String> {
	
	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_wls_data md where md.vin =:vin  and (md.day between :reportStartDate and :reportEndDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);
	
	public List<MachineWheelLoaderData> findByVinAndDayBetweenOrderByDayAsc(String vin, Date startDate,Date endDate);
	
	@Query("select new com.wipro.jcb.livelink.app.reports.report.WeekelyBucketCount(m.day,Coalesce(m.numberOfBuckets,0)) from MachineWheelLoaderData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<WeekelyBucketCount> getBucketCountData(String vin,Date startDate, Date endDate);

	@Query("select new com.wipro.jcb.livelink.app.reports.report.WeekelyDutyCycle(m.day,Coalesce(m.cumulativeLoadedWeight,0)) from MachineWheelLoaderData m where ?1 = m.vin and m.day between ?2 and ?3 order by m.day asc")
	public List<WeekelyDutyCycle> getDutyCycleData(String vin,Date startDate, Date endDate);
	
}
