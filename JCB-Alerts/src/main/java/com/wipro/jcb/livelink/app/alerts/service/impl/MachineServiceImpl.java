package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertUtilities;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationDetailResponse;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationList;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationListResponseDto;
import com.wipro.jcb.livelink.app.alerts.repo.NotificationDetailsRepo;
import com.wipro.jcb.livelink.app.alerts.service.MachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */
@Slf4j
@Service
@Transactional
@PropertySource("application.properties")
public class MachineServiceImpl implements MachineService {

    @Autowired
    NotificationDetailsRepo notificationDetailsRepo;

    @Autowired
    AlertUtilities alertUtilities;

    @Override
    public NotificationListResponseDto getNotificationListByGroupingDate(String userName, Date startDate, Date endDate, String pageNumber, String pageSize) {
        log.info("Fetching notification list for user: {}, pageNumber: {}, pageSize: {}", userName, pageNumber, pageSize);
        NotificationListResponseDto notificationList = new NotificationListResponseDto();
        List<NotificationList> listNotification = new ArrayList<>();
        Map<Date, List<NotificationDetailResponse>> listGroupingByDate = new HashMap<>();

        try {
            List<NotificationDetailResponse> listFromDb = notificationDetailsRepo.getNotificationByUser(userName,
                    PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)));
            log.debug("Retrieved {} notifications from database", listFromDb.size());

            String pattern = "dd-MM-yyyy HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            for (NotificationDetailResponse notificationItem : listFromDb) {
                log.debug("Processing notification: {}", notificationItem.toString());
                Date date = alertUtilities.getPreviousDay(notificationItem.getDay());
                String formattedDate = simpleDateFormat.format(date);

                if (listGroupingByDate.containsKey(notificationItem.getDay())) {
                    log.debug("Notification already exists for date: {}", formattedDate);
                    notificationItem.setFlag(false);
                    List<NotificationDetailResponse> detailListItem = listGroupingByDate.get(notificationItem.getDay());
                    detailListItem.add(notificationItem);
                    listGroupingByDate.put(notificationItem.getDay(), detailListItem);
                } else {
                    log.debug("Adding new notification for date: {}", formattedDate);
                    notificationItem.setFlag(false);
                    List<NotificationDetailResponse> detailList = new ArrayList<>();
                    detailList.add(notificationItem);
                    listGroupingByDate.put(notificationItem.getDay(), detailList);
                }

                // Set headers and descriptions based on notification type
                if (notificationItem.getType().equals("Breakfast")) {
                    notificationItem.setBreakfastHeader("Breakfast");
                    notificationItem.setBreakfastDescription("Breakfast Report for the date " + formattedDate);
                } else if (notificationItem.getType().equals("Salesforce")) {
                    notificationItem.setSalesforceHeader("Customer Sales Feedback");
                    notificationItem.setSalesforceDescription(
                            "Please help us in reaching our goal of 100 % fantastic sales experience by answering a few questions about your recent purchase.");
                } else if (!notificationItem.getType().equals("Alert")) {
                    notificationItem.setNotificationDescription("New Push Notification");
                    notificationItem.setNotificationHeader("New Push Notification");
                    notificationItem.setVin(notificationItem.getVin());
                }
            }

            log.debug("Grouping notifications by date resulted in {} entries", listGroupingByDate.size());
            for (Map.Entry<Date, List<NotificationDetailResponse>> entry : listGroupingByDate.entrySet()) {
                NotificationList notifyItem = new NotificationList();
                notifyItem.setDate(entry.getKey());
                notifyItem.setNotificationList(entry.getValue());
                listNotification.add(notifyItem);
            }

            listNotification.sort(Comparator.comparing(NotificationList::getDate).reversed());
            notificationList.setNotificationData(listNotification);

            log.info("Successfully fetched notification list for user: {}", userName);
            return notificationList;

        } catch (Exception e) {
            log.error("Exception occurred while fetching notification list for user: {}", userName, e);
            throw new RuntimeException("Failed to fetch notification list", e);
        }
    }
}
