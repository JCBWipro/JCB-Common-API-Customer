package com.wipro.jcb.livelink.app.machines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This UserDetails DTO is used to Handle Role and Username
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
	
	private String userName;
	private String roleName;

}
