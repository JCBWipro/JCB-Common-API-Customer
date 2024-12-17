package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.entity.MachineCompactionCoachData;
import com.wipro.jcb.livelink.app.machines.service.reports.FuelConsumption;


@Component
public interface MachineCompactionCoachDataRepository extends PagingAndSortingRepository<MachineCompactionCoachData, String> {
	
	@Query(value = "SELECT md.day as date,md.total_fuel_used_in_ltrs as totalFuelUsed,md.average_fuel_consumption as fuelAverage from machine_compactor_data md where md.vin =:vin and (md.day between :startDate and :endDate) and total_fuel_used_in_ltrs is not null order by day", nativeQuery = true)
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date startDate, Date endDate);
	
}
