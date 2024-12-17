package com.wipro.jcb.livelink.app.reports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.repo.MachineRepository;
import com.wipro.jcb.livelink.app.reports.service.MachineResponseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@PropertySource("application.properties")
public class MachineResponseServiceImpl implements MachineResponseService {
	
	@Autowired
    MachineRepository machineRepository;
	
	@Override
    public Machine getMachineDetails(String vin, String userName) {
        Machine machine = null;
        try {
            machine = machineRepository.findByVinAndUserName(vin, userName);
        } catch (final Exception e) {
            e.printStackTrace();
            log.error("Failed to check vin with user {}", e.getMessage());
        }

        return machine;
    }
	
	
}
