package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData;
import com.wipro.jcb.livelink.app.machines.repo.ServiceCallJsonRepository;
import com.wipro.jcb.livelink.app.machines.service.LoadHistoricalDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Service
public class LoadHistoricalDataImpl implements LoadHistoricalDataService {
    @Autowired
    ServiceCallJsonRepository serviceCallJsonRepository;

    @Override
    public List<ServiceCallJsonData> fetchServiceCallData() {
        List<ServiceCallJsonData> serviceCallDataList = new ArrayList<>();
        try {
            serviceCallDataList = serviceCallJsonRepository.getJsonData();
        } catch (Exception e) {
            log.error("Error occured in  fetchServiceCallData Service:{}", e.getMessage());
            e.printStackTrace();
        }
        return serviceCallDataList;
    }
}
