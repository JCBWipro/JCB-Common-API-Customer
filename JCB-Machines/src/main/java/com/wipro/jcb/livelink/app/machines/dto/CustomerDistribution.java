package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * CustomerDistribution is a data model class that represents the distribution of customers based on machine categories.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDistribution {
    @ApiModelProperty(value = "Machine count on particular given key", example = "30", required = true)
    private Long machineCount;
    @ApiModelProperty(value = "Contains customer info depend on machine category", example = "", required = true)
    private List<DistributionParams> distributionParams;
}
