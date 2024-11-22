package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.jcb.livelink.app.machines.entity.MachineGeofence;

@Repository
public interface MachineGeofenceRepository extends CrudRepository<MachineGeofence, String> {
	
	MachineGeofence findByVin(String vin);
	
	@Transactional
	@Modifying
	@Query("delete from MachineGeofence a where a.landmarkId =:landmarkId")
	void deleteByLandmarkId(Integer landmarkId);

}
