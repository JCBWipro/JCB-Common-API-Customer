package com.wipro.jcb.livelink.app.dataprocess.constants;

import lombok.Getter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Getter
public enum EventType {

    SERVICE("Service"), HEALTH("Health"), SECURITY("Security"), UTILIZATION("Utilization"), LANDMARK("Landmark");
    private String name;

    EventType(final String name) {
        this.name = name;
    }

}
