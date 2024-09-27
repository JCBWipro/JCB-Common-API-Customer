package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/*
@Component
public interface MachineFeatureDataRepo extends PagingAndSortingRepository<MachineFeatureInfo, String> {

	public MachineFeatureInfo findByVinAndType(String vin, String type);
	
	public List<MachineFeatureInfo> findByVinAndFlag(String vin, boolean flag);
	
	@Query("SELECT m.type from MachineFeatureInfo m where ?1 = m.vin and ?2 = m.flag")
	public List<String> getTypeByVinAndFlag(String vin, boolean flag);
	
	//@Override
	@Async
	public <S extends MachineFeatureInfo> S save(S entity);
	
	//@Override
	public <S extends MachineFeatureInfo> Iterable<S> save(Iterable<S> entities);

	@Query("SELECT m from MachineFeatureInfo m where ?1 = m.vin and ?2 = m.flag ORDER BY m.createdAt desc")
	public List<MachineFeatureInfo> findByVinAndFlagOrderByDayDescLimit1(String vin, boolean b,Pageable pagable);
}
*/
