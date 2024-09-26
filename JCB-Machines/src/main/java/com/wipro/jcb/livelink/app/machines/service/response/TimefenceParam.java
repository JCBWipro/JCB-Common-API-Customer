package com.wipro.jcb.livelink.app.machines.service.response;

public class TimefenceParam {
	String startTime = "08:00 AM";
	String endTime = "08:00 PM";

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public TimefenceParam(String startTime, String endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public TimefenceParam() {
		super();
	}
	@Override
	public String toString() {
		return "TimefenceParam [startTime=" + startTime + ", endTime=" + endTime + "]";
	}

	
}

