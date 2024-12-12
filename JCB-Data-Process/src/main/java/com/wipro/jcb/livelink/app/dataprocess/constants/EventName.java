package com.wipro.jcb.livelink.app.dataprocess.constants;

import lombok.Getter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Getter
public enum EventName {
    TIME_FENCE_ALERT("Time Fence Alert"),
    GEO_FENCE_ALERT("Geo Fence Alert");
    private final String name;

    EventName(final String name) {
        this.name = name;
    }

}
