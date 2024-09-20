package com.wipro.jcb.livelink.app.machines.commonUtils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
public class DoubleValue implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 8445175318692900690L;
    Double val;

    public DoubleValue(Double val) {
        super();
        this.val = val;
    }

    public DoubleValue() {
        super();
    }

}
