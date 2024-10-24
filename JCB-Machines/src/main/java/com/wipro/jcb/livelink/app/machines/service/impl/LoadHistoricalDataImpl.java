package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData;
import com.wipro.jcb.livelink.app.machines.repo.ServiceCallJsonRepository;
import com.wipro.jcb.livelink.app.machines.service.LoadHistoricalDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 */
@Slf4j
@Service
public class LoadHistoricalDataImpl implements LoadHistoricalDataService {
    @Autowired
    ServiceCallJsonRepository serviceCallJsonRepository;

    //Fetches service call data from the repository and processes it
    @Override
    public String fetchServiceCallData() {
        String jsonCartList = "";
        try {
            List<ServiceCallJsonData> ServiceCallDataList = serviceCallJsonRepository.getJsonData();
            ServiceCallDataList.get(0).getLabel().replaceAll("/", "");
            jsonCartList = ServiceCallDataList.get(0).getLabel();
        } catch (Exception e) {
            log.error("Error occurred in  fetchServiceCallData Service:{}", e.getMessage());
        }
        return jsonCartList;
    }
}
