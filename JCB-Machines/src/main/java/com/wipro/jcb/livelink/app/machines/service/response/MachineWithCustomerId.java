package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineWithCustomerId {
    @Schema(description = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private Long machineCount;
    @Schema(description = "XXXXX", required = true)
    private String customerId;

}

