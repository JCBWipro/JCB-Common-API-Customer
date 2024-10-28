package com.wipro.jcb.livelink.app.machines.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/17/2024
 */

@Setter
@Getter
@Data
public class ServiceCallData {
    private List<ServiceCallJsonData> servicecallrequest;


    public ServiceCallData() {
        super();
    }

}
