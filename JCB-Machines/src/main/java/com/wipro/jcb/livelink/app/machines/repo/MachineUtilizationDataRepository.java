package com.wipro.jcb.livelink.app.machines.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineUtilizationData;
import com.wipro.jcb.livelink.app.machines.service.reports.MachineUtilization;

import jakarta.transaction.Transactional;

@Component
public interface MachineUtilizationDataRepository extends CrudRepository<MachineUtilizationData, String> {
	/**
	 * Find list of instance of the MachineUtilizationData by machine and day
	 * between startDate and endDate also orderBy day
	 *
//	 * @param machine
	 *            is instance of the machine
	 * @param startDate
	 *            is start date for retrieving history data in "yyyy-MM-dd" format
	 * @param endDate
	 *            is end date for retrieving history data in "yyyy-MM-dd" format
	 * @return List<MachineUtilizationData> is list of instance of the
	 *         MachineUtilizationData
	 */
	/*public List<MachineUtilizationData> findDistinctByMachineAndDayBetweenOrderByDayAsc(Machine machine, Date startDate,
			Date endDate);*/
	public List<MachineUtilizationData> findByVinAndDayBetweenOrderByDayAsc(String vin, Date startDate,
			Date endDate);
	/**
	 * Find list of instance of the MachineUtilizationData by day and vin
	 *
	 * @param day
	 *            is date for retrieving data where format is "yyyy-MM-dd"
	 * @param vin
	 *            is unique identity of machine for which data to be retrieved
	 * @return MachineUtilizationData is instance of MachineUtilizationData
	 */
	public MachineUtilizationData findByDayAndVin(Date day, String vin);


	@Async
	public <S extends MachineUtilizationData> Iterable<S> save(Iterable<S> entities);

	@Override
	public <S extends MachineUtilizationData> S save(S entity);

/*
	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.AggregatedMachineUtilization(m.day as day ,sum(m.idleHours) as idleHours,sum(m.offHours) as offHours,sum(m.workingHours) as workingHours) FROM MachineUtilizationData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachineUtilization> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList,
			Date startDate, Date endDate);
	
	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.AggregatedMachineUtilization(m.day as day ,sum(m.idleHours) as idleHours,sum(m.offHours) as offHours,sum(m.workingHours) as workingHours) FROM MachineUtilizationData m join m.machine.users u where ?1 = u.userName AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachineUtilization> getByUserNameAndDayBetweenOrderByDayAsc(String userName,Date startDate, Date endDate);
*/

	@Query("SELECT m.vin  FROM MachineUtilizationData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.vin HAVING (sum(m.workingHours) >= ?4 AND sum(m.workingHours) <= ?5 )")
	public List<String> findByMachineUsageForDuration(List<String> allVin, Date start, Date end, Double startRange,
			Double endRage);

	@Query("SELECT m.vin  FROM MachineUtilizationData m join m.machine.users u where ?1 = u.userName AND m.day >= ?2 AND m.day <= ?3 GROUP by m.vin HAVING (sum(m.workingHours) >= ?4 AND sum(m.workingHours) <= ?5 )")
	public List<String> findByMachineUsageForDuration(String userName, Date start, Date end, Double startRange,
			Double endRage);
	
	@Query("SELECT m.vin FROM MachineUtilizationData m join m.machine.users u where ?1 = u.userName  AND m.day >= ?2 AND m.day <= ?3 GROUP by m.vin HAVING (sum(m.workingHours) >= ?4 AND sum(m.workingHours) <= ?5 )")
	public List<String> findMachineByUtilizationUsageForDuration(String userName, Date start, Date end, Double startRange,
			Double endRage, Pageable pageable);
	
	@Query("SELECT m.vin  FROM MachineUtilizationData m join m.machine.users u where ?1 = u.userName  AND lower(m.machine.customer.id) = lower(?6) AND m.day >= ?2 AND m.day <= ?3 GROUP by m.vin HAVING (sum(m.workingHours) >= ?4 AND sum(m.workingHours) <= ?5 )")
	public List<String> findByMachineUsageForDuration(String userName, Date start, Date end, Double startRange,
			Double endRage, String customerName);

