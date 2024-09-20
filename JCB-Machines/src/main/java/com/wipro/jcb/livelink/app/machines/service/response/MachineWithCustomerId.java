package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "Machine unique identifier", example = "PUNJD22CV0000IIII", required = true)
    private Long machineCount;
    @ApiModelProperty(value = "XXXXX", required = true)
    private String customerId;

}

