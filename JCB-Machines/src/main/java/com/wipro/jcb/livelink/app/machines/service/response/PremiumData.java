package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:05-10-2024
 * project: JCB-Common-API-Customer
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PremiumData {

    private String label;
    private String fieldName;
    private Boolean isActive;
}
