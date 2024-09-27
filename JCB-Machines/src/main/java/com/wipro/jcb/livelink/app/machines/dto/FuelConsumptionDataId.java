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
public class FuelConsumptionDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3844277147297566079L;
    private Date day;
    private String vinId;
}
