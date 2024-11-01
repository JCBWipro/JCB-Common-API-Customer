package com.wipro.jcb.livelink.app.machines.enums;

public enum MachineUtilizationCategory {
	
	MODERATED_USED("MODERATED_USED"), HEAVILY_USED("HEAVILY_USED"), LESSER_USED("LESSER_USED"), NO_DATA_AVAILABLE(
			"NO_DATA_AVAILABLE");
	MachineUtilizationCategory(String name) {
		this.name = name;
	}

	private final String name;

	public String getName() {
		return name;
	}

}
