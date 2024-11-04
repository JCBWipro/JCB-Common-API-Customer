package com.wipro.jcb.livelink.app.machines.enums;

public enum CommunicationType {
	COMMUNICATING("COMMUNICATING"), NON_COMMUNICATING("NON_COMMUNICATING"), SUBSCRIBED_COUNT("SUBSCRIBED_COUNT"),
	ALL("ALL");

	CommunicationType(String name) {
		this.name = name;
	}

	private final String name;

	public String getName() {
		return name;
	}
}
