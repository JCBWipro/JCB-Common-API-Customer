package com.wipro.jcb.livelink.app.machines.repo;


import com.wipro.jcb.livelink.app.machines.commonUtils.DateValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.IntegerValue;
import com.wipro.jcb.livelink.app.machines.entity.MachineEnginestatusHistory;
import com.wipro.jcb.livelink.app.machines.service.response.MachineEngineStatusHistoryData;
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
public interface MachineEngineStatusHistoryDataRepo extends CrudRepository<MachineEnginestatusHistory, String> {

   /* //to get time stamp list for a machine
    @Query("SELECT new com.jcb.livelinkappserver.common.DateValue( enginedata.dateTime) from MachineEnginestatusHistory enginedata where ?1 = enginedata.vin And enginedata.dateTime >= ?2 order by enginedata.dateTime ASC")
    public List<DateValue> getDateByVin(String vin, Date date);

    @Query("SELECT new com.jcb.livelinkappserver.common.DateValue( enginedata.dateTime) from MachineEnginestatusHistory enginedata where ?1 = enginedata.vin And enginedata.dateTime between ?2 and ?3 order by enginedata.dateTime ASC")
    public List<DateValue> getDateByVin(String vin, Date startdate, Date endDate);

    //To get engine status list for a machine
    @Query("SELECT new com.jcb.livelinkappserver.common.IntegerValue(CAST(enginedata.isEngineOn as int)) from MachineEnginestatusHistory enginedata where ?1 = enginedata.vin And enginedata.dateTime >= ?2 order by enginedata.dateTime ASC")
    public List<IntegerValue> getByVin(String vin, Date date);

    @Query("SELECT new com.jcb.livelinkappserver.common.IntegerValue(CAST(enginedata.isEngineOn as int)) from MachineEnginestatusHistory enginedata where ?1 = enginedata.vin And enginedata.dateTime between ?2 and ?3 order by enginedata.dateTime ASC")
    public List<IntegerValue> getByVin(String vin, Date startDate, Date endDate);*/

    @Query(value = "SELECT * from machineenginestatushistorydata enginedata where ?1 = enginedata.vin And enginedata.date_time between ?2 and ?3 order by enginedata.date_time DESC limit 1", nativeQuery = true)
    public MachineEnginestatusHistory findByVinAndBetweenDate(String vin, Date startDate, Date endDate);

    //Delete old records
    @Transactional
    @Modifying
    @Query("delete from MachineEnginestatusHistory enginedata where enginedata.dateTime < ?1")
    public void deletByDate(Date date);

   /* //To get latest inserted engine status record for all machine
    @Query(value = "select  b.*  from machineenginestatushistorydata b inner join (select vin_id, max(date_time)  as d from machineenginestatushistorydata where date_time <= :endDateTime group by vin_id  ) t on b.vin_id=t.vin_id and b.date_time=t.d ORDER BY ?#{#pageable}",
            countQuery = "select count( b.*) from machineenginestatushistorydata b inner join (select vin_id, max(date_time)  as d from machineenginestatushistorydata where date_time <= :endDateTime group by vin_id  ) t on b.vin_id=t.vin_id and b.date_time=t.d",
            nativeQuery = true)
    public List<MachineEnginestatusHistory> getlastUpdatedEngineStatus(@Param("endDateTime") Date endDateTime, Pageable pageable);
*/
    @Async
    public <S extends MachineEnginestatusHistory> Iterable<S> save(Iterable<S> entities);


   /* @Query("SELECT e from MachineEnginestatusHistory e where ?1 = e.vin and date_time > ?2 order by e.dateTime desc")
    public List<MachineEnginestatusHistory> getLatestEngineStatus(String vin, Date date, Pageable pageable);*/

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.MachineEngineStatusHistoryData(CAST(enginedata.isEngineOn as int),enginedata.dateTime) from MachineEnginestatusHistory enginedata where ?1 = enginedata.vin And enginedata.dateTime between ?2 and ?3 order by enginedata.dateTime ASC",nativeQuery=true)
    public List<MachineEngineStatusHistoryData> getEngineDetails(String vin, Date startDate, Date endDate);


}
