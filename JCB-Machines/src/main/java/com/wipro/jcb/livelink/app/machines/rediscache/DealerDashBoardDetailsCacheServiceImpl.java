package com.wipro.jcb.livelink.app.machines.rediscache;

import com.wipro.jcb.livelink.app.machines.dto.DealerDashboard;
import com.wipro.jcb.livelink.app.machines.service.DealerDashBoardDetailsCacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-11-2024
 */
@Service
@PropertySource("application.properties")
public class DealerDashBoardDetailsCacheServiceImpl implements DealerDashBoardDetailsCacheService {


    @Override
    @CachePut(value = "DealerDashboardDetails",key = "#userName")
    public DealerDashboard setDealerDashboardDetailsByUsername(DealerDashboard custDistribution,String userName) {
        return custDistribution;
    }

    @Override
    @Cacheable(value = "DealerDashboardDetails", key = "#userName")
    public DealerDashboard getDealerDashboardDetailsByUsername(String userName) {
            return new DealerDashboard();
        }

   /* @Override
    @CacheEvict(value = "DealerDashboardDetails", key = "#userName")
    public void removeDealerDashboardDetails(String userName) {

    }*/
}
