package com.wipro.jcb.livelink.app.dataprocess.commonUtils;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Data
public class FuelHistoryDataId implements Serializable {

    @Serial
    private static final long serialVersionUID = 3725304868871038840L;
    private String vinId;
    private Date dateTime;
}
