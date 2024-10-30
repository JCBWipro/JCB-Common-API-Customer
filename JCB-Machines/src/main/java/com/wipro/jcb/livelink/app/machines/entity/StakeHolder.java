package com.wipro.jcb.livelink.app.machines.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 */
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StakeHolder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    Customer customer;
    Dealer dealer;
    Long count;

    public StakeHolder(Customer customer, Long count) {
        super();
        this.customer = customer;
        this.count = count;
    }
}
