package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class MachineListResponseV3 {

    private List<MachineResponseView> machines;
    private AlertCount alertCount;
    private Long machineCount;
    private Long notificationCount;

    @Override
    public String toString() {
        return "MachineListResponse [machines=" + machines + ", alertCount=" + alertCount + "]";
    }
}
