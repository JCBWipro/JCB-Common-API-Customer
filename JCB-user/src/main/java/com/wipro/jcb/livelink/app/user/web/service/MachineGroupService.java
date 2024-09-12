package com.wipro.jcb.livelink.app.user.web.service;

import com.wipro.jcb.livelink.app.user.web.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.web.exception.NoMachineGroupFoundException;
import com.wipro.jcb.livelink.app.user.web.exception.UserNotFoundException;
import com.wipro.jcb.livelink.app.user.web.repo.ContactRepo;
import com.wipro.jcb.livelink.app.user.web.repo.MachineGroupRepo;
import com.wipro.jcb.livelink.app.user.web.reponse.MachineGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineGroupService {
    @Autowired
    ContactRepo contactRepo;
    @Autowired
    MachineGroupRepo machineGroupRepo;

    public List<MachineGroupResponse> getAllMachineGroups(String userName) {
        //checking the user is present in DB or not
        ContactEntity contactEntity = contactRepo.findByUserContactId(userName);
        if (contactEntity == null) {
            throw new UserNotFoundException("User not found in DB");
        }
        //fetching MachineGroupList
        List<MachineGroupResponse> machineGroups = machineGroupRepo.getMachineGroupList(userName);
        if (machineGroups.isEmpty()) {
            throw new NoMachineGroupFoundException("No machine groups found for this user");
        }
        return machineGroups;
    }

}
