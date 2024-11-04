package com.wipro.jcb.livelink.app.machines.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.entity.Alert;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform;

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
    
    @Query("SELECT count(DISTINCT a.vin) FROM Alert a join a.machine.users u where u.userName=:userName  AND a.isCustomerVisible = true AND a.eventLevel =:alertLevel AND a.isOpen=true AND a.machine.customer.id is not null AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service")
	public Long countMachinesByCriticalAlertsCustomer(String userName, EventLevel alertLevel);
    
    @Query("SELECT count (DISTINCT a.vin) FROM Alert a join a.machine.users u WHERE  a.vin NOT In (SELECT DISTINCT a.vin FROM Alert a join a.machine.users u where u.userName=:userName AND a.machine.renewalFlag = true AND a.isCustomerVisible = true AND a.eventLevel =:alertLevel AND true = a.isOpen AND a.machine.customer.id is not null) AND a.eventLevel =:alertLevel2 AND a.machine.customer.id is not null AND u.userName=:userName AND a.isOpen=true AND a.isCustomerVisible = true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service")
	public Long countMachinesByHighAlertsCustomer(String userName, EventLevel alertLevel, EventLevel alertLevel2);
    
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId( count(DISTINCT a.vin ) as counter, a.machine.customer.id ) FROM Alert a join a.machine.users u where u.userName=:userName  AND a.isCustomerVisible = true AND a.eventLevel=:alertLevel AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType != com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getMachinesByCriticalAlertsCustomer(String userName, EventLevel alertLevel, Pageable pageable);
    
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform( count( DISTINCT a.vin) as counter, a.machine.platform) FROM Alert a WHERE a.machine.vin In (SELECT mc.vin FROM Machine mc join mc.users u where u.userName=:userName) AND a.isCustomerVisible = true AND a.eventLevel =:alertLevel AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getAlertCountGroupByPlatform(String userName, EventLevel alertLevel);
    
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId( count(DISTINCT a.vin ) as counter, a.machine.customer.id ) FROM Alert a WHERE a.machine.vin In (SELECT mc.vin FROM Machine mc join mc.users u where u.userName=:userName) AND a.isCustomerVisible = true AND a.eventLevel =:eventLevel AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByCountWithCustomers(String userName, EventLevel eventLevel, Pageable pageable);
    
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId( count(DISTINCT a.vin ) as counter, a.machine.customer.id ) FROM Alert a WHERE a.machine.vin In (SELECT mc.vin FROM Machine mc join mc.users u where u.userName=:userName) AND a.isCustomerVisible = true AND a.eventLevel =:eventLevel AND a.machine.platform =:platform AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByCountWithCustomers(String userName, EventLevel eventLevel, String platform, Pageable pageable);
}
