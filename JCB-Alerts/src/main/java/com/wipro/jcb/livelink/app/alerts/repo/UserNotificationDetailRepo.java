package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.entity.UserNotificationDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Component
public interface UserNotificationDetailRepo extends CrudRepository<UserNotificationDetail, String> {

    @Query("SELECT u FROM UserNotificationDetail u WHERE u.userName = :userName")
    UserNotificationDetail findByUserNameToken(String userName);
}
