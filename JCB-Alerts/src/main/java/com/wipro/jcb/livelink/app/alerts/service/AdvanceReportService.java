package com.wipro.jcb.livelink.app.alerts.service;

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
}
