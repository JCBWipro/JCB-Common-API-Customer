package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseLL {
	private String firstName;
	private String lastName;
	private String orgName;
	private String roleName;
	private String tokenExpiryDate;
	private String tokenID;
	private String tokenIssuedDate;
	private String emailId;
	private String mobileNumber;
	private String loginId;
	private String countrycode;
	private String sys_gen_password;
	private String secretQuestion;
	private int machineCount;
	private String language;

}
