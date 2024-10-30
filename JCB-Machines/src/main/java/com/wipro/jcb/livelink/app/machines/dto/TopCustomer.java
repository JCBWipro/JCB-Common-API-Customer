package com.wipro.jcb.livelink.app.machines.dto;

import lombok.*;

import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * TopCustomer is a data model class that represents the top customers based on machine counts.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopCustomer {
    private String customerName;
    private String customerId;
    private Integer machineCount;
    private List<MachineCountWIthCategory> platforms;

    public TopCustomer(String customerName, String customerId, Integer machineCount) {
        super();
        this.customerName = customerName;
        this.customerId = customerId;
        this.machineCount = machineCount;
    }
}
