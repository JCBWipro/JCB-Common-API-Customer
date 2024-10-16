package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.MachineFuelConsumptionData;

@Component
public interface MachineFuelConsumptionDataRepository extends CrudRepository<MachineFuelConsumptionData, String> {
	
	@Query("SELECT m FROM MachineFuelConsumptionData m where m.vin In ?1 AND m.day >= ?2 AND m.day <= ?3 ORDER BY m.day ASC")
	public List<MachineFuelConsumptionData> getByVinListAndDayBetweenOrderByDayAsc(List<String> vinList, Date startDate, Date endDate);
	
}
