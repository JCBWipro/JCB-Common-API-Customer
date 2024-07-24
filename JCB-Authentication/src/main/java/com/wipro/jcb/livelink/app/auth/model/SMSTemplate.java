package com.wipro.jcb.livelink.app.auth.model;

import lombok.Data;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-07-2024
 * project: JCB-New-Common-API
 */
@Data
public class SMSTemplate {

    List<String> to;
    List<String> msgBody;
    int assetEventId;
    String serialNumber;
    String transactionTime;
}
