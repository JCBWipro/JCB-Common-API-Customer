package com.wipro.jcb.livelink.app.reports.service;

import com.wipro.jcb.livelink.app.reports.entity.Machine;

public interface MachineResponseService {

    Machine getMachineDetails(String vin, String userName);

}