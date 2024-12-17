package com.wipro.jcb.livelink.app.reports.response;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wipro.jcb.livelink.app.reports.constants.AppServerConstants;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

public class FuelHistoryDataListV2 {
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonProperty("date")
	@JsonFormat(pattern=AppServerConstants.DateFormat, timezone=AppServerConstants.timezone)
	public Date date;
	public List<DoubleValue> values= new LinkedList<>();
	public List<DateValue> timestamps =new LinkedList<>();

}
