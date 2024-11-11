package com.wipro.jcb.livelink.app.alerts.service;

import com.wipro.jcb.livelink.app.alerts.dto.NotificationListResponseDto;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationRemovedResponse;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */
@Component
public interface MachineService {

    NotificationListResponseDto getNotificationListByGroupingDate(String userName, Date startDate, Date endDate, String pageNumber, String pageSize);

    NotificationRemovedResponse readAllNotification(String userName);

    NotificationRemovedResponse readNotification(int id , String userName);
}
