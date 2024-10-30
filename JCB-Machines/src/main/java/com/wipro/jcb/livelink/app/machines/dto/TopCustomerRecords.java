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
 * TopCustomerRecords is a data model class that represents the records of top customers, including machine counts and platform details.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopCustomerRecords {
    @ApiModelProperty(value = "Machine count for all machines count for customer", example = "30", required = true)
    private Integer machineCount = 0;
    @ApiModelProperty(value = "Details of top customer", example = "", required = true)
    private List<TopCustomer> customerInfo = new LinkedList<>();
    @ApiModelProperty(value = "Distinct platform of all machines of user ", example = "", required = true)
    private List<Platforms> platforms = new LinkedList<>();
}
