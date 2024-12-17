package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class MachineBHLDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6037626787004212021L;
    Date day;
    String vinId;
}
