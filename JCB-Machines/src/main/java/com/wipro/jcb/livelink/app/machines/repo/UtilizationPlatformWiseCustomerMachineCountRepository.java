package com.wipro.jcb.livelink.app.machines.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.UtilizationPlatformWiseCustomerMachineCount;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

@Repository
public interface UtilizationPlatformWiseCustomerMachineCountRepository extends PagingAndSortingRepository<UtilizationPlatformWiseCustomerMachineCount, String>{
	
	public List<UtilizationPlatformWiseCustomerMachineCount> findByUtilizationCategoryAndPlatformOrderByMachineCountDescCustomerIdAsc(
			MachineUtilizationCategory utilizationCategory, String platform, Pageable pageable);
	
}
