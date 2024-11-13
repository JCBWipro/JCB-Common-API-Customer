package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertUtilities;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationDetailResponse;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationList;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationListResponseDto;
import com.wipro.jcb.livelink.app.alerts.dto.NotificationRemovedResponse;
import com.wipro.jcb.livelink.app.alerts.entity.NotificationDetails;
import com.wipro.jcb.livelink.app.alerts.repo.NotificationDetailsRepo;
import com.wipro.jcb.livelink.app.alerts.service.MachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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
@PropertySource({"application.properties", "notification_messages.properties"})
public class MachineServiceImpl implements MachineService {

    @Value("${notification.read.success}")
    String notificationReadSuccessMessage;

    @Value("${notification.noread.found}")
    String noUnreadNotificationsMessage;

    @Value("${notification.read.single.success}")
    String notificationReadSingleSuccessMessage;

    @Value("${notification.already.read}")
    String notificationAlreadyReadMessage;

    @Value("${notification.id.notfound}")
    String notificationIdNotFoundMessage;

    @Value("${notification.delete.success}")
    String notificationDeleteSuccessMessage;

    @Value("${notification.notfound}")
    String notificationNotFound;

    @Value("${notification.delete.successid}")
    String notificationDeleteSuccessIdMessage;



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

    @Override
    public NotificationRemovedResponse readAllNotification(String userName) {
        NotificationRemovedResponse response = new NotificationRemovedResponse();
        try {
            log.info("Checking for all notifications as read for user: {}", userName);
            // Get the count of unread notifications before marking them as read
            int unreadCount = notificationDetailsRepo.getUnReadNotificationCount(userName);

            if (unreadCount > 0) {
                notificationDetailsRepo.readNotificationByUser(userName);
                response.setMessage(notificationReadSuccessMessage);
                log.debug("Successfully marked {} notifications as read for user: {}", unreadCount, userName);
            } else {
                String message = MessageFormat.format(noUnreadNotificationsMessage, userName);
                response.setMessage(message);
                log.info(message);
            }

        } catch (Exception e) {
            log.error("Error marking all notifications as read for user: {}", userName, e);
            response.setMessage("Failed to mark notifications as read");
        }
        return response;
    }


    @Override
    public NotificationRemovedResponse readNotification(int id, String userName) {
        NotificationRemovedResponse response = new NotificationRemovedResponse();
        try {
            log.info("Read notification details for the user {} and notification ID: {}", userName, id);

            // Check if the notification exists for the user
            Optional<NotificationDetails> existingNotification = notificationDetailsRepo.findById(id);
            if (existingNotification.isPresent() && existingNotification.get().getUserId().equals(userName)) {
                // Check if the notification is already read (assuming 'flag' indicates read status)
                if (!existingNotification.get().getFlag()) {
                    notificationDetailsRepo.readNotificationById(id);
                    String message = MessageFormat.format(notificationReadSingleSuccessMessage, userName, id);
                    response.setMessage(message);
                    log.debug(message);
                } else {
                    String message = MessageFormat.format(notificationAlreadyReadMessage, id);
                    response.setMessage(message);
                    log.info(message);
                }
            } else {
                String message = MessageFormat.format(notificationIdNotFoundMessage, userName);
                response.setMessage(message);
                log.warn(message);
            }
        } catch (Exception e) {
            log.error("Error reading notification: {}", e.getMessage(), e);
            response.setMessage("Failed to process notification read request");
        }
        return response;
    }

   //Delete AllAlertNotification for given User
   @Override
   public NotificationRemovedResponse deleteAllAlertNotification(String userName) {
       NotificationRemovedResponse response = new NotificationRemovedResponse();
       try {
           log.info("Attempting to delete all notifications for user: {}", userName);

           // Find notifications for the user
           List<NotificationDetails> userNotifications = notificationDetailsRepo.findByUserId(userName);

           if (!userNotifications.isEmpty()) {
               // Delete notifications if found
               notificationDetailsRepo.deleteAll(userNotifications);
               String message = MessageFormat.format(notificationDeleteSuccessMessage,userName);
               response.setMessage(message);
               log.debug(message);
           } else {
               // Handle the case where no notifications are found for the user
               String message = MessageFormat.format(notificationNotFound,userName);
               response.setMessage(message);
               log.info(message);
           }

       } catch (Exception e) {
           log.error("Error deleting notifications for user: {}", userName, e);
           response.setMessage("Failed to delete notifications for user: " + userName);
       }
       return response;
   }

    //Delete AlertNotification for given User
    @Override
    public NotificationRemovedResponse deleteNotification(Integer id, String userName) {
        NotificationRemovedResponse response = new NotificationRemovedResponse();
        try {
            Optional<NotificationDetails> existingNotification = notificationDetailsRepo.findById(id);
            if (existingNotification.isPresent() && existingNotification.get().getUserId().equals(userName)) {
                log.info("Delete notification details for the user {}", userName);
                notificationDetailsRepo.deleteById(id);
                String message = MessageFormat.format(notificationDeleteSuccessIdMessage,id);
                response.setMessage(message);
            }else {
                log.warn("ID not present in DB for the userID");
                String message = MessageFormat.format(notificationIdNotFoundMessage, userName);
                response.setMessage(message);
            }
        } catch (Exception e) {
            log.error("Error deleting notification: {}", e.getMessage(), e);
            response.setMessage("Failed to process notification delete request");
        }
        return response;
    }
}
