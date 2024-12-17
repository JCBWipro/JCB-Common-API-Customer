package com.wipro.jcb.livelink.app.reports.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.PremiumRequest;

@Repository
public interface PremiumRequestRepo extends CrudRepository<PremiumRequest, Integer>{
	
	PremiumRequest findByVinAndUserId(String vin, String userName);

}
