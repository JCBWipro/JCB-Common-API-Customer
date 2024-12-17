package com.wipro.jcb.livelink.app.machines.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/*@Component
public interface MachineLocationHistoryRepo extends CrudRepository<MachineLocationHistoryData, String> {

	
	@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.LocationHistory(mlh.latitude,mlh.longitude,to_char(date_time,'dd/mm/yyyy hh24:mi')) from MachineLocationHistoryData mlh where mlh.vin =?1 and mlh.dateTime between ?2 and ?3 order by mlh.dateTime ASC ")
	public Set<LocationHistory> getMachineLocationHistoryData(String vin, Date date,Date endDate);

	
	@Transactional
	@Modifying
	@Query("delete from MachineLocationHistoryData mlh where mlh.dateTime < ?1")
	public void deleteByDate( Date date);


	@Query("select count(*) from MachineLocationHistoryData mlh")
	public long countbydate();


	@Query(value="select * from MachineLocationHistoryData mlh where mlh.vin=?1 and date_time between ?2 and ?3 order by date_time asc limit 1",nativeQuery=true)
	public MachineLocationHistoryData getMachineFirstLocation(String vin, Date startDate, Date endDate);


	@Query(value="select * from MachineLocationHistoryData mlh where mlh.vin=?1 and date_time between ?2 and ?3 order by date_time desc limit 1",nativeQuery=true)
	public MachineLocationHistoryData getMachineLastLocation(String vin, Date startDate, Date endDate);


}*/
