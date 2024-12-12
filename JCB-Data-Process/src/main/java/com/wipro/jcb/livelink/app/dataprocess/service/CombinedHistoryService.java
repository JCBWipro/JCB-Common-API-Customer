package com.wipro.jcb.livelink.app.dataprocess.service;

import com.wipro.jcb.livelink.app.dataprocess.dto.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Component
public interface CombinedHistoryService {

    String getToday();
    Timestamp getLastModifiedDate();
    void addBatchMachineWithFuelConsume(List<MachineFuelConsumption> packets);
    void addBatchMachineWithEngineStatus(List<MachineEngineStatus> packets);

    List<Map<String, Object>> getMachineForTimefenceAlert(Set<String> vinList, String today);
    List<Map<String, Object>> getMachineForGeofenceAlert(Set<String> vinList);
    void addTimefenceAlert(List<AlertData> alertData);
    void addGeofenceAlert(List<AlertData> alertData);
    void updateBatchMachineLocation(List<MachineLocation> packets);
    void updateBatchMachineFeedData(List<MachineData> packets);
    void updateBatchMachineFeedDataHavingFuel(List<MachineData> packets);
    void addBatchMachineLocationHistory(List<MachineLocationHistory> machineLocationHistory);
    void updateBatchMachineLocationHistory(List<MachineLocationHistoryUpdateData> updateMachineLocationHistory);
    void addLocationHistory(List<MachineLocationHistory> machineLocationHistory);
    MachineLocation getExistingMachineLocation(final String vin);
    void updateStatusOnTime(List<MachineData> packets);
    int removeAlertForGeofence(Set<String> vinList);
    int findByAlertById(String id);
    List<Map<String, Object>> findAlertByVin(Set<String> vinList);
    List<Map<String, Object>> findAlertByVin(Set<String> vinList,String eventName);
    void updateStatusOnTime(List<MachineData> packets, Timestamp lastModifiedDate);
}
