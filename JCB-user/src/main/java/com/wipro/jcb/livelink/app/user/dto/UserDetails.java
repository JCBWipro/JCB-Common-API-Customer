package com.wipro.jcb.livelink.app.user.dto;

/**
 * This UserDetails DTO is used to Handle Role and Username
 */
public class UserDetails {

	private String userName;
	private String roleName;

	public UserDetails() {
	}

	public UserDetails(String userName, String roleName) {
		super();
		this.userName = userName;
		this.roleName = roleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "UserDetails [userName=" + userName + ", roleName=" + roleName + "]";
	}

}
