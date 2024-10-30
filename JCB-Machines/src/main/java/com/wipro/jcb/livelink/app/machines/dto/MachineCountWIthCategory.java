package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 */

/**
 * MachineCountWIthCategory is a data model class that represents the count of machines categorized by a specific category.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MachineCountWIthCategory {
    @ApiModelProperty(value = "Machine count on particular given key", example = "30", required = true)
    private Integer machineCount;
    @ApiModelProperty(value = "category for separation", example = "Backhoe", required = true)
    private String category;
}
