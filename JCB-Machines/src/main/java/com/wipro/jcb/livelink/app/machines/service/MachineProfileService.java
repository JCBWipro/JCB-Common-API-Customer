package com.wipro.jcb.livelink.app.machines.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.response.MachineProfile;

@Component
public interface MachineProfileService {
	public MachineProfile getMachineProfile(String userName, String vin) throws ProcessCustomError;

	public String putMachineProfile(String userName, String vin, String operatorName, String phoneNumber, String hours,
			String workStart, String workEnd, String jcbCertified, String tag, String site, MultipartFile image)
			throws ProcessCustomError;

	public void removeMachineProfileImage(String userName, String string) throws ProcessCustomError;
}

