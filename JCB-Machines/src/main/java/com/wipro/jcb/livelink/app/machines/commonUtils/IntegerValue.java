package com.wipro.jcb.livelink.app.machines.commonUtils;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */

@Setter
@Getter
public class IntegerValue implements Serializable {

    @Serial
    private static final long serialVersionUID = -6313377994788070591L;

    Integer val;


    public IntegerValue() {
        super();
    }

    public IntegerValue(Integer val) {
        super();
        this.val = val;
    }

    @Override
    public String toString() {
        return "IntegerValue [val=" + val + "]";
    }
}

