package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.Alert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 */
@Component
public interface AlertRepository extends PagingAndSortingRepository<Alert, String> {

    List<Alert> findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(String vin, boolean isCustomerVisible, boolean isOpen);

    @Query("SELECT DISTINCT a.eventName FROM Alert a WHERE a.machine.vin IN ?1 AND a.isCustomerVisible = true AND lower(a.eventName) LIKE lower(concat(?2,'%'))")
    List<String> getByMachinesAndSuggestionEventName(List<String> vin, String search);

    @Query("SELECT DISTINCT a.eventLevel FROM Alert a WHERE a.machine.vin IN ?1 AND a.isCustomerVisible = true AND lower(a.eventLevel) LIKE lower(concat(?2,'%'))")
    List<String> getByMachinesAndSuggestionEventLevel(List<String> vin, String search);

    @Query("SELECT DISTINCT a.location FROM Alert a WHERE a.machine.vin IN ?1 AND a.isCustomerVisible = true AND lower(a.location) LIKE lower(concat(?2,'%'))")
    List<String> getByMachinesAndSuggestionLocation(List<String> vin, String search);
}
