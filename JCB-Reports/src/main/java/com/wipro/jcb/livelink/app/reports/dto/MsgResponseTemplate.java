package com.wipro.jcb.livelink.app.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This Class Represents a Message Response Template
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgResponseTemplate {
    private String message;
    private boolean success;
}