package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseFilter {
    @ApiModelProperty(value = "List of filter", example = "[2DX Super ecoXcellence,3DX Super ecoXcellence]", required = true)
    private List<Filter> filters;

}
