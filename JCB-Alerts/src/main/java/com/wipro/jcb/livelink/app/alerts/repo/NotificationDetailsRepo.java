package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.dto.NotificationDetailResponse;
import com.wipro.jcb.livelink.app.alerts.entity.NotificationDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */
@Repository
public interface NotificationDetailsRepo extends CrudRepository<NotificationDetails, Integer> {

    @Query("SELECT new com.wipro.jcb.livelink.app.alerts.dto.NotificationDetailResponse(nd.id, nd.day, nd.type, nd.userId, nd.vin, nd.alertId, nd.alertTitle, nd.alertDesc, nd.alertTime, nd.createdAt, nd.updatedAt, nd.deletedAt) from NotificationDetails nd where nd.userId =?1 order by day desc, nd.createdAt desc")
    List<NotificationDetailResponse> getNotificationByUser(String userName, Pageable pageable);

}
