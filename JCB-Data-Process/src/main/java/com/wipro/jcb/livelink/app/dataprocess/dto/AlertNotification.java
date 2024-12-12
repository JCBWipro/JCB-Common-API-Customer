package com.wipro.jcb.livelink.app.dataprocess.dto;

import lombok.*;

import java.util.Set;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertNotification implements PushNotification {

    private Boolean content_available;

    private AlertNotificationNotification notification;

    private AlertNotificationData data;

    private String priority;
    private Set<String> registration_ids;

}
