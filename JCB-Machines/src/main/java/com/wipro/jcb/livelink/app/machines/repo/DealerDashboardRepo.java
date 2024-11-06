package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.DealerDashboardData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:01-11-2024
 */
@Component
public interface DealerDashboardRepo extends CrudRepository<DealerDashboardData, String> {

    //@Query("SELECT d FROM DealerDashboardData d WHERE d.userName = ?1 AND d.graphType = ?2 AND d.category = ?3")
    DealerDashboardData findByUserNameAndGraphTypeAndCategory(String userId, String graphType, String category);

    @Query("SELECT d FROM DealerDashboardData d where d.userName = ?1 order by d.machineCount desc")
    List<DealerDashboardData> findAll(String userId);

    @Query("SELECT d FROM DealerDashboardData d where d.graphType = ?1 AND d.userName = ?2 order by d.machineCount desc")
    List<DealerDashboardData> findByGraphTypeAndUserName(String type, String userId);
}
