package com.wipro.jcb.livelink.app.machines.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.UtilizationPlatformWiseMachineCount;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

@Repository
public interface UtilizationPlatformWiseMachineCountRepository
		extends PagingAndSortingRepository<UtilizationPlatformWiseMachineCount, String> {
	
	public List<UtilizationPlatformWiseMachineCount> findDistinctPlatformByUtilizationCategoryOrderByMachineCountDesc(MachineUtilizationCategory utilizationCategory);
	
	public UtilizationPlatformWiseMachineCount findByUtilizationCategoryAndPlatform(
			MachineUtilizationCategory utilizationCategory, String platform);
	
}
