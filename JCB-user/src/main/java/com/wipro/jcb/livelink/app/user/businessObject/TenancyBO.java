package com.wipro.jcb.livelink.app.user.businessObject;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TenancyBO {
	
	private int tenancyId;
	private String tenancyName;
	private int parentTenancyId;
	private String parentTenancyName;
	private String createdBy;
	private String createdDate;
	private String operatingStartTime;
	private String operatingEndTime;
	private int size;
	private String tenancyCode;
	private String accountName;
	List<String> tenancyAdminList= new LinkedList<String>();
	TreeMap<String, TreeMap<Integer,String>> tenancyNameIDProxyUser = new TreeMap<String, TreeMap<Integer,String>>();

}
