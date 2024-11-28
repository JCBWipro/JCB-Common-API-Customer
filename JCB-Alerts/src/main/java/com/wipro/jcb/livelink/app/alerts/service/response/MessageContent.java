package com.wipro.jcb.livelink.app.alerts.service.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-11-2024
 */
@Setter
@Getter
public class MessageContent {

    private String token;
    private Notification notification;
    private Data data;

}
