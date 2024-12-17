package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerDistribution;
import com.wipro.jcb.livelink.app.machines.dto.DealerDashboard;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import org.springframework.stereotype.Component;

@Component
public interface DealerDashboardResponseService {

    CustomerDistribution getDealerDashboardDetails(String userName, String search, String distributor,
                                                   String keyParam, int pageNumber, int pageSize) throws ProcessCustomError;

    DealerDashboard getDealerDashboardResponse(String userName, String search, String type) throws ProcessCustomError;


}
