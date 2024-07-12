package com.wipro.jcb.livelink.app.auth.entity;

import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TenancyBridgeEntity {

    int parentId;
    int childId;
    int level;
    String bottomFlag;
    String topFlag;


}
