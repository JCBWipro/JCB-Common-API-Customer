package com.wipro.jcb.livelink.app.machines.repo;


import com.wipro.jcb.livelink.app.machines.entity.MachineEnginestatusHistory;
import com.wipro.jcb.livelink.app.machines.service.response.MachineEngineStatusHistoryData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public interface MachineEngineStatusHistoryDataRepo extends CrudRepository<MachineEnginestatusHistory, String> {

    @Query(value = "SELECT * from machineenginestatushistorydata enginedata where ?1 = enginedata.vin And enginedata.date_time between ?2 and ?3 order by enginedata.date_time DESC limit 1", nativeQuery = true)
    public MachineEnginestatusHistory findByVinAndBetweenDate(String vin, Date startDate, Date endDate);

    //Delete old records
    @Transactional
    @Modifying
    @Query("delete from MachineEnginestatusHistory enginedata where enginedata.dateTime < ?1")
    public void deletByDate(Date date);

    @Async
    public <S extends MachineEnginestatusHistory> Iterable<S> save(Iterable<S> entities);

    @Query(value = "SELECT enginedata.date_time,enginedata.is_engine_on FROM machineenginestatushistorydata enginedata WHERE enginedata.vin = ?1 AND enginedata.date_time BETWEEN ?2 AND ?3 ORDER BY enginedata.date_time ASC", nativeQuery = true)
    List<MachineEngineStatusHistoryData> getEngineDetails(String vin, Date startDate, Date endDate);

    //to get time stamp list for a machine
    @Query(value = "SELECT enginedata.date_time FROM machineenginestatushistorydata enginedata WHERE enginedata.vin = ?1 AND enginedata.date_time BETWEEN ?2 AND ?3 ORDER BY enginedata.date_time ASC", nativeQuery = true)
    List<Date> getDateByVin(String vin, Date startdate, Date endDate);

    //To get engine status list for a machine
    @Query(value = "SELECT CAST(enginedata.is_engine_on AS SIGNED) " +
            "FROM machineenginestatushistorydata enginedata " +
            "WHERE enginedata.vin = ?1 AND enginedata.date_time BETWEEN ?2 AND ?3 " +
            "ORDER BY enginedata.date_time ASC", nativeQuery = true)
    List<Integer> getByVin(String vin, Date startDate, Date endDate);


}
