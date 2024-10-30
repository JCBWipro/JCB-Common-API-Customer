package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineFuelHistory;

@Component
public interface MachineFuelHistoryDataRepo extends PagingAndSortingRepository<MachineFuelHistory, String> {
	
	@Query(value="select * from machinefuelhistorydata where vin=:vin order by date_time desc limit 1", nativeQuery=true)
	public MachineFuelHistory getFuelStatusByVin(@Param("vin") String vin);
	
	@Query(value="select fuel_level from machinefuelhistorydata where vin=:vin and  date_time >= :startDate and date_time <= :endDate order by date_time desc limit 1", nativeQuery=true)
	public Double getFuelLevelByOneHour(@Param("vin") String vin, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query(value="select date_time from machinefuelhistorydata where vin=:vin and date_time >= :startDate and date_time <= :endDate order by date_time desc limit 1", nativeQuery=true)
	public Date getDateLevelByOneHour(@Param("vin") String vin, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
