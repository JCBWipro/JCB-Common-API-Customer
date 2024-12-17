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
public class AlertNotificationData {

    private String alertDesc;

    private String alertType;

    private String alertKey;

    private String title;
}
