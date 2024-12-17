package com.wipro.jcb.livelink.app.alerts.service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-11-2024
 */
public interface EmailService {

    void sentRetryMail(String string, String message, int count, int retryMachineDataCount);

}
