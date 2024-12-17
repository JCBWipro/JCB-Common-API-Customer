package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.entity.MachineBHLData;
import com.wipro.jcb.livelink.app.machines.service.reports.FuelConsumption;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */

@Component
public interface MachineBHLDataRepo extends CrudRepository<MachineBHLData, String> {
	
	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_wls_data md where md.vin =:vin and  (md.day between :reportStartDate and :reportEndDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);

	@Query(value = "select  count(*) from machine_bhl_data  where day=?2  and average_fuel_consumption >=?1", nativeQuery = true)
	public long getBhlAverageCountYesterday(Double range, Date yesterDay);

	@Query(value = "select  count(*) from machine_bhl_data  where day=?2  and total_fuel_used_in_ltrs >=?1", nativeQuery = true)
	public long getBhlFuelCountYesterday(Double range, Date yesterDay);
}
