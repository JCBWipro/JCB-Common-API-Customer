package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
public class MachineDutyCycle {

    private Double attachment;
    private Double idling;
    private Double excavation;
    private Double loading;
    private Double roading;

    public MachineDutyCycle(Double attachment, Double idling, Double excavation, Double loading, Double roading) {
        super();
        this.attachment = attachment;
        this.idling = idling;
        this.excavation = excavation;
        this.loading = loading;
        this.roading = roading;
    }

}
