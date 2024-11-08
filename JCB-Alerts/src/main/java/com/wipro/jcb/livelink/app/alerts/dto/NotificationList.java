package com.wipro.jcb.livelink.app.alerts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
* Author: Rituraj Azad
* User: RI20474447
* Date:07-11-2024
*/

@Setter
@Getter
public class NotificationList {

    private Date date;

    List<NotificationDetailResponse> notificationList;

}
