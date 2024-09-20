package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/*public interface MachineGeofenceRepository extends CrudRepository<MachineGeofence, String> {

	MachineGeofence findByVin(String vin);

	@Transactional
	@Modifying
	@Query("delete from MachineGeofence a where a.landmarkId = ?1 ")
	void deleteByLandmarkId(Integer landmarkId);

	@Query("select a from MachineGeofence a where a.landmarkId = ?1 ")
	MachineGeofence findByLandmarkId(Integer landmarkId);

}*/
