package com.wipro.jcb.livelink.app.reports.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineUtilizationData;
import com.wipro.jcb.livelink.app.reports.report.AggregatedMachineUtilization;

@Component
public interface MachineUtilizationDataRepository extends CrudRepository<MachineUtilizationData, String> {
	
	@Query("SELECT new com.wipro.jcb.livelink.app.reports.report.AggregatedMachineUtilization(m.day as day ,sum(m.idleHours) as idleHours,sum(m.offHours) as offHours,sum(m.workingHours) as workingHours) FROM MachineUtilizationData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachineUtilization> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList, Date startDate, Date endDate);
	
	@Query(value="select coalesce(sum(working_hours+idle_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getEngineOnHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(off_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getEngineOffHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(working_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getUtilization(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(idle_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getIdleHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(off_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getOffHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(working_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getWorkingTimeAvg(@Param("vin") String userName,@Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(working_hours+idle_hours,0) from machineutilizationdata where vin= :vin and day = :day ",nativeQuery=true)
	public Double getWorkingHours(@Param("vin")String vin, @Param("day")Date day);
	
}
