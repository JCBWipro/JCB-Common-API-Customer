package com.wipro.jcb.livelink.app.machines.service;

import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.response.MachineProfile;

/**
 * This MachineProfileService interface is to create Abstract methods for MachineProfile Implementation Service
 */
@Component
public interface MachineProfileService {
	public MachineProfile getMachineProfile(String userName, String vin) throws ProcessCustomError;
}

