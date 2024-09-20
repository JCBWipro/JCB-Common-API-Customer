package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.service.reports.FuelConsumption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/*@Component
public interface MachineTelehandlerDataRepository extends PagingAndSortingRepository<MachineTelehandlerData, String> {
	
	public <S extends MachineTelehandlerData> Iterable<S> save(Iterable<S> entities);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineTelehandlerData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumption> getFuelConsumptionData(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.averageFuelConsumption) from MachineTelehandlerData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<FuelConsumption> getAverageFuelConsumption(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.MachinePowerBands(md.day,md.fuelUsedInLowPowerBandLtrs,md.fuelUsedInMediumPowerBandLtrs,md.fuelUsedInHighPowerBandLtrs,md.fuelUsedInIdlePowerBandLtrs) from MachineTelehandlerData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
	public List<MachinePowerBands> getFuelPowerBand(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs,md.averageFuelConsumption) from MachineTelehandlerData md where md.vin =?1  and (md.day between ?2 and ?3) and totalFuelUsedInLtrs is not null order by day")
	public List<FuelConsumption> getFuelConsumptionReport(String vin, Date startDate, Date endDate);

	@Query("select count(*) from MachineTelehandlerData tel where tel.day = ?1")
	public Long getTelehanderCountByYesterdate(Date yesterdate);

	@Query("select md.totalFuelUsedInLtrs from MachineTelehandlerData md where md.vin =?1 and md.day =?2")
	public Double getFuelConsumption(String vin, Date yesterday);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineTelehandlerData md where md.vin =?1 and md.day=?2 and md.totalFuelUsedInLtrs>0  ")
	public List<FuelConsumption> getFuelConsumptionDetailsReport(String vin, Date reportStartDate);

	@Transactional
	@Modifying
	@Query("delete from MachineTelehandlerData tel where tel.day < ?1 and tel.vin in (select vin from Machine where premiumFeature=false) ")
	public void deletByDay(Date date);

}*/
