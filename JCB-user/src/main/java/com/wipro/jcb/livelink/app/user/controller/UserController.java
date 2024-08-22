package com.wipro.jcb.livelink.app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wipro.jcb.livelink.app.user.dto.UserAuthenticationRespContract;
import com.wipro.jcb.livelink.app.user.dto.UserDetails;
import com.wipro.jcb.livelink.app.user.service.UserAuthenticationResponseService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserAuthenticationResponseService userAuthenticationResponseService;

	/*
	 * This End Point is used for the testing Purpose
	 */
	@GetMapping
	public String getString(@RequestHeader("LoggedInUserRole") String userDetails) {
		Gson gson = new Gson();
		UserDetails userResponse = gson.fromJson(userDetails, UserDetails.class);
		return "LoggedIn Role is:-" + userResponse.getRoleName() + " and UserName is:-" + userResponse.getUserName();
	}

	/*
	 * This End Point is used to fetch Tenancy Details
	 */
	@GetMapping("/tenancyDetails/{userName}")
	public UserAuthenticationRespContract getTenancyDetails(@PathVariable String userName) {
		return userAuthenticationResponseService.getTenancyDetails(userName);

	}
}