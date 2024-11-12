package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.dto.NotificationDetailResponse;
import com.wipro.jcb.livelink.app.alerts.entity.NotificationDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */

/**
 * NotificationDetailsRepo interface for accessing NotificationDetails data from Db
 */
@Repository
public interface NotificationDetailsRepo extends CrudRepository<NotificationDetails, Integer> {

    @Query("SELECT new com.wipro.jcb.livelink.app.alerts.dto.NotificationDetailResponse(nd.id, nd.day, nd.type, nd.userId, nd.vin, nd.alertId, nd.alertTitle, nd.alertDesc, nd.alertTime, nd.createdAt, nd.updatedAt, nd.deletedAt) from NotificationDetails nd where nd.userId =?1 order by day desc, nd.createdAt desc")
    List<NotificationDetailResponse> getNotificationByUser(String userName, Pageable pageable);

    //count of unread notification for specific user
    @Query("SELECT count(*) FROM NotificationDetails nd where nd.userId =?1 and nd.flag = false")
    Integer getUnReadNotificationCount(String userName);

    /**
     * Marks all notifications for a specific user as read by setting the 'flag' to true.
     * @param userName The username of the user whose notifications should be marked as read.
     */
    @Modifying
    @Query("UPDATE NotificationDetails nd set nd.flag = true where nd.userId =?1")
    void readNotificationByUser(String userName);

    /**
     * Marks a specific notification as read by setting the 'flag' to true.
     * @param id The ID of the notification to be marked as read.
     */
    @Modifying
    @Query("UPDATE NotificationDetails nd set nd.flag = true where nd.id =?1")
    void readNotificationById(int id);
}
