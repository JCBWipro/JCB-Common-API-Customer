package com.wipro.jcb.livelink.app.mob.auth.response;

/**
 * This UserResponse is to Handle and Map, Mobile Users related Data
 */
public interface UserResponse {
	
	String getUSER_ID();
	String getpassword();
	String getuserType();
	String getroleName();
	String getfirstName();
	String getlastName();
	String getemail();
	String getphoneNumber();
	String getimage();
	String getthumbnail();
	String getcountry();
	String getsmsLanguage();
	String gettimeZone();
	String getlanguage();

}
