package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.MachineGeofence;

@Repository
public interface MachineGeofenceRepository extends CrudRepository<MachineGeofence, String> {

}
