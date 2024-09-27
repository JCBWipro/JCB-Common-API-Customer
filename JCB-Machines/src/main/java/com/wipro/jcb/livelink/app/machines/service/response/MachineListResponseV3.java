package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:19-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
public class MachineListResponseV3 {

    private List<MachineResponseView> machines;
    private AlertCount alertCount;
    private Long machineCount;
    private Long notificationCount;

    public MachineListResponseV3(List<MachineResponseView> machines, AlertCount alertCount, Long machineCount,
                                 Long notificationCount) {
        super();
        this.machines = machines;
        this.alertCount = alertCount;
        this.machineCount = machineCount;
        this.notificationCount = notificationCount;
    }

    @Override
    public String toString() {
        return "MachineListResponse [machines=" + machines + ", alertCount=" + alertCount + "]";
    }
}
