package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*@Repository
public interface MachineBreakdownRepo extends JpaRepository<MachineBreakdown, String> {

	@Query("SELECT m FROM MachineBreakdown m where m.platform IN ?1")
	List<MachineBreakdown> getBreakdownPlatformIn(@Param("platformList") List<String> platformList);

	MachineBreakdown findByVin(String vin);


}*/
