package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerDistribution;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;

public interface DealerDashboardResponseService {
	
	public CustomerDistribution getDealerDashboardDetails(String userName, String search, String distributor,
			String keyParam, int pageNumber, int pageSize) throws ProcessCustomError;

}
