package com.wipro.jcb.livelink.app.dataprocess.repo;

import com.wipro.jcb.livelink.app.dataprocess.entity.MachineFuelHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
public interface MachineFuelHistoryDataRepo extends PagingAndSortingRepository<MachineFuelHistory, String> {

    @Query(value="select fuel_level from machinefuelhistorydata  where vin =?1 order by date_time desc limit 1",nativeQuery=true)
    Double getFuelHistoryRecord(String vin);

}
