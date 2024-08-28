package com.wipro.jcb.livelink.app.user.controller;

import com.wipro.jcb.livelink.app.user.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.repo.ContactRepo;
import com.wipro.jcb.livelink.app.user.reponse.MachineGroupResponse;
import com.wipro.jcb.livelink.app.user.service.MachineGroupService;
import lombok.extern.java.Log;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class MachineGroupController {

    private static final Logger log = LoggerFactory.getLogger(MachineGroupController.class);

    @Autowired
    MachineGroupService machineGroupService;
    @Autowired
    ContactRepo contactRepo;

    /*
       This End Point is used to fetch the MachineGroupsDetails
     */
    @GetMapping("/getMachineGroupDetails/{userName}")
    public ResponseEntity<?> getAllMachineGroups(@PathVariable String userName) {
        ContactEntity contactEntity = contactRepo.findByUserContactId(userName);
        Map<String, Object> response = new HashMap<>();
        if (contactEntity == null) {
            log.info("User not found in DB");
            response.put("message", "Invalid user detail in header.");
            response.put("success", false);
            return ResponseEntity.badRequest().body(response); // 400 Bad Request
        }

        try {
            List<MachineGroupResponse> machineGroups = machineGroupService.getAllMachineGroups(userName);
            if (machineGroups.isEmpty()) {
                response.put("message", "No machine groups found for this user.");
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(machineGroups); // 200 OK

        } catch (Exception e) {
            log.error("Error fetching machine groups for user: {}", userName, e);
            response.put("message", "Failed to fetch machine groups.");
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response); // 500 Internal Server Error with message
        }
    }

}
