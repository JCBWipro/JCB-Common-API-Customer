package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.MachineTelehandlerData;
import com.wipro.jcb.livelink.app.reports.report.FuelConsumptionResponse;
import com.wipro.jcb.livelink.app.reports.report.MachinePowerBands;

@Repository
public interface MachineTelehandlerDataRepository extends PagingAndSortingRepository<MachineTelehandlerData, String> {
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.FuelConsumptionResponse(md.day,md.totalFuelUsedInLtrs) from MachineTelehandlerData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumptionResponse> getFuelConsumptionData(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.FuelConsumptionResponse(md.day,md.averageFuelConsumption) from MachineTelehandlerData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumptionResponse> getAverageFuelConsumption(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.MachinePowerBands(md.day,md.fuelUsedInLowPowerBandLtrs,md.fuelUsedInMediumPowerBandLtrs,md.fuelUsedInHighPowerBandLtrs,md.fuelUsedInIdlePowerBandLtrs) from MachineTelehandlerData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<MachinePowerBands> getFuelPowerBand(String vin, Date startDate, Date endDate);


}
