package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.constants.EventType;
import com.wipro.jcb.livelink.app.alerts.entity.Alert;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertCountByEventType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-10-2024
 */
@Component
public interface AlertRepository extends CrudRepository<Alert, Long> {

    List<Alert> findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(String vin, boolean isCustomerVisible, boolean isOpen);


    @Query("SELECT  new com.wipro.jcb.livelink.app.alerts.service.response.AlertCountByEventType(count(a.vin) as counter, a.eventType) " +
            "FROM Alert a join a.machine.users u where ?1 = u.userName AND ?2 = a.readFlag AND ?3 = a.isOpen And a.isCustomerVisible = true group by a.eventType")
    List<AlertCountByEventType> getAlertCountByEventType(String userName, Boolean readFlag, Boolean isOpen);

    @Query("SELECT  new com.wipro.jcb.livelink.app.alerts.service.response.AlertCountByEventType(count(a.vin) as counter, a.eventType) " +
            "FROM Alert a join a.machine.users u where ?1 = u.userName AND ?2 = a.isOpen And a.isCustomerVisible = true group by a.eventType")
    List<AlertCountByEventType> getAlertCountByEventType(String userName, Boolean isOpen);

    @Query("SELECT  new com.wipro.jcb.livelink.app.alerts.service.response.AlertCountByEventType(count(a.vin) as counter, a.eventType) " +
            "FROM Alert a join a.machine.users u where ?1 = u.userName AND ?2 = a.isOpen And lower(a.vin) LIKE lower(concat('%',?3)) And a.isCustomerVisible = true group by a.eventType")
    List<AlertCountByEventType> getAlertCountByEventTypeLikeVin(String userName, Boolean isOpen, String vin);

    @Query("SELECT a  FROM Alert a join a.machine.users u join a.machine m where ?1 = u.userName AND a.eventType = ?2 AND ( m.platform IN ?3 OR m.model IN ?3) AND a.isCustomerVisible = true AND a.isOpen=true order by a.readFlag ASC , a.eventGeneratedTime DESC")
    public List<Alert> getAlertsUsingFilter(String userName,EventType eventType,List<String> filters, Pageable pageable);

    @Query("SELECT a  FROM Alert a join a.machine.users u join a.machine m where ?1 = u.userName AND a.eventType = ?2 AND ( m.platform IN ?3 OR m.model IN ?3) AND a.isCustomerVisible = true AND a.isOpen=true order by a.isOpen DESC , a.eventGeneratedTime DESC")
    public List<Alert> getAlertsUsingFilterVTwo(String userName,EventType eventType,List<String> filters, Pageable pageable);

    @Query("SELECT a  FROM Alert a join a.machine.users u join a.machine m where ?1 = u.userName AND a.isCustomerVisible = true AND a.isOpen=true AND a.eventType = ?2 AND (a.machine.platform IN ?4 OR a.machine.model IN ?4) AND ( lower(a.vin) LIKE lower(concat('%', ?3,'%')) OR lower(a.eventName) LIKE lower(concat('%', ?3,'%')) OR lower(a.location) LIKE lower(concat('%', ?3,'%')) OR lower(a.machine.tag) LIKE lower(concat('%', ?3,'%'))) ORDER BY a.eventGeneratedTime DESC")
    public List<Alert> getAlertsUsingFilterSearch(String userName,EventType eventType,String search,List<String> filters, Pageable pageable);

    @Query("SELECT a  FROM Alert a join a.machine.users u join a.machine m where ?1 = u.userName AND a.isCustomerVisible = true AND a.isOpen=true AND a.eventType = ?2 AND ((lower(m.tag) LIKE lower(concat(?3,'%')) ) OR (lower(m.vin) LIKE lower(concat(?3,'%')) ))")
    public List<Alert> getAlertsUsingSearch(String userName,EventType eventType,String search, Pageable pageable);


    @Query("SELECT a  FROM Alert a join a.machine.users u where ?1 = u.userName AND a.isCustomerVisible = true AND a.isOpen=true AND a.eventType = ?2 order by a.readFlag ASC , a.eventGeneratedTime DESC")
    public List<Alert> findAlertByEventType(String userName,EventType eventType, Pageable pageable);

    @Query("SELECT a  FROM Alert a join a.machine.users u where ?1 = u.userName AND a.isCustomerVisible = true AND a.isOpen=true AND a.eventType = ?2 order by a.isOpen DESC , a.eventGeneratedTime DESC")
    public List<Alert> findAlertByEventTypeOrderByEventGeneratedTimeDesc(String userName,EventType eventType, Pageable pageable);
}