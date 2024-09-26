package com.wipro.jcb.livelink.app.machines.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * This UserDetails DTO is used to Handle Role and Username
 */
@Setter
@Getter
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

    @Override
	public String toString() {
		return "UserDetails [userName=" + userName + ", roleName=" + roleName + "]";
	}

}

