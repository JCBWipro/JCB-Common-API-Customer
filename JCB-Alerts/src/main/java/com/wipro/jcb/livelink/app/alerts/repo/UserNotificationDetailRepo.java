package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.entity.UserNotificationDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Component
public interface UserNotificationDetailRepo extends CrudRepository<UserNotificationDetail, String> {

    @Query("SELECT u FROM UserNotificationDetail u WHERE u.userName = :userName")
    UserNotificationDetail findByUserNameToken(String userName);

    @Query(value = "SELECT DISTINCT(n.push_notification_token) FROM user_notification_detail n " +
            "JOIN live_link_user lu ON lu.user_id=n.user_name " +
            "WHERE n.user_type ='Customer' AND n.os = 'Android' AND " +
            "CAST(REPLACE(lu.user_app_version, '.', '') AS UNSIGNED INTEGER) > 201 AND " + // Corrected CAST
            "n.user_name=?1 AND n.access_token=?2 ", nativeQuery = true)
    List<String> getAndroidFCMKeyForSalesforceUniqueUser(String userName, String token);

    @Query(value = "SELECT DISTINCT(n.push_notification_token) FROM user_notification_detail n  JOIN live_link_user lu ON lu.user_id=n.user_name WHERE n.user_type ='Customer' AND n.os = 'iOS' AND n.user_name=?1 AND n.access_token=?2 ",nativeQuery = true)
    List<String> getIosFCMKeyForSalesforce(String userName, String token);
}
