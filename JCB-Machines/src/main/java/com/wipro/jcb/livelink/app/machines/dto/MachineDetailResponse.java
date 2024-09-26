package com.wipro.jcb.livelink.app.machines.dto;

import com.wipro.jcb.livelink.app.machines.entity.MachineFuelConsumptionData;
import com.wipro.jcb.livelink.app.machines.entity.MachineUtilizationData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
public class MachineDetailResponse {
    @Schema(description = "Machine details", required = true)
    private MachineDetailData machine;
    @Schema(description = "List of alerts of provided machine", required = true)
    private List<AlertData> alert;
    @Schema(description = "List of machine utilization of provided machine", required = true)
    private List<MachineUtilizationData> utilization;
    @Schema(description = "List of machine fuel consumption of provided machine", required = true)
    private List<MachineFuelConsumptionData> fuel;
    @Schema(description = "List of machine performance of provided machine", required = true)
    private List<MachinePerformanceData> performance;

    private int serviceAlertCount=0;
    private int normalAlertCount=0;
    private int alertWithRedEventLevel=0;

    @Schema(description = "List of machine utilization of provided machine with missing dates", required = true)
    private List<MachineUtilizationData> updatedUtilizationList;
    @Schema(description = "List of machine fuel consumption of provided machine with missing dates", required = true)
    private List<MachineFuelConsumptionData> updatedFuelList;
    @Schema(description = "List of machine performance of provided machine with missing dates", required = true)
    private List<MachinePerformanceData> updatedPerformanceList;

    public MachineDetailResponse(MachineDetailData machine, List<AlertData> alert,
                                 List<MachineUtilizationData> utilization, List<MachineFuelConsumptionData> fuel,
                                 List<MachinePerformanceData> performance) {
        super();
        this.machine = machine;
        this.alert = alert;
        this.utilization = utilization;
        this.fuel = fuel;
        this.performance = performance;
    }

    public MachineDetailResponse() {
    }

    @Override
    public String toString() {
        return "MachineDetailResponse [machine=" + machine + ", alert=" + alert + ", utilization=" + utilization
                + ", fuel=" + fuel + ", performance=" + performance + ", serviceAlertCount=" + serviceAlertCount
                + ", normalAlertCount=" + normalAlertCount + ", alertWithRedEventLevel=" + alertWithRedEventLevel
                + ", updatedUtilizationList=" + updatedUtilizationList + ", updatedFuelList=" + updatedFuelList
                + ", updatedPerformanceList=" + updatedPerformanceList + "]";
    }


}