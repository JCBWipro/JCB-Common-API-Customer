package com.wipro.jcb.livelink.app.user.service;

import com.wipro.jcb.livelink.app.user.repo.MachineGroupRepo;
import com.wipro.jcb.livelink.app.user.reponse.MachineGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class MachineGroupService {
    @Autowired
    private MachineGroupRepo machineGroupRepo;

    public List<MachineGroupResponse> getAllMachineGroups(String userName) {
        return machineGroupRepo.getMachineGroupList(userName);
    }

}
