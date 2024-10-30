package com.wipro.jcb.livelink.app.machines.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * DealerCustomers is a data model class that encapsulates a list of customer information.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DealerCustomers {
    private List<CustomerInfo> customers;
}
