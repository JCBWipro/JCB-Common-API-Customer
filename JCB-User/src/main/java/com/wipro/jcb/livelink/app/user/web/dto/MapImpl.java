package com.wipro.jcb.livelink.app.user.web.dto;

import java.sql.Timestamp;

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
public class MapImpl {
	
	private String Nickname;
	private String TotalMachineHours;
	private Timestamp OperatingStartTime;
	private Timestamp OperatingEndTime;
	private String Latitude;
	private String Longitude;
	private Long ActiveAlert;
	private String EngineStatus;
	private String SerialNumber;
	private int ProfileCode;
	private String ProfileName;
	private String LastReportedTime;
	private String Severity;

}
