package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineServiceHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MachineServiceHistoryRepo extends CrudRepository<MachineServiceHistory, String>{

	@Async
	public <S extends MachineServiceHistory> Iterable<S> save(Iterable<S> entities);

	@Query(value = "SELECT new  com.jcb.livelinkappserver.domain.MachineServiceHistory(jobCardNumber,serviceDoneAt,serviceDone,comments,vin,createdAt) FROM MachineServiceHistory sh where sh.vin = ?1 ORDER BY sh.serviceDoneAt DESC",nativeQuery=true)
	public List<MachineServiceHistory> getHistoryDetails(String vin);

	@Query(value = "SELECT new  com.jcb.livelinkappserver.domain.MachineServiceHistory(jobCardNumber,serviceDoneAt,serviceDone,comments,vin,createdAt) FROM MachineServiceHistory sh where sh.vin = ?1 AND sh.serviceDoneAt between ?2 and ?3 ORDER BY sh.serviceDoneAt DESC",nativeQuery=true)
	public List<MachineServiceHistory> findByVinAndServiceDateBetweenOrderByServiceDateDesc(String vin, Date date,
			Date date2);
}