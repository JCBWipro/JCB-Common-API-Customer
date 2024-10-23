package com.wipro.jcb.livelink.app.reports.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.MachineFeedParserData;

@Repository
public interface MachineFeedParserDataRepo extends CrudRepository<MachineFeedParserData, String>{
	
	@Query(value="SELECT * FROM machine_feedparser_data where vin=:vin and last_modified_date is not null", nativeQuery = true)
	public MachineFeedParserData findByVin(String vin);

}
