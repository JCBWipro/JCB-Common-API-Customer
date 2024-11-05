package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.DealerDashboardCustomer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-11-2024
 */
@Component
public interface DDashboardCustomerRepo extends CrudRepository<DealerDashboardCustomer, String> {

    @Query("SELECT d FROM DealerDashboardCustomer d where d.userName = ?1 order by machineCount desc")
    List<DealerDashboardCustomer> findAll(String userId, Pageable pageable);
}
