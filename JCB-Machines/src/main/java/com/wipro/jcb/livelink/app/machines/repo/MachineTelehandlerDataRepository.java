package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.MachineTelehandlerData;
import com.wipro.jcb.livelink.app.machines.service.reports.FuelConsumption;

@Repository
public interface MachineTelehandlerDataRepository extends PagingAndSortingRepository<MachineTelehandlerData, String> {

	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_telehandler_data md where md.vin =:vin  and (md.day between :startDate and :endDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date startDate, Date endDate);

}
