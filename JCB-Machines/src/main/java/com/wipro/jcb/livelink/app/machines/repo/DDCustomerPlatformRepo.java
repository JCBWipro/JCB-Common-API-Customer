package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.DealerDashboardCustomerPlatform;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-11-2024
 */
public interface DDCustomerPlatformRepo extends CrudRepository<DealerDashboardCustomerPlatform, String> {

    @Query("SELECT d FROM DealerDashboardCustomerPlatform d where d.customerId = ?1")
    List<DealerDashboardCustomerPlatform> findAll(String customerId);
}
