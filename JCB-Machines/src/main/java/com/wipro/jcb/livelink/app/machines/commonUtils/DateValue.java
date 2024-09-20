package com.wipro.jcb.livelink.app.machines.commonUtils;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class DateValue {

    Date key;

    public DateValue(Date key) {
        super();
        this.key = key;
    }

    public DateValue() {
        super();
    }

    @Override
    public String toString() {
        return "DateValue [key=" + key + "]";
    }

    public Date getKey() {
        return key;
    }

    public void setKey(Date key) {
        this.key = key;
    }

}
