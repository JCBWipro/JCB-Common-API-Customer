package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wipro.jcb.livelink.app.machines.dto.MachineResponseV2;
import lombok.*;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MachineListResponseV2 {
    private List<MachineResponseV2> machines;
    private AlertCount alertCount;
    private Long machineCount;
    private Long notificationCount;

    @Override
    public String toString() {
        return "MachineListResponse [machines=" + machines + ", alertCount=" + alertCount + "]";
    }
}
