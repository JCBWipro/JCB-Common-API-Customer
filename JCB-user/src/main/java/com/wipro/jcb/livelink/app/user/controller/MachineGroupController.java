package com.wipro.jcb.livelink.app.user.controller;

import com.wipro.jcb.livelink.app.user.reponse.MachineGroupResponse;
import com.wipro.jcb.livelink.app.user.service.MachineGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class MachineGroupController {
    @Autowired
    private MachineGroupService machineGroupService;
    @GetMapping("/getMachineGroupDetails/{userName}")
    public List<MachineGroupResponse> getAllMachineGroups(@PathVariable String userName) {
        return machineGroupService.getAllMachineGroups(userName) ;
    }

}
