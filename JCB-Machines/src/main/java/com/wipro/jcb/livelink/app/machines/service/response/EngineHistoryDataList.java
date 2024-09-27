package com.wipro.jcb.livelink.app.machines.service.response;

import com.wipro.jcb.livelink.app.machines.commonUtils.DateValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.IntegerValue;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
public class EngineHistoryDataList {
    public List<IntegerValue> values = new LinkedList<>();
    public List<DateValue> timestamps =new LinkedList<>();
}
