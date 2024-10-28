package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachinePerformanceData;
import com.wipro.jcb.livelink.app.reports.report.AggregatedMachinePerformance;
import com.wipro.jcb.livelink.app.reports.response.MachinePowerBand;

@Component
public interface MachinePerformanceDataRepository extends CrudRepository<MachinePerformanceData, String> {
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.AggregatedMachinePerformance(m.day as day ,sum(m.powerBandHighInHours) as powerBandHighInHours,sum(m.powerBandLowInHours) as powerBandLowInHours,sum(m.powerBandMediumInHours) as powerBandMediumInHours) FROM MachinePerformanceData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachinePerformance> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList, Date startDate, Date endDate);
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.response.MachinePowerBand(coalesce(sum(m.powerBandHighInHours),0) as powerBandHighInHours,coalesce(sum(m.powerBandLowInHours),0) as powerBandLowInHours,coalesce(sum(m.powerBandMediumInHours),0) as powerBandMediumInHours) FROM MachinePerformanceData m where m.vin= ?1 AND m.day >= ?2 AND m.day <= ?3")
	public MachinePowerBand getPowerBand(String vin, Date day, Date day2);
}
