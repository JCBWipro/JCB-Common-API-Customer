package com.wipro.jcb.livelink.app.dataprocess.dto;

import lombok.*;

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
public class AlertNotificationNotification {

    private String body;

    private String title;

    private String sound = "default";
}