	@Query("SELECT m.vin  FROM MachineUtilizationData m where m.vin In ?1 AND m.platform =?6 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.vin HAVING (sum(m.workingHours) >= ?4 AND sum(m.workingHours) <= ?5 )")
	public List<String> findByMachineUsageForDuration(List<String> allVin, Date start, Date end, Double startRange,
			Double endRage, String platform);
	
	@Query("SELECT m.vin FROM MachineUtilizationData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3")
	public List<String> findByMachineNotUsedForDuration(List<String> allVin, Date start, Date end);
 
	@Query("SELECT m.vin FROM MachineUtilizationData m join m.machine.users u where ?1 = u.userName AND m.day >= ?2 AND m.day <= ?3")
	public List<String> findByMachineNotUsedForDuration(String userName, Date start, Date end);

	// improved
	@Query("SELECT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND m.vin NOT In(SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName  AND lower(mu.machine.customer.id) = lower(?4) AND mu.day >= ?2 AND mu.day <= ?3 )")
	public List<String> findByMachineNotUsedForDuration(String userName, Date start, Date end, String customerName);

	@Query("SELECT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND m.platform =?5 AND m.vin NOT In(SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName  AND lower(mu.machine.customer.id) = lower(?4) AND mu.day >= ?2 AND mu.day <= ?3 )")
	public List<String> findByMachineNotUsedForDuration(String userName, Date start, Date end, String customerName,
			String platform);

	@Query("SELECT m.vin FROM MachineUtilizationData m where m.vin In ?1 AND m.platform =?4 AND m.day >= ?2 AND m.day <= ?3")
	public List<String> findByMachineNotUsedForDuration(List<String> allVin, Date start, Date end, String platform);

	@Query("select distinct m.vin FROM MachineUtilizationData m where m.day between ?1 and ?2 group by m.vin having sum(m.workingHours) between 8 and 16")
	public List<String> getCountForModerateUsedData(Date start, Date end);

	@Query("select distinct m.vin FROM MachineUtilizationData m where m.day between ?1 and ?2 group by m.vin having sum(m.workingHours) < 8")
	public List<String> getCountForLesserUsedData(Date start, Date end);

	/*@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m where m.renewalFlag = true AND m.vin In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 GROUP by mu.vin HAVING (sum(mu.workingHours) >= ?4 AND sum(mu.workingHours) <= ?5 ) ) AND NOT m.customer.id = null GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getplatformsByMachineUsageForDuration(String userName, Date start, Date end,
																		   Double startRange, Double endRage);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND m.vin NOT In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 ) AND NOT m.customer.id = null GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getplatformsByUnusedMachineForDuration(String userName, Date start, Date end);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m where m.vin In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 GROUP by mu.vin HAVING (sum(mu.workingHours) >= ?4 AND sum(mu.workingHours) <= ?5 ) ) AND NOT m.customer.id = null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByMachineUsageForDuration(String userName, Date start, Date end,
																			 Double startRange, Double endRage, Pageable page);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.vin NOT In (SELECT DISTINCT(mu.vin) FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 ) AND NOT m.customer.id = null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByUnusedMachineForDuration(String userName, Date start, Date end,
			Pageable page);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m where  m.vin In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 AND ?6 = mu.platform  GROUP by mu.vin HAVING (sum(mu.workingHours) >= ?4 AND sum(mu.workingHours) <= ?5) ) AND NOT m.customer.id = null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByMachineUsageForDuration(String userName, Date start, Date end,
			Double startRange, Double endRage, String platform, Pageable page);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.vin NOT In (SELECT DISTINCT(mu.vin) FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 ) AND ?4= m.platform AND NOT m.customer.id = null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByUnusedMachineForDuration(String userName, Date start, Date end,
			String platform, Pageable page);
*/
/*	@Query("SELECT m FROM Machine m where m.vin In(SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where ?1 = u.userName AND mu.day >= ?2 AND mu.day <= ?3 GROUP by mu.vin HAVING (sum(mu.workingHours) >= ?4 AND sum(mu.workingHours) <= ?5 )) AND lower(m.customer.id) = lower(?3)")
	public List<Machine> getUsedMachines(String userName, Date start, Date end, Double startRange, Double endRage,
			String platform, String customerId, Pageable page);*/

