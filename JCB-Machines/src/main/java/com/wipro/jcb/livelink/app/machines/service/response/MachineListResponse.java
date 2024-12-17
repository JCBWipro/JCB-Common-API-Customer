package com.wipro.jcb.livelink.app.machines.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MachineListResponse {
    private List<MachineResponse> machines;
    private AlertCount alertCount;
    private Long machineCount;

    @Override
    public String toString() {
        return "MachineListResponse [machines=" + machines + ", alertCount=" + alertCount + "]";
    }
}
