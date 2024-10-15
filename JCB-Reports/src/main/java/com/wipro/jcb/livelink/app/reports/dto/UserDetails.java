package com.wipro.jcb.livelink.app.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class is to Handle Response related to UserName and RoleName
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
	
	private String userName;
    private String roleName;

}
