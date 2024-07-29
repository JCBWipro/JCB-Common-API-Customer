package com.wipro.jcb.livelink.app.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-07-2024
 * project: JCB-New-Common-API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgResponseTemplate {
    private String message;
    private boolean success;
}
