package com.wipro.jcb.livelink.app.alerts.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:11/7/2024
 */

/**
 * NotificationUnReadCountResponse class represents the response structure for unread notification count
 */
@Setter
@Getter
@NoArgsConstructor
public class NotificationUnReadCountResponse {
    private String userId;
    private Integer count;
}