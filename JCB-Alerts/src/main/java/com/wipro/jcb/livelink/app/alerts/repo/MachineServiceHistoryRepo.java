package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.entity.MachineServiceHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MachineServiceHistoryRepo extends CrudRepository<MachineServiceHistory, String> {

    @Async
    public <S extends MachineServiceHistory> Iterable<S> save(Iterable<S> entities);

    @Query(value = "SELECT job_card_number,service_done_at,service_done,comments,vin,created_at FROM machine_service_history sh where sh.vin =:vin ORDER BY sh.service_done_at DESC ", nativeQuery = true)
    List<MachineServiceHistory> getHistoryDetails(String vin);

    @Query("SELECT new com.wipro.jcb.livelink.app.alerts.entity.MachineServiceHistory(jobCardNumber,serviceDoneAt,serviceDone,comments,vin,createdAt) FROM MachineServiceHistory sh where sh.vin = ?1 AND sh.serviceDoneAt between ?2 and ?3 ORDER BY sh.serviceDoneAt DESC")
    List<MachineServiceHistory> findByVinAndServiceDateBetweenOrderByServiceDateDesc(String vin, Date date,
                                                                                            Date date2);
}
