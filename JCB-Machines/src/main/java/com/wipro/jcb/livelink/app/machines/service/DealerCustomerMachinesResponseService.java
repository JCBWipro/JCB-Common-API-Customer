package com.wipro.jcb.livelink.app.machines.service;

import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerMachinesV3;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;

@Component
public interface DealerCustomerMachinesResponseService {
	
	public CustomerMachinesV3 getMachinesWithCustomerV3(String userName, String distributer, String keyParam,
			String tabSeparator, String customerId, String filter, String search, Boolean skipReports, int pageNumber, int pageSIze) throws ProcessCustomError;

}
