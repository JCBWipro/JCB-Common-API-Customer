package com.wipro.jcb.livelink.app.machines.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.UtilizationLegendWiseCustomerMachineCount;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;

@Repository
public interface UtilizationLegendWiseCustomerMachineCountRepository
		extends PagingAndSortingRepository<UtilizationLegendWiseCustomerMachineCount, String> {

	public List<UtilizationLegendWiseCustomerMachineCount> findByUtilizationCategoryOrderByMachineCountDescCustomerIdAsc(
			MachineUtilizationCategory utilizationCategory, Pageable pageable);

}
