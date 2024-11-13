package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.dto.NotificationListResponseDto;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationRemovedResponse;
import com.wipro.jcb.livelink.app.alerts.repo.NotificationDetailsRepo;
import com.wipro.jcb.livelink.app.alerts.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.alerts.service.MachineService;
import com.wipro.jcb.livelink.app.alerts.service.response.NotificationUnReadCountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;

/*
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */

/**
 *  Service implementation provides functionality to retrieve the unread count for specific user
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class AdvanceReportServiceImpl implements AdvanceReportService {

    @Autowired
    MachineService machineService;
    @Autowired
    NotificationDetailsRepo notificationDetailsRepo;

    @Override
    public NotificationListResponseDto getNotificationListByGroupingDate(String userName, Date startDate, Date endDate, String pageNumber, String pageSize) {
        return machineService.getNotificationListByGroupingDate(userName, startDate, endDate, pageNumber, pageSize);
    }

    //Retrieves the unread notification
    @Override
    public NotificationUnReadCountResponse unReadNotificationCount(String userName) {
        NotificationUnReadCountResponse response = new NotificationUnReadCountResponse();
        response.setUserId(userName);
        Integer count = 0;
        try {
            count = notificationDetailsRepo.getUnReadNotificationCount(userName);
            response.setCount(count);

        } catch (Exception e) {
            log.error("Exception occurred in unReadNotificationCount {}", e.getMessage());
            log.info("Exception occurred for Notification count API :{}Exception -{}", userName, e.getMessage());
        }
        return response;
    }

    @Override
    public NotificationRemovedResponse readNotification(int id, String userName) {
        return machineService.readNotification(id, userName);
    }

    @Override
    public NotificationRemovedResponse readAllNotification(String userName) {
        return machineService.readAllNotification(userName);
    }

    @Override
    public NotificationRemovedResponse deleteAllNotification(String userName) {
        return machineService.deleteAllAlertNotification(userName);
    }

    @Override
    public NotificationRemovedResponse deleteNotification(Integer id, String userName) {
        return machineService.deleteNotification(id, userName);
    }
}
