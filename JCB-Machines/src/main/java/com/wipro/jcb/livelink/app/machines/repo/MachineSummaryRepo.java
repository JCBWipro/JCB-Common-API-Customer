package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Component
public interface MachineSummaryRepo extends CrudRepository<MachineSummary, String>{

	@Transactional
	@Modifying
	@Query(value="delete from MachineSummary m")
	public void deleteExistingMachinedata();
	
	@Transactional
	@Modifying
	@Query(value="insert into machine_summary(vin,fuel_level,image,latitude,longitude,location,model,platform,site,status_as_on_time,tag,thumbnail,total_machine_hours) select  vin,fuel_level,image,latitude,longitude,location,model,platform,site,status_as_on_time,tag,thumbnail,total_machine_hours from machine where status_as_on_time >= :statusAsOnTime",nativeQuery=true)
	public void addAllmachinedata(@Param("statusAsOnTime")Date statusAsOnTime);
	
	@Transactional
	@Modifying
	@Query(value="insert into machine_summary(vin,fuel_level,image,latitude,longitude,location,model,platform,site,status_as_on_time,tag,thumbnail,total_machine_hours) select  vin,fuel_level,image,latitude,longitude,location,model,platform,site,status_as_on_time,tag,thumbnail,total_machine_hours from machine",nativeQuery=true)
	public void addAllmachinedataTest();
	
	@Query("SELECT m FROM MachineSummary  m join m.machine.users u where ?2 = u.userName AND m.vin=?1")
	public MachineSummary findByVinAndUserName(String vin, String userName);
	
	@Query("SELECT m FROM MachineSummary  m join m.machine.users u where ?1 = u.userName")
	public List<MachineSummary> findByUserName( String userName);

	@Query("SELECT m FROM MachineSummary  m join m.machine.users u where u.userName = ?1 order by m.statusAsOnTime")
	public List<MachineSummary> findByUserName(String userName,Pageable pageable);

	
	
	/*@Query("SELECT count(m) FROM Machine m join m.users u where ?1 = u.userName and m.statusastime < ?2")
	public Long getCountByUsersUserName(String userName,Date date);*/
	
	/*@Query("SELECT count(m) FROM Machine m join m.users u where ?1 = u.userName")
	public Long getCountByUsersUserName(String userName);*/

	
}
