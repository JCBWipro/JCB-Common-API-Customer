package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:17-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@NoArgsConstructor
public class RdMachineResponse {

    private List<RdVinImeiResponse> machineList;

}
