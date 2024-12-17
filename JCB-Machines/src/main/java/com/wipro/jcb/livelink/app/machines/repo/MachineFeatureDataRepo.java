package com.wipro.jcb.livelink.app.machines.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.MachineFeatureInfo;

@Repository
public interface MachineFeatureDataRepo extends PagingAndSortingRepository<MachineFeatureInfo, String> {

	@Query(value = "SELECT * from machine_feature_info where vin=:vin and flag=:b ORDER BY created_at desc", nativeQuery = true)
	public List<MachineFeatureInfo> findByVinAndFlagOrderByDayDescLimit1(String vin, boolean b, Pageable pagable);
	
	public List<MachineFeatureInfo> findByVinAndFlag(String vin, boolean flag);

}
