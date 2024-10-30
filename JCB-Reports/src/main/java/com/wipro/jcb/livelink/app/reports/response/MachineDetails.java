package com.wipro.jcb.livelink.app.reports.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDetails {
	
	private String vin;
	private String model;
	private Date renewalDate;
	private String customerName;
	private String mobileNumber;
	private Date statusOn;
	private Date lastService;
	private Date nextService;
	private String serviceType;
	private String nextServiceStatus;
	private String platform;

}
