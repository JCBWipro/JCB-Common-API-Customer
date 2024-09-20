package com.wipro.jcb.livelink.app.machines.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
public class MachinePerformanceDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8564378808953107809L;
    private Date day;
    private String vinId;
}
