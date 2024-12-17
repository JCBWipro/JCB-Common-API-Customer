package com.wipro.jcb.livelink.app.machines.dto;

import lombok.Data;

/**
 *  this class representing an email template.
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
