package com.wipro.jcb.livelink.app.machines.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Getter
@Setter
public class FuelHistoryDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = 7935098866382273748L;
    private String vinId;
    private Date dateTime;

}
