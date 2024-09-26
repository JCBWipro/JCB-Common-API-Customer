package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineFuelConsumptionData;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface MachineFuelConsumptionDataRepository extends CrudRepository<MachineFuelConsumptionData, String> {
	/**
	 * Find list of instance of the MachineFuelConsumptionData by day and vin
	 *
	 * @param day
	 *            is date for retrieving data where format is "yyyy-MM-dd"
	 * @param vin
	 *            is unique identity of machine for which data to be retrieved
	 * @return MachineFuelConsumptionData is instance of MachineFuelConsumptionData
	 */
	public MachineFuelConsumptionData findByDayAndVin(Date day, String vin);

	/*public List<MachineFuelConsumptionData> findDistinctByMachineAndDayBetweenOrderByDayAsc(Machine machine,
			Date startDate, Date endDate);*/
	public List<MachineFuelConsumptionData> findByVinAndDayBetweenOrderByDayAsc(String vin,
			Date startDate, Date endDate);


	@Query("SELECT m FROM MachineFuelConsumptionData m  join m.machine.users u where ?1 = u.userName ORDER BY m.day DESC")
	public List<MachineFuelConsumptionData> findByMachineInOrderByDayDescLimit1(String userName, Pageable pagable);

	public MachineFuelConsumptionData findTop1ByMachineInOrderByDayDesc(List<Machine> machines);

	/*@Async*/
	public <S extends MachineFuelConsumptionData> Iterable<S> save(Iterable<S> entities);

	@Override
	public <S extends MachineFuelConsumptionData> S save(S entity);

	@Query("SELECT m FROM MachineFuelConsumptionData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 ORDER BY m.day ASC")
	public List<MachineFuelConsumptionData> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList, Date startDate,
			Date endDate);
	
	
	@Transactional
	@Modifying
	@Query(value="delete from machinefuelconsumption_data m where m.day < :day ",nativeQuery=true)
	public void deletByDate(@Param("day")Date day);
	
	//@Query(value="SELECT machine_type FROM machinefuelconsumption_data where ?1 = vin_id limit 1", nativeQuery=true)
	@Query(value="SELECT machineType FROM microservices_db.machinefuelconsumptionData where vinId=:vin limit 1", nativeQuery=true)
	public String getMachineTypeByVin(String vin);

	@Query(value="SELECT COUNT(m.vin) FROM machinefuelconsumption_data m WHERE m.vin= :vin and m.day = :day and m.fuel_consumed= :fuelConsumed and m.fuel_level= :fuelLevel ",nativeQuery=true)
	public long getCountByVin(@Param("vin")String vin, @Param("day")Date day, @Param("fuelConsumed")Double fuelConsumed, @Param("fuelLevel")String fuelLevel);

	
}
