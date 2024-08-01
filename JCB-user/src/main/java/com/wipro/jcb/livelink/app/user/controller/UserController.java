package com.wipro.jcb.livelink.app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wipro.jcb.livelink.app.user.businessObject.TenancyBO;
import com.wipro.jcb.livelink.app.user.businessObject.UserDetailsBO;
import com.wipro.jcb.livelink.app.user.dto.UserAuthenticationRespContract;
import com.wipro.jcb.livelink.app.user.dto.UserDetails;
import com.wipro.jcb.livelink.app.user.entity.AccountContactMapping;
import com.wipro.jcb.livelink.app.user.repo.AccountContactMappingRepo;
import com.wipro.jcb.livelink.app.user.repo.AccountRepo;
import com.wipro.jcb.livelink.app.user.service.TenancyService;
import com.wipro.jcb.livelink.app.user.service.UserDetailsBOService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserDetailsBOService userDetailsBOService;
	
	@Autowired
	private TenancyService tenancyService;
	
	@Autowired
	private AccountRepo accountRepo;
	
	@Autowired
	private AccountContactMappingRepo accountContactMappingRepo;

    /*
		This End Point is used for the testing Purpose
	*/
	@GetMapping
	public String getString(@RequestHeader("LoggedInUserRole") String userDetails) {
		Gson gson = new Gson();
		UserDetails userResponse = gson.fromJson(userDetails, UserDetails.class);
		return "LoggedIn Role is:-"+userResponse.getRoleName()+" and UserName is:-"+userResponse.getUserName();
	}
	
	/*
		This End Point is used to fetch Tenancy Details
	*/
	@GetMapping("/tenancyDetails/{userName}")
	public UserAuthenticationRespContract getTenancyDetails(@PathVariable String userName) {
		
		//Fetch userDetails like Contact and Role based on userName
		UserDetailsBO userDetails = userDetailsBOService.getUserDetails(userName);
		
		//Fetch Tenancy Details based on userName and accountId
		TenancyBO tenancy = tenancyService.getUserTenancy(userName, userDetails.getAccount_id());
		UserAuthenticationRespContract userAuthenticationResponse = new UserAuthenticationRespContract();
		
		if(userDetails.getContact().getLast_name()==null) {
			userAuthenticationResponse.setUser_name(userDetails.getContact().getFirst_name());
		} else {
			userAuthenticationResponse.setUser_name(userDetails.getContact().getFirst_name()+" "+userDetails.getContact().getLast_name());
		}
		
		//Fetch Account and Contact ID's based on userName
		AccountContactMapping reporesult = accountContactMappingRepo.findAccountAndContactID(userName);
		
		//Fetch countryName based on countryCode from country_codes table
		String countryName = accountRepo.findCountryNameByCode(reporesult.getContact().getCountrycode());
		
		//Starting Setting up Details into userAuthenticationResponse
		userAuthenticationResponse.setLoginId(userName);
		userAuthenticationResponse.setUser_name(userAuthenticationResponse.getUser_name()+"|"+countryName);
		String llAccountCode = tenancyService.getLLAccountCode(reporesult.getAccount().getAccountCode());
		
		if(llAccountCode==null && userDetails.getLast_login_date() != null){
			userAuthenticationResponse.setLast_login_date(userDetails.getLast_login_date()+"|NA");
		}
		else if(llAccountCode!=null && userDetails.getLast_login_date() == null){
			userAuthenticationResponse.setLast_login_date("|"+llAccountCode);
		}
		else if(llAccountCode!=null && userDetails.getLast_login_date() != null){
			userAuthenticationResponse.setLast_login_date(userDetails.getLast_login_date()+"|"+llAccountCode);
		}
		else{
			userAuthenticationResponse.setLast_login_date("|NA");
		}
		
		userAuthenticationResponse.setRole_name(userDetails.getContact().getRole().getRole_name());
		userAuthenticationResponse.setRoleId(userDetails.getContact().getRole().getRole_id());
		userAuthenticationResponse.setIsTenancyAdmin(userDetails.getContact().getIs_tenancy_admin());
		userAuthenticationResponse.setSysGeneratedPassword(userDetails.getContact().getSysGeneratedPassword());
		//Ending of Setting up Details into userAuthenticationResponse
		
		//Fetch TenancyList based on userAuthenticationResponse
		userAuthenticationResponse = tenancyService.getTenancyObj(tenancy, userAuthenticationResponse);
		return userAuthenticationResponse;
	}
}