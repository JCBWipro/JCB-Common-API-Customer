package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.dto.MachinePerformanceData;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface MachinePerformanceDataRepository extends CrudRepository<MachinePerformanceData, String> {
	/**
	 * Find list of instance of the MachinePerformanceData by machine and day
	 * between startDate and endDate also orderBy day
	 *
//	 * @param machine
	 *            is instance of the machine
	 * @param startDate
	 *            is start date for retrieving history data in "yyyy-MM-dd" format
	 * @param endDate
	 *            is end date for retrieving history data in "yyyy-MM-dd" format
	 * @return List<MachinePerformanceData> is list of instance of the
	 *         MachinePerformanceData
	 */
	/*
	 * public List<MachinePerformanceData>
	 * findByMachineAndDayBetweenOrderByDayDesc(Machine machine,Date startDate,Date
	 * endDate);
	 */ 
	/*public List<MachinePerformanceData> findDistinctByMachineAndDayBetweenOrderByDayAsc(Machine machine,
			Date startDate, Date endDate);*/
	 
	 public List<MachinePerformanceData> findByVinAndDayBetweenOrderByDayAsc(String vin,
				Date startDate, Date endDate);

	/**
	 * Find list of instance of the MachinePerformanceData by day and vin
	 *
	 * @param day
	 *            is date for retrieving data where format is "yyyy-MM-dd"
	 * @param vin
	 *            is unique identity of machine for which data to be retrieved
	 * @return MachinePerformanceData is instance of MachinePerformanceData
	 */
	public MachinePerformanceData findByDayAndVin(Date day, String vin);
	

	@Async
	public <S extends MachinePerformanceData> Iterable<S> save(Iterable<S> entities);

	@Override
	public <S extends MachinePerformanceData> S save(S entity);

	/*@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.AggregatedMachinePerformance(m.day as day ,sum(m.powerBandHighInHours) as powerBandHighInHours,sum(m.powerBandLowInHours) as powerBandLowInHours,sum(m.powerBandMediumInHours) as powerBandMediumInHours) FROM MachinePerformanceData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachinePerformance> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList,
			Date startDate, Date endDate);
	
	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.AggregatedMachinePerformance(m.day as day ,sum(m.powerBandHighInHours) as powerBandHighInHours,sum(m.powerBandLowInHours) as powerBandLowInHours,sum(m.powerBandMediumInHours) as powerBandMediumInHours) FROM MachinePerformanceData m join m.machine.users u where ?1 = u.userName AND m.day >= ?2 AND m.day <= ?3 GROUP by m.day ORDER BY m.day ASC")
	public List<AggregatedMachinePerformance> getByUserNameAndDayBetweenOrderByDayAsc(String userName,
			Date startDate, Date endDate);*/
	
	@Query("SELECT m FROM MachinePerformanceData m join m.machine.users u where ?1 = u.userName ORDER BY m.day DESC")
	public List<MachinePerformanceData> findByMachineInOrderByDayDescLimit1(String userName, Pageable pagable);
	
	@Transactional
	@Modifying
	@Query(value="delete from machineperformancedata m where m.day < :day ",nativeQuery=true)
	public void deletByDate(@Param("day")Date day);
	
	public Long countByDay(Date day);

	/*@Query("SELECT new com.jcb.livelinkappserver.api.response.MachinePowerBand (coalesce(sum(m.powerBandHighInHours),0) as powerBandHighInHours,coalesce(sum(m.powerBandLowInHours),0) as powerBandLowInHours,coalesce(sum(m.powerBandMediumInHours),0) as powerBandMediumInHours)  FROM MachinePerformanceData m where m.vin= ?1 AND m.day >= ?2 AND m.day <= ?3")
	public MachinePowerBand getPowerBand(String vin, Date day, Date day2);*/

	@Query(value="SELECT COUNT(m.vin) FROM machineperformancedata m WHERE m.vin= :vin and m.day = :day and m.power_band_low_in_hours= :low and m.power_band_medium_in_hours= :medium and m.power_band_high_in_hours= :high ",nativeQuery=true)
	public long getCountByVin(@Param("vin")String vin, @Param("day")Date day, @Param("low")Double low, @Param("medium")Double medium,@Param("high") Double high);
}
