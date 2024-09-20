package com.wipro.jcb.livelink.app.machines.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/*@Component
public interface MachineIntelliDigDataRepo extends PagingAndSortingRepository<MachineIntelliDigData, String> {
	
	public MachineIntelliDigData findByDayAndVin(Date date, String vin);
	
	public List<MachineIntelliDigData> findByVin(String vin);

	//@Override
	@Async
	public <S extends MachineIntelliDigData> S save(S entity);

	//@Override
	public <S extends MachineIntelliDigData> Iterable<S> save(Iterable<S> entities);
	
	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.DepthProfile(m.day, m.targetDepthBand1Perct, m.targetDepthBand2Perct, m.targetDepthBand3Perct) from MachineIntelliDigData m where ?1 = m.vinId and m.day between ?2 and ?3 order by m.day asc")
	public List<DepthProfile> findDepthProfile(String vin, Date startDate, Date endDate);
	
	@Query("select new com.jcb.livelinkappserver.api.response.intelli.report.ProfileUtilization(m.day, m.profileDepthUtilizationPerct, m.profileReachUtilizationPerct, m.profileSlopeUtilizationPerct) from MachineIntelliDigData m where ?1 = m.vinId and m.day between ?2 and ?3 order by m.day asc")
	public List<ProfileUtilization> findProfileUtilizationData(String vin, Date startDate, Date endDate);
	
	@Transactional
	@Modifying
	@Query("delete from MachineIntelliDigData intelli where intelli.day < ?1")
	public void deletByDay(Date date);

}*/
