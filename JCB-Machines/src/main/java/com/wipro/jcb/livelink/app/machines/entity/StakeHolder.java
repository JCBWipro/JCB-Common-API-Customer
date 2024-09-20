package com.wipro.jcb.livelink.app.machines.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
public class StakeHolder implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    Customer customer;
    Dealer dealer;
    Long count;

    public StakeHolder() {
    }

    @Override
    public String toString() {
        return "StakeHolder [customer=" + customer + ", dealer=" + dealer + ", count=" + count + "]";
    }

    public StakeHolder(Customer customer, Dealer dealer, Long count) {
        super();
        this.customer = customer;
        this.dealer = dealer;
        this.count = count;
    }
    public StakeHolder(Customer customer, Long count) {
        super();
        this.customer = customer;
        this.count = count;
    }
}
