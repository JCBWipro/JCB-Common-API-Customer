package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This CustomerInfo is to handle CustomerInfo related details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo {

	private String ownerName;
	private String phoneNumber;
	private String address;

}