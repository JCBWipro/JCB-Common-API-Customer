package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.MachineLocationHistoryData;

@Repository
public interface MachineLocationHistoryDataRepo extends CrudRepository<MachineLocationHistoryData, String> {

	@Query(value = "select * from machinelocationhistorydata mlh where mlh.vin=:vin and date_time between :startDate and :endDate order by date_time asc limit 1", nativeQuery = true)
	public MachineLocationHistoryData getMachineFirstLocation(String vin, Date startDate, Date endDate);

	@Query(value = "select * from machinelocationhistorydata mlh where mlh.vin=:vin and date_time between :startDate and :endDate order by date_time desc limit 1", nativeQuery = true)
	public MachineLocationHistoryData getMachineLastLocation(String vin, Date startDate, Date endDate);

}
