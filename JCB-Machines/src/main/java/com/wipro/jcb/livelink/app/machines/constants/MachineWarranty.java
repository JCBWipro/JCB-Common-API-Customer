package com.wipro.jcb.livelink.app.machines.constants;

import lombok.Getter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-11-2024
 */
@Getter
public enum MachineWarranty {
    UNDER_WARRANTY("UNDER_WARRANTY"), WARRANTY_EXPIRED("WARRANTY_EXPIRED");

    MachineWarranty(String name) {
        this.name = name;
    }

    private final String name;
}
