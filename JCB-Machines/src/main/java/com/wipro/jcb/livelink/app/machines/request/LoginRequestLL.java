package com.wipro.jcb.livelink.app.machines.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestLL {
	
	private String userName;
	private String userType;
	private String password;

}
