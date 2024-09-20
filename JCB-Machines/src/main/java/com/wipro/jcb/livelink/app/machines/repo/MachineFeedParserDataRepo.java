package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineFeedParserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public interface MachineFeedParserDataRepo extends CrudRepository<MachineFeedParserData, String> {
	//@Override
	@Async
	public <S extends MachineFeedParserData> Iterable<S> save(Iterable<S> entities);

	@Override
	public <S extends MachineFeedParserData> S save(S entity);
	
	@Query("select m from MachineFeedParserData m where m.vin = ?1 and m.lastModifiedDate is not null")
	public MachineFeedParserData findByVin(String vin);
	
	/*@Query(value="SELECT mf.status_as_on_time, machin_user.user_id, machine.vin FROM machine_feedparser_data mf join machin_user ON mf.vin = machin_user.vin "
			+ "join machine on mf.vin=machine.vin join live_link_user u on machin_user.user_id = u.user_id WHERE machine.vin in ?1 and u.user_type = ?4 and "
			+ "mf.status_as_on_time NOT between ?2\\:\\:date+machine.start_time\\:\\:time and ?2\\:\\:date+end_time\\:\\:time and (start_time LIKE '%AM' OR "
			+ "start_time LIKE '%PM') AND (end_time LIKE '%AM' OR end_time LIKE '%PM') and mf.last_modified_date between ?2 and ?3 order by machine.vin", 
			nativeQuery = true)
	public List<TimeFenceMachines> findTimeFenceBreachingMachines(List<String> vins, Date startTime, Date endTime, int userType);
	*/
	
}