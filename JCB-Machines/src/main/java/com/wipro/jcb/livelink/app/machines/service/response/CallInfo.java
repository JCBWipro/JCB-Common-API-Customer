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
public class CallInfo {
     @Schema(description = "service area contact for help", example = "+918378989259", required = true)
    String service;
     @Schema(description = "parts area contact for help", example = "+918378989259", required = true)
    String parts;
     @Schema(description = "sales area contact for help", example = "+918378989259", required = true)
    String sales;
     @Schema(description = "Other area help contact", example = "+918378989259", required = true)
    String other;

}
