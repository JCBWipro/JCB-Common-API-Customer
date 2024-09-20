package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MachineListResponse {
    private List<MachineResponse> machines;
    private AlertCount alertCount;
    private Long machineCount;

    public List<MachineResponse> getMachines() {
        return machines;
    }

    public void setMachines(List<MachineResponse> machines) {
        this.machines = machines;
    }

    public AlertCount getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(AlertCount alertCount) {
        this.alertCount = alertCount;
    }

    public Long getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(Long machineCount) {
        this.machineCount = machineCount;
    }

    public MachineListResponse(List<MachineResponse> machines, AlertCount alertCount, Long machineCount) {
        super();
        this.machines = machines;
        this.alertCount = alertCount;
        this.machineCount = machineCount;
    }

    @Override
    public String toString() {
        return "MachineListResponse [machines=" + machines + ", alertCount=" + alertCount + "]";
    }
}
