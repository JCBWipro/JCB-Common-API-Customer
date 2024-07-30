package com.wipro.jcb.livelink.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PseudoListResponse {
	
	private String Account_ID;
	private String Account_Name;
	private String Account_Code;
	private String mapping_code;
	private String Tenancy_ID;
	private String Tenancy_Name;

}
