package com.wipro.jcb.livelink.app.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.user.businessObject.TenancyBO;
import com.wipro.jcb.livelink.app.user.businessObject.UserDetailsBO;
import com.wipro.jcb.livelink.app.user.commonUtils.UserCommonUtils;
import com.wipro.jcb.livelink.app.user.dto.UserAuthenticationRespContract;
import com.wipro.jcb.livelink.app.user.entity.AccountContactMapping;
import com.wipro.jcb.livelink.app.user.repo.AccountContactMappingRepo;
import com.wipro.jcb.livelink.app.user.repo.AccountRepo;

@Service
public class UserAuthenticationResponseService {

	@Autowired
	private UserDetailsBOService userDetailsBOService;

	@Autowired
	private TenancyService tenancyService;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private AccountContactMappingRepo accountContactMappingRepo;

	public UserAuthenticationRespContract getTenancyDetails(String userName) {

		// Fetch userDetails like Contact and Role based on userName
		UserDetailsBO userDetails = userDetailsBOService.getUserDetails(userName);

		// Fetch Tenancy Details based on userName and accountId
		TenancyBO tenancy = tenancyService.getUserTenancy(userName, userDetails.getAccount_id());
		UserAuthenticationRespContract userAuthenticationResponse = new UserAuthenticationRespContract();

		if (userDetails.getContact().getLast_name() == null) {
			userAuthenticationResponse.setUser_name(userDetails.getContact().getFirst_name());
		} else {
			userAuthenticationResponse.setUser_name(
					userDetails.getContact().getFirst_name() + " " + userDetails.getContact().getLast_name());
		}
		String roleName = UserCommonUtils.getRolesByID(userDetails.getContact().getRole().getRole_id());

		// Fetch Account and Contact ID's based on userName
		AccountContactMapping reporesult = accountContactMappingRepo.findAccountAndContactID(userName);

		// Fetch countryName based on countryCode from country_codes table
		String countryName = accountRepo.findCountryNameByCode(reporesult.getContact().getCountrycode());

		// Starting Setting up Details into userAuthenticationResponse
		userAuthenticationResponse.setLoginId(userName);
		userAuthenticationResponse.setUser_name(userAuthenticationResponse.getUser_name() + "|" + countryName);
		userAuthenticationResponse.setRole_name(roleName);
		userAuthenticationResponse.setRoleId(userDetails.getContact().getRole().getRole_id());
		// Ending of Setting up Details into userAuthenticationResponse

		// Fetch TenancyList based on userAuthenticationResponse
		userAuthenticationResponse = tenancyService.getTenancyObj(tenancy, userAuthenticationResponse);
		return userAuthenticationResponse;

	}

}
