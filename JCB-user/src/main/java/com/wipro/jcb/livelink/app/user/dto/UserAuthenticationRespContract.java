package com.wipro.jcb.livelink.app.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationRespContract {
	
	String user_name;
	String loginId;
	String last_login_date;
	String role_name;
	int roleId;
	int isTenancyAdmin;
	int sysGeneratedPassword;
	boolean isSMS;
	boolean isMap;
	List<String> tenancyNameIDProxyUser ;

}
