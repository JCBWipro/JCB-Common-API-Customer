package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.machines.commonUtils.DateValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.DoubleValue;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.NonNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/17/2024
 */

/**
 * This class represents the fuel history data for a specific date.
 */
public class FuelHistoryDataListV3 {
    @NonNull
    @Temporal(TemporalType.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern= AppServerConstants.DateFormat, timezone=AppServerConstants.timezone)
    public Date date;
    public List<DoubleValue> values= new LinkedList<>();
    public List<DateValue> timestamps =new LinkedList<>();
    public String message;
}
