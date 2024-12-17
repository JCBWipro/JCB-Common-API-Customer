package com.wipro.jcb.livelink.app.machines.enums;

public enum LivelinkRenawal {
	RENEWAL_OVERDUE("RENEWAL_OVERDUE"), RENEWAL_APPROACHING("RENEWAL_APPROACHING"), RENEWAL_IMMEDIATE(
			"RENEWAL_IMMEDIATE"), RENEWAL_NO_DATA("RENEWAL_NO_DATA"), ALL("ALL");
	LivelinkRenawal(String name) {
		this.name = name;
	}

	private final String name;

	public String getName() {
		return name;
	}
}