	@Query("SELECT m FROM Machine m where m.vin In ?1")
	public List<Machine> getUnusedMachines(List<String> vinList, Pageable pageable);
	
	@Query("SELECT m FROM MachineUtilizationData m where m.vin In (SELECT mc.vin FROM Machine mc join mc.users u where ?1 = u.userName) ORDER BY m.day DESC")
	public List<MachineUtilizationData> findByMachineInOrderByDayDescLimit1(String userName, Pageable pagable);
	
	@Query("SELECT Count(m.vin) FROM MachineUtilizationData m  where m.vin IN  (select distinct m2.vin from MachineUtilizationData m2 where m2.day >= ?2 AND m2.day <= ?3 AND m2.vin In (SELECT mc.vin FROM Machine mc join mc.users u where ?1 = u.userName) GROUP by m2.vin HAVING (sum(m2.workingHours) >= ?4 AND sum(m2.workingHours) <= ?5 ))")
	public Long countByMachineUsageForDuration(String userName, Date start, Date end, Double startRange,
			Double endRange);
		
	@Transactional
	@Modifying
	@Query(value="delete from machineutilizationdata m where m.day < :day and m.vin_id in (select vin from machine where premium_feature=false)",nativeQuery=true)
	public void deletByDate(@Param("day")Date day);
		
	@Query("SELECT m FROM MachineUtilizationData  m join m.machine.users u where ?1 = u.userName and m.day=?2 and m.workingHours > 0.5 and m.machine.renewalFlag = true ")
	public List<MachineUtilizationData> findByUserName( String userName,Date  today);
	
	@Query("SELECT AVG(m.workingHours) FROM MachineUtilizationData  m where m.day=?1 and m.workingHours > 0 ")
	public Double getworkingHourAvg(Date  today);
	
	@Query("SELECT SUM(m.idleHours)/SUM(m.workingHours) FROM MachineUtilizationData  m where m.day=?1 and m.workingHours > 0")
	public Double getidelingAvg(Date  today);
	
	/*@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.MachineCountWIthCategory (count(m.vin) as counter,u.userName) From MachineUtilizationData  m join m.machine.users u where ?1 = u.userType and m.machine.renewalFlag = true and m.day=?2 and m.workingHours > 0.5 group by u.userName having count (m.vin) <=200 order by counter asc ")
	public List<MachineCountWIthCategory> getWorkingMachineCountbyUserName( UserType userType, Date  today,Pageable pageable);
*/
	
	/*@Query("SELECT count(m.vin) From MachineUtilizationData  m join m.machine.users u where ?1 = u.userType and m.day=?2 and m.workingHours > 0 and ?3= u.userName group by u.userName order by counter asc ")
	public long getWorkingMachineCount(UserType userType, Date  today);*/
	
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName")
	public Long getCountByUsersUserName(String userName);

	//@Query(value="select count(mvin) As ac from (select distinct(vin) as mvin from machin_user where user_id='prit05655' EXCEPT select distinct(machin_user.vin) as mvin from machineutilizationdata join machin_user ON machin_user.vin= machineutilizationdata.vin where user_id='prit05655') as derivedTable ",nativeQuery=true)
	@Query(value="select count(mvin) As ac from (select distinct(vin) as mvin from machin_user where user_id= :userName EXCEPT select distinct(machin_user.vin) as mvin from machineutilizationdata join machin_user ON machin_user.vin= machineutilizationdata.vin where user_id= :userName and day > :date ) as derivedTable ",nativeQuery=true)	
	public Long getNoDataCountForUtilzation(@Param("userName") String userName,@Param("date") Date date);
	 
