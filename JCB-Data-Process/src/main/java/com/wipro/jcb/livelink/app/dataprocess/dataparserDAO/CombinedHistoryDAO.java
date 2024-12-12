package com.wipro.jcb.livelink.app.dataprocess.dataparserDAO;

/*
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
import com.wipro.jcb.livelink.app.dataprocess.dto.*;
import com.wipro.jcb.livelink.app.dataprocess.service.CombinedHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class CombinedHistoryDAO {

    @Autowired
    CombinedHistoryService combinedHistoryService;

    public void addBatchMachineWithFuelConsume(List<MachineFuelConsumption> packets) {
        combinedHistoryService.addBatchMachineWithFuelConsume(packets);
    }

    public void addBatchMachineWithEngineStatus(List<MachineEngineStatus> packets) {
        combinedHistoryService.addBatchMachineWithEngineStatus(packets);
    }

    public void addTimefenceAlert(List<AlertData> alertData) {
        combinedHistoryService.addTimefenceAlert(alertData);
    }

    public void addGeofenceAlert(List<AlertData> alertData) {
        combinedHistoryService.addGeofenceAlert(alertData);
    }

    public void updateBatchMachineLocation(List<MachineLocation> packets) {
        combinedHistoryService.updateBatchMachineLocation(packets);
    }

    public void updateBatchMachineFeedData(List<MachineData> packets) {
        combinedHistoryService.updateBatchMachineFeedData(packets);
    }

    public void updateBatchMachineFeedDataHavingFuel(List<MachineData> packets) {
        combinedHistoryService.updateBatchMachineFeedDataHavingFuel(packets);
    }

    public void addBatchMachineLocationHistory(List<MachineLocationHistory> machineLocationHistory) {
        combinedHistoryService.addBatchMachineLocationHistory(machineLocationHistory);
    }

    public void updateBatchMachineLocationHistory(List<MachineLocationHistoryUpdateData> updateMachineLocationHistory) {
        combinedHistoryService.updateBatchMachineLocationHistory(updateMachineLocationHistory);
    }

    public void addLocationHistory(List<MachineLocationHistory> machineLocationHistory) {
        combinedHistoryService.addLocationHistory(machineLocationHistory);
    }
    public MachineLocation getExistingMachineLocation(final String vin) {
        return combinedHistoryService.getExistingMachineLocation(vin);
    }

    public void updateStatusOnTime(List<MachineData> packets) {
        combinedHistoryService.updateStatusOnTime(packets, combinedHistoryService.getLastModifiedDate());
    }

    public List<Map<String, Object>> getMachineForTimefenceAlert(Set<String> vinList) {
        return combinedHistoryService.getMachineForTimefenceAlert(vinList, combinedHistoryService.getToday()); // Pass vinList
    }

    public List<Map<String, Object>> getMachineForGeofenceAlert(Set<String> vinList) {
        return combinedHistoryService.getMachineForGeofenceAlert(vinList); // Pass vinList
    }

    public List<Map<String, Object>> findAlertByVin(Set<String> vinList, String eventName) {
        return combinedHistoryService.findAlertByVin(vinList,eventName);

    }
}