package com.wipro.jcb.livelink.app.user.web.reponse;

/**
 * This UserDetailsReponse is used to Handle Contact Related Response
 */
public interface UserDetailsReponse {
	
	String getContactId();
	
	String getFirstname();

	String getLastname();

	Integer getIsTenancyAdmin();
	
	Integer getSysGeneratedPassword();

	Integer getRoleId();

	Integer getAccountId();

}
