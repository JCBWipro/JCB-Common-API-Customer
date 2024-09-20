package com.wipro.jcb.livelink.app.machines.service.reports;

import com.wipro.jcb.livelink.app.machines.service.response.StdMachineImagesResponse;
import lombok.*;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardMachineBaseResponse {

    List<StdMachineImagesResponse> data;

}
