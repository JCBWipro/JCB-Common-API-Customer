package com.wipro.jcb.livelink.app.user.web.controller;

import com.wipro.jcb.livelink.app.user.web.exception.NoMachineGroupFoundException;
import com.wipro.jcb.livelink.app.user.web.exception.UserNotFoundException;
import com.wipro.jcb.livelink.app.user.web.reponse.MachineGroupResponse;
import com.wipro.jcb.livelink.app.user.web.service.MachineGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class MachineGroupController {

    private static final Logger log = LoggerFactory.getLogger(MachineGroupController.class);

    @Autowired
    MachineGroupService machineGroupService;
    /*
       This End Point is used to fetch the MachineGroupsDetails with reference to the userName
     */
    @GetMapping("/getMachineGroupDetails/{userName}")
    public ResponseEntity<?> getAllMachineGroups(@PathVariable String userName) {
        try {
            List<MachineGroupResponse> machineGroups = machineGroupService.getAllMachineGroups(userName);
            return ResponseEntity.ok(machineGroups);
        } catch (UserNotFoundException e) {
            log.info("User not found in DB: {}", userName);
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid user detail in header."));
        } catch (NoMachineGroupFoundException e) {
            log.info("No machine groups found for user: {}", userName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "No machine groups found for this user."));
        } catch (Exception e) {
            log.error("Error fetching machine groups for user: {}", userName, e);
            return ResponseEntity.internalServerError().body(Collections.singletonMap("message", "Failed to fetch machine groups."));
        }
    }

}
