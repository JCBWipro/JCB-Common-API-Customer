package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.Alert;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface AlertRepository extends PagingAndSortingRepository<Alert, String> {

    List<Alert> findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(String vin, boolean isCustomerVisible, boolean isOpen);
}
