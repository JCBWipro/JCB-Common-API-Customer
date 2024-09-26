package com.wipro.jcb.livelink.app.machines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/27/2024
 * project: JCB-Common-API-Customer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgResponseTemplate {
    private String message;
    private boolean success;
}
