package com.wipro.jcb.livelink.app.user.web.dto;

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
public class MapRespContract {
	
	private String Nickname;
	private String OperatingStartTime;
	private String OperatingEndTime;
	private String Latitude;
	private String SerialNumber;
	private String Longitude;
	private Long ActiveAlert;
	private String EngineStatus;
	private int ProfileCode;
	private String ProfileName;
	private String LastReportedTime;
	private String Severity;
	private String TotalMachineHours;

}
