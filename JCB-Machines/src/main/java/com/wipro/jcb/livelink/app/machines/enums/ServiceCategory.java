package com.wipro.jcb.livelink.app.machines.enums;

public enum ServiceCategory {
	
	SERVICE_DUE("SERVICE_DUE"), SERVICE_OVERDUE("SERVICE_OVERDUE"), SERVICE_NORMAL("SERVICE_NORMAL"), NO_DATA_AVAILABLE(
			"NO_DATA_AVAILABLE");
	
	ServiceCategory(String name) {
		this.name = name;
	}

	private final String name;

	public String getName() {
		return name;
	}

}
