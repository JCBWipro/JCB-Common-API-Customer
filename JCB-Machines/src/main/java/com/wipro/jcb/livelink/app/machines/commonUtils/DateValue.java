package com.wipro.jcb.livelink.app.machines.commonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DateValue {

    Date key;

    @Override
    public String toString() {
        return "DateValue [key=" + key + "]";
    }

}
