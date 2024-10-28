package com.wipro.jcb.livelink.app.reports.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineEnginestatusHistory;
import com.wipro.jcb.livelink.app.reports.response.DateValue;
import com.wipro.jcb.livelink.app.reports.response.IntegerValue;


@Component
public interface MachineEngineStatusHistoryDataRepo extends CrudRepository<MachineEnginestatusHistory, String> {

    @Query("SELECT new com.wipro.jcb.livelink.app.reports.response.DateValue(enginedata.dateTime) from MachineEnginestatusHistory enginedata where enginedata.vin=:vin And enginedata.dateTime between :startdate and :endDate order by enginedata.dateTime ASC")
    List<DateValue> getDateByVin(String vin, Date startdate, Date endDate);

    @Query("SELECT new com.wipro.jcb.livelink.app.reports.response.IntegerValue(CAST(enginedata.isEngineOn as int)) from MachineEnginestatusHistory enginedata where enginedata.vin=:vin And enginedata.dateTime between :startDate and :endDate order by enginedata.dateTime ASC")
    List<IntegerValue> getByVin(String vin, Date startDate, Date endDate);


}
