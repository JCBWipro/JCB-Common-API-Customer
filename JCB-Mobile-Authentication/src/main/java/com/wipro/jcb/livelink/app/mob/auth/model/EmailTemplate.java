package com.wipro.jcb.livelink.app.mob.auth.model;

import lombok.Data;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:29-07-2024
 * project: JCB-Common-API-Customer
 */
@Data
public class EmailTemplate {
    String to;
    String subject;
    String body;
    String fileToBeAttached;
    String serialNumber;
    String transactionTime;
}
