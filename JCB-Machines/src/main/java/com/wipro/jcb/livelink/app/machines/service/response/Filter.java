package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
public class Filter {
    @Schema(description = "filter attribute", example = "2DX Super ecoXcellence", required = true)
    String filter;

    public Filter(String filter) {
        super();
        this.filter = filter;
    }

}
