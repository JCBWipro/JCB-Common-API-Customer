package com.wipro.jcb.livelink.app.alerts.service;

import com.wipro.jcb.livelink.app.alerts.dto.NotificationRemovedResponse;
import com.wipro.jcb.livelink.app.alerts.service.response.NotificationUnReadCountResponse;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationListResponseDto;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */

public interface AdvanceReportService {

    NotificationListResponseDto getNotificationListByGroupingDate(String userName, Date startDate, Date endDate, String pageNumber, String pageSize);

    /**
     * retrieves count for unread notification for specific user
     */
    NotificationUnReadCountResponse unReadNotificationCount(String userName);

    /**
     * Marks all notifications for the given user as read.
     * @param userName The username of the user.
     * @return A NotificationRemovedResponse indicating success or failure.
     */
    NotificationRemovedResponse readAllNotification(String userName);

    /**
     * Marks a specific notification as read for the given user.
     * @param id The ID of the notification to mark as read.
     * @param userName The username of the user.
     * @return A NotificationRemovedResponse indicating success or failure.
     */
    NotificationRemovedResponse readNotification(int id, String userName);

    /**
     * Delete all notification for the specified user .
     * @param userName The username of the user whose notifications are to be deleted .
     * @return A response indicating the result of the delete operation .
     */
    NotificationRemovedResponse deleteAllNotification(String userName);

    /**
     * Deletes a specific notification for the specified user by notification ID
     * @param id the ID of the notification to be deleted
     * @param userName The username of the user whose notification is to be deleted .
     * @return A response indicating the result of the delete operation .
     */
    NotificationRemovedResponse deleteNotification(Integer id, String userName);
}
