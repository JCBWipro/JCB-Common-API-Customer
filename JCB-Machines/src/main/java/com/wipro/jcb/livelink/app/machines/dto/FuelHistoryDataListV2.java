package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.commonUtils.DateValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.DoubleValue;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
public class FuelHistoryDataListV2 {
    @NotNull
    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern= AppServerConstants.DateFormat, timezone=AppServerConstants.timezone)
    public Date date;
    public List<DoubleValue> values= new LinkedList<>();
    public List<DateValue> timestamps =new LinkedList<>();

}
