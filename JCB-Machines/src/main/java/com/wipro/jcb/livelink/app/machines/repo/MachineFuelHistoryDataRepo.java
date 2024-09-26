package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineFuelHistory;
import com.wipro.jcb.livelink.app.machines.service.response.MachineFuelHistoryData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface MachineFuelHistoryDataRepo extends PagingAndSortingRepository <MachineFuelHistory, String> {

	@Query(value = "SELECT fueldata.date_time,fueldata.fuel_level from machinefuelhistorydata fueldata where ?1 = fueldata.vin And fueldata.date_time between ?2 and ?3 order by fueldata.date_time ASC",nativeQuery=true)
	List<MachineFuelHistoryData> getFuelDetails(String vin, Date startDate, Date endDate);
}
