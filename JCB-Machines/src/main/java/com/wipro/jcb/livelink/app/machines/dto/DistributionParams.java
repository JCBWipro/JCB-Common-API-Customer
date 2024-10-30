package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 */

/**
 * DistributionParams is a data model class that represents the parameters for distributing machine counts by category.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DistributionParams {
    @ApiModelProperty(value = "Machine count on particular given key", example = "30", required = true)
    private Long machineCount = 0L;
    @ApiModelProperty(value = "category for separation", example = "Backhoe", required = true)
    private String category;
    @ApiModelProperty(value = "Information about customer", example = "Backhoe", required = true)
    private List<CustomerInfo> customerInfo = new ArrayList<>();
}
