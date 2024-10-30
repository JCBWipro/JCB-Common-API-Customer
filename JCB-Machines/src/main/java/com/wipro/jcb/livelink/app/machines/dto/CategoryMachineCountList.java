package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * CategoryMachineCountList is a data model class that represents a list of machine counts categorized by specific keys.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMachineCountList {
    @ApiModelProperty(value = "Machine count on perticular given key", example = "30", required = true)
    private Long machineCount = 0L;
    @ApiModelProperty(value = "Object", example = "All count list with category", required = true)
    private List<MachineCountWIthCategory> keys = new LinkedList<>();

}
