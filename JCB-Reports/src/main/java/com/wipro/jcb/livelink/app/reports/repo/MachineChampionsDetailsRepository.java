package com.wipro.jcb.livelink.app.reports.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.MachineChampionsDetails;

@Repository
public interface MachineChampionsDetailsRepository extends CrudRepository<MachineChampionsDetails, String> {

	@Query(value="select champions_email from machine_champions_details where dealer_id =:dealerEmail", nativeQuery = true)
	String findByChampionsEmail(String dealerEmail);
}
