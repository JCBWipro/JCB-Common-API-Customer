package com.wipro.jcb.livelink.app.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This UserAuthenticationRespContract DTO is used to Handle TenancyNames and other info Details as an response
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationRespContract {
	
	String user_name;
	String loginId;
	String role_name;
	int roleId;
	List<String> tenancyNameIDProxyUser ;

}
