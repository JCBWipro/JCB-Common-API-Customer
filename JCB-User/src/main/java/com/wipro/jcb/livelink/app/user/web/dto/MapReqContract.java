package com.wipro.jcb.livelink.app.user.web.dto;

import java.util.List;

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
public class MapReqContract {
	
	private String LoginId;
	private List<String> SerialNumberList;
	private List<String> AlertSeverityList;
	private List<Integer> AlertTypeIdList;
	private List<Integer> Landmark_IdList;
	private List<Integer> LandmarkCategory_IdList;
	
	private List<Integer> Tenancy_ID;
	private List<Integer> loginUserTenancyList;
	private List<Integer> machineGroupIdList;
	private List<Integer>  machineProfileIdList;
	private List<Integer> modelIdList;
	private boolean ownStock;

}
