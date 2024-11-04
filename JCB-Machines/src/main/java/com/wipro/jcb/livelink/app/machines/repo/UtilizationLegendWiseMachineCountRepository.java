package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.UtilizationLegendWiseMachineCount;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

@Repository
public interface UtilizationLegendWiseMachineCountRepository
		extends PagingAndSortingRepository<UtilizationLegendWiseMachineCount, String> {
	
	public UtilizationLegendWiseMachineCount findByUtilizationCategory(MachineUtilizationCategory utilizationCategory);

}