	@Query(value="select count(ac) as cnt from (select distinct(m.vin) as ac from machineutilizationdata as m join machin_user ON machin_user.vin= m.vin where user_id= :userName and m.day > :start  group by m.vin having sum(m.working_hours) >=:max ) as derivedTable",nativeQuery=true)
	public Double getCountForHavilyUsedData(@Param("userName") String userName,@Param("start")Date start,@Param("max")Double  max);
	
	
	@Query(value="select count(ac) as cnt from (select distinct(m.vin) as ac from machineutilizationdata as m join machin_user ON machin_user.vin= m.vin where user_id= :userName and m.day > :start  group by m.vin having sum(m.working_hours) <= :min ) as derivedTable",nativeQuery=true)
	public Double opgetCountForlesserUsedData(@Param("userName") String userName,@Param("start")Date start,@Param("min")Double  min);
	
	
	@Query(value="select count(ac) as cnt from (select distinct(m.vin) as ac from machineutilizationdata as m join machin_user ON machin_user.vin= m.vin where user_id= :userName and m.day > :start group by m.vin having (sum(m.working_hours) >= :min) and (sum(m.working_hours) <= :max ))as derivedTable",nativeQuery=true)
	public Double getCountForModrateUsedData(@Param("userName") String userName,@Param("start")Date start,@Param("min")Double  min,@Param("max")Double max);
	
	
	@Query(value="select coalesce(sum(working_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getWorkingTimeAvg(@Param("vin") String userName,@Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(idle_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getHighIdleTimeAvg(@Param("vin") String userName,@Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(working_hours+idle_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getEngineOnHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(off_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getEngineOffHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(working_hours),0) from machineutilizationdata where vin= :vin ",nativeQuery=true)
	public Double getUtilizationTill(@Param("vin")String vin);
	
	@Query(value="select coalesce(sum(working_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getUtilization(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(idle_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getIdleHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(sum(off_hours),0) from machineutilizationdata where vin= :vin and day >= :day and day <= :day2",nativeQuery=true)
	public Double getOffHrs(@Param("vin")String vin, @Param("day")Date day,@Param("day2")Date  day2);
	
	@Query(value="select coalesce(working_hours+idle_hours,0) from machineutilizationdata where vin= :vin and day = :day ",nativeQuery=true)
	public Double getWorkingHours(@Param("vin")String vin, @Param("day")Date day);
	
	@Query(value="SELECT COUNT(m.vin) FROM machineutilizationdata m WHERE m.vin= :vin and day = :day and idle_hours= :idleHours and off_hours= :offHours and working_hours= :workingHours ",nativeQuery=true)
	public long getCountByVin(@Param("vin")String vin,@Param("day")Date day, @Param("idleHours")Double idleHours, @Param("offHours")Double offHours, @Param("workingHours")Double workingHours);
	
	@Query(value="SELECT day as date,working_hours as working,idle_hours as idle,off_hours as off FROM machineutilizationdata m where m.vin =:vin and m.day between :reportStartDate and :reportEndDate order by m.day",nativeQuery=true)
	public List<MachineUtilization> getUtilizationDetails(String vin, Date reportStartDate, Date reportEndDate);
	
	@Query("SELECT m FROM MachineUtilizationData  m join m.machine.users u where m.workingHours > 0.5 and m.machine.renewalFlag = true and ?1 = u.userName and m.day=?2  ORDER BY m.machine.statusAsOnTime")
	public List<MachineUtilizationData> findByUserNameWithPagesize(String userName, Date selectedDate,Pageable pageable);
	
	@Query("SELECT count(m) FROM MachineUtilizationData  m join m.machine.users u where m.workingHours > 0.5 and m.machine.renewalFlag = true and ?1 = u.userName and m.day=?2")
	public long getCountByUserName(String userName,Date selectedDate);
	
}
