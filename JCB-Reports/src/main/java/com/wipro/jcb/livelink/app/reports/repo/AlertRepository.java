package com.wipro.jcb.livelink.app.reports.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.reports.entity.Alert;

@Component
public interface AlertRepository extends CrudRepository<Alert, Long> {
	
	@Query(value="select * from alert where vin= :vin and is_open=true and is_customer_visible =true order by event_level asc limit 3",nativeQuery=true)
	public List<Alert> getAlertDetailsByVin(@Param("vin")String vin);
	
	@Query(value="select count(*) from alert where vin= :vin and  is_open=true and is_customer_visible =true",nativeQuery=true)
	public Long getAlertCountVin(@Param("vin")String vin);

}