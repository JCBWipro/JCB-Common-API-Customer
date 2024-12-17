package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
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

    @Query("SELECT count(DISTINCT a.vin) FROM Alert a join a.machine.users u JOIN a.machine m where ?1 = u.userName  AND a.isCustomerVisible = true AND a.eventLevel = ?2 AND true = a.isOpen")
    Long countMachinesByCriticalAlerts(String userName, EventLevel alertLevel);

    @Query("SELECT count (DISTINCT a.vin) FROM Alert a join a.machine.users u JOIN a.machine m WHERE  a.vin NOT In (SELECT DISTINCT a.vin FROM Alert a join a.machine.users u where ?1 = u.userName AND a.eventLevel = ?2 AND true = a.isOpen) AND a.isCustomerVisible = true AND a.eventLevel = ?3 AND ?1 = u.userName AND true = a.isOpen")
    Long countMachinesByHighAlerts(String userName, EventLevel alertLevel, EventLevel alertLevel2);

}
