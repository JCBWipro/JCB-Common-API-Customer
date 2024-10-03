package com.wipro.jcb.livelink.app.machines.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.response.MachineProfile;

/**
 * This MachineProfileService interface is to create Abstract methods for MachineProfile Implementation Service
 */
@Component
public interface MachineProfileService {
	public MachineProfile getMachineProfile(String userName, String vin) throws ProcessCustomError;
	
	String putMachineProfile(String userName, String vin, String operatorName, String phoneNumber, String hours,
			String workStart, String workEnd, String jcbCertified, String tag, String site, MultipartFile image)
			throws ProcessCustomError;
}