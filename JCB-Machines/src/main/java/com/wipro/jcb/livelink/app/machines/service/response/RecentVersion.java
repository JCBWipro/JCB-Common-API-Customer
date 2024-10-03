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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentVersion {

    @Schema(description = "1.3.0,1.4.0", required = true)
    private String android;
    @Schema(description = "1.3.0,1.4.0", required = true)
    private String ios;
}
