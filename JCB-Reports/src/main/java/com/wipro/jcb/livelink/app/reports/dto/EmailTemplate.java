package com.wipro.jcb.livelink.app.reports.dto;

import lombok.Data;

/**
 *  This Class Representing an Email Template.
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
