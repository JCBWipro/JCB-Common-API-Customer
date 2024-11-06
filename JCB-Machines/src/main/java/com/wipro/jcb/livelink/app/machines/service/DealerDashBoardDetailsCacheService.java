package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.dto.DealerDashboard;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:01-11-2024
 */
@Component
public interface DealerDashBoardDetailsCacheService {

    DealerDashboard setDealerDashboardDetailsByUsername(DealerDashboard custDistribution, String userName);

    DealerDashboard getDealerDashboardDetailsByUsername(String userName);

}
