package com.wipro.jcb.livelink.app.user.businessObject;

import com.wipro.jcb.livelink.app.user.entity.ContactEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetailsBO {
	
	private int role_id;
	private String error_msg;
	private String role_name;
	private String last_login_date;
	private int account_id;
	private ContactEntity contact;
	private int tenancyId;
	private boolean isSMS;
	private boolean isMap;
	private String loginId;
	private String first_name;
	private String last_name;
	private int Is_tenancy_admin;
	public String countryCode;
	public String primaryMobileNumber;
	public String language;
	public String timeZone;
	private String primaryEmailId;
	private int sysGeneratedPassword;
	private int tenancyAdminCount;

}
