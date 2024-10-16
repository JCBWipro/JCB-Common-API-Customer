package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface LoadHistoricalDataService {
    List<ServiceCallJsonData> fetchServiceCallData();

}
