package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WidgetsResponse {

    @Schema(description = "navigation", required = true)
    private String standard;
    @Schema(description = "machinelocation", required = true)
    private String premium;
}
