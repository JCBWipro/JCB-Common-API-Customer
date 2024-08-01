package com.wipro.jcb.livelink.app.user.reponse;

/**
 * This UserDetailsReponse is used to Handle Contact Related Response
 */
public interface UserDetailsReponse {
	
	public String getContactId();
	
	public String getFirstname();

	public String getLastname();

	public Integer getIsTenancyAdmin();
	
	public Integer getSysGeneratedPassword();

	public Integer getRoleId();

	public Integer getAccountId();

}
