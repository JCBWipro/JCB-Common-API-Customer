package com.wipro.jcb.livelink.app.reports.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineUtilizationData;
import com.wipro.jcb.livelink.app.reports.report.AggregatedMachineUtilization;

@Component
public interface MachineUtilizationDataRepository extends CrudRepository<MachineUtilizationData, String> {
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.AggregatedMachineUtilization(m.day as day ,sum(m.idleHours) as idleHours,sum(m.offHours) as offHours,sum(m.workingHours) as workingHours) FROM MachineUtilizationData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachineUtilization> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList, Date startDate, Date endDate);
	
}
