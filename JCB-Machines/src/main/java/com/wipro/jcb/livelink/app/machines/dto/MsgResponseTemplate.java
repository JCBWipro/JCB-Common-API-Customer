package com.wipro.jcb.livelink.app.machines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * this class representing a message response template
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgResponseTemplate {
    private String message;
    private boolean success;
}