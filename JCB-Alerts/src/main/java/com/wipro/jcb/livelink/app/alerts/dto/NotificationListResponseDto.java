package com.wipro.jcb.livelink.app.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:07-11-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationListResponseDto {

    private List<NotificationList> notificationData;

}
