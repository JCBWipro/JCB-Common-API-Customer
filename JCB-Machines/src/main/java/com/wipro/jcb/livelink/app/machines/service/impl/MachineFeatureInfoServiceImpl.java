package com.wipro.jcb.livelink.app.machines.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.machines.repo.MachineFeatureDataRepo;
import com.wipro.jcb.livelink.app.machines.service.MachineFeatureInfoService;

@Service
public class MachineFeatureInfoServiceImpl implements MachineFeatureInfoService{
	
	@Autowired
	private MachineFeatureDataRepo machineFeatureDataRepo;

	@Override
	public Boolean isExist(String vin) {
		return !machineFeatureDataRepo.findByVinAndFlag(vin, true).isEmpty();
	}

}
