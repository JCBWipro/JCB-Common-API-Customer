package com.wipro.jcb.livelink.app.usermachine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.jcb.livelink.app.usermachine.commonUtils.UserMachineCommonUtils;
import com.wipro.jcb.livelink.app.usermachine.dto.UserDetails;

@RestController
@RequestMapping(value = "/api/usermachines")
public class UserMachineController {
	
	/*
     * This End Point is used for the testing Purpose
     */
    @GetMapping
    public String getString(@RequestHeader("LoggedInUserRole") String userDetails) {
    	UserDetails userResponse = UserMachineCommonUtils.getUserDetails(userDetails);
        return "LoggedIn Role is:-" + userResponse.getRoleName() + " and UserName is:-" + userResponse.getUserName();
    }

}
