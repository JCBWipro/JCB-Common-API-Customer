package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.repository.CrudRepository;

import com.wipro.jcb.livelink.app.machines.entity.MachineTimefence;


public interface MachineTimefenceRepository extends CrudRepository<MachineTimefence, String> {
	
	MachineTimefence findByVin(String vin);

}
