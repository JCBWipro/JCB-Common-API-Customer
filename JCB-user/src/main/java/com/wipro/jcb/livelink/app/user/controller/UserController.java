package com.wipro.jcb.livelink.app.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public String getString(@RequestHeader("LoggedInUserRole") String role) {
    	if(role.equals("JCB Account")) {
    		return "User Verified!!!. LoggedIn User Role is "+role;
    	} else {
    		return "LoggedIn User Role is not Admin";
    	}
    }

}