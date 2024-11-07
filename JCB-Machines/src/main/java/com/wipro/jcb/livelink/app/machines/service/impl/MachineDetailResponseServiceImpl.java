package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.constants.EventType;
import com.wipro.jcb.livelink.app.machines.constants.FuelLevelNAConstant;
import com.wipro.jcb.livelink.app.machines.dto.*;
import com.wipro.jcb.livelink.app.machines.entity.*;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.AlertRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineFuelConsumptionDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachinePerformanceDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineUtilizationDataRepository;
import com.wipro.jcb.livelink.app.machines.service.MachineDetailResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wipro.jcb.livelink.app.machines.constants.AppServerConstants.loadHistoricalDataForDays;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class MachineDetailResponseServiceImpl implements MachineDetailResponseService {

    @Autowired
    MachinePerformanceDataRepository machinePerformanceDataRepository;

    @Autowired
    MachineFuelConsumptionDataRepository machineFuelConsumptionDataRepository;

    @Autowired
    MachineUtilizationDataRepository machineUtilizationDataRepository;

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    Utilities utilities;

    @Value("${machine.online}")
    int online;

    @Override
    public MachineDetailResponse getMachineDetailResponseList(Machine machine, MachineFeedParserData machineFeedparserdata) throws ProcessCustomError {
        log.info("getMachineDetailResponseList: Request received for machine: {}", machine.getVin());
        long startTime = System.currentTimeMillis();

        String startDate = utilities.getStartDate(loadHistoricalDataForDays);
        String endDate = utilities.getEndDate(1);

        try {
            MachineDetailResponse machineDetailResponse;
            int serviceAlertCount = 0;
            int normalAlertCount = 0;
            int alertCountWithRedEventLevel = 0;
            boolean engineStatus = true;

            String vin = machine.getVin();
            log.debug("getMachineDetailResponseList: Processing machine details for VIN: {}", vin);

            boolean fuelDataExcluded = (FuelLevelNAConstant.getExceptionMachines().contains(vin)) ||
                    !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) ||
                    !FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));

            log.debug("getMachineDetailResponseList: Fuel data exclusion flag: {}", fuelDataExcluded);

            log.debug("getMachineDetailResponseList: Fetching fuel consumption data...");
            final List<MachineFuelConsumptionData> machineFuelConsumptionData = fuelDataExcluded
                    ? new ArrayList<>()
                    : machineFuelConsumptionDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
            log.debug("getMachineDetailResponseList: Fetched {} fuel consumption data records", machineFuelConsumptionData.size());

            final List<AlertData> alertData = new ArrayList<>();
            Date connectivityTime = utilities.getStartDateTimeWithMinutes(online);
            String engineValue = "on";

            Date lastCommunicationTime;
            if (machineFeedparserdata != null && machineFeedparserdata.getLastModifiedDate() != null) {
                lastCommunicationTime = machineFeedparserdata.getLastModifiedDate();
                log.debug("getMachineDetailResponseList: Using last modified date from MachineFeedParserData: {}", lastCommunicationTime);
            } else {
                lastCommunicationTime = machine.getLastCommunicationTime();
                log.debug("getMachineDetailResponseList: Using last communication time from Machine: {}", lastCommunicationTime);
            }

            final MachineDetailData machineDetailData;
            if (!machineFuelConsumptionData.isEmpty()) {
                machineDetailData = new MachineDetailData(engineStatus, engineValue,
                        connectivityTime.before(machine.getStatusAsOnTime()),
                        (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off",
                        machine.getCoolantTemperatureAlertStatus(),
                        (machine.getCoolantTemperatureAlertStatus()) ? "Fault" : "No Fault",
                        machine.getBatteryConnectedStatus(),
                        machine.getBatteryVoltage().toString(),
                        machine.getEngineOilPressureAlertStatus(),
                        (machine.getEngineOilPressureAlertStatus()) ? "Fault" : "No Fault",
                        machine.getAirFilterAlertStatus(),
                        (machine.getAirFilterAlertStatus()) ? "Fault" : "No Fault",
                        machine.getFuelLevelStatus(),
                        machineFuelConsumptionData.get(0).getFuelConsumed().toString(),
                        machine.getStatusAsOnTime(), lastCommunicationTime,
                        machine.getImage(), machine.getThumbnail());
            } else {
                machineDetailData = new MachineDetailData(engineStatus, engineValue,
                        connectivityTime.before(machine.getStatusAsOnTime()),
                        (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off",
                        machine.getCoolantTemperatureAlertStatus(),
                        (machine.getCoolantTemperatureAlertStatus()) ? "Fault" : "No Fault",
                        machine.getBatteryConnectedStatus(),
                        machine.getBatteryVoltage().toString(),
                        machine.getEngineOilPressureAlertStatus(),
                        (machine.getEngineOilPressureAlertStatus()) ? "Fault" : "No Fault",
                        machine.getAirFilterAlertStatus(),
                        (machine.getAirFilterAlertStatus()) ? "Fault" : "No Fault",
                        machine.getFuelLevelStatus(),
                        fuelDataExcluded ? "-" : "NA",
                        machine.getStatusAsOnTime(), lastCommunicationTime,
                        machine.getImage(), machine.getThumbnail());
            }

            log.debug("getMachineDetailResponseList: Processing alerts for VIN: {}", vin);
            for (final Alert alert : alertRepository.findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(vin, true, true)) {
                if (alert.getEventType() == EventType.Service) {
                    serviceAlertCount++;
                } else {
                    normalAlertCount++;
                    if (alert.getIsOpen() && alert.getEventLevel() == EventLevel.RED) {
                        alertCountWithRedEventLevel++;
                    }
                }
                alertData.add(new AlertData(alert.getEventGeneratedTime(), alert.getEventLevel(),
                        alert.getEventName(), alert.getEventType(), alert.getId(), alert.getIsOpen(),
                        alert.getIsDtcAlert()));
            }
            log.debug("getMachineDetailResponseList: Processed {} alerts", alertData.size());

            if (machineFeedparserdata != null && machineFeedparserdata.getStatusAsOnTime() != null) {
                machineDetailData.setBatteryVoltage(machineFeedparserdata.getBatteryVoltage().toString());
                machineDetailData.setConnectivity(connectivityTime.before(machineFeedparserdata.getStatusAsOnTime()));
                machineDetailData.setConnectivityValue((connectivityTime.before(machineFeedparserdata.getStatusAsOnTime())) ? "On" : "Off");
            }

            log.debug("getMachineDetailResponseList: Fetching utilization data...");
            List<MachineUtilizationData> machineUtilizationData = machineUtilizationDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
            log.debug("getMachineDetailResponseList: Fetched {} utilization data records", machineUtilizationData.size());

            log.debug("getMachineDetailResponseList: Fetching performance data...");
            List<MachinePerformanceData> machinePerformanceData = machinePerformanceDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
            log.debug("getMachineDetailResponseList: Fetched {} performance data records", machinePerformanceData.size());

            machineDetailResponse = new MachineDetailResponse(machineDetailData, alertData,
                    machineUtilizationData, machineFuelConsumptionData, machinePerformanceData);
            machineDetailResponse.setServiceAlertCount(serviceAlertCount);
            machineDetailResponse.setNormalAlertCount(normalAlertCount);
            machineDetailResponse.setAlertWithRedEventLevel(alertCountWithRedEventLevel);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("getMachineDetailResponseList: Completed for VIN: {}. Time taken: {}ms", machine.getVin(), elapsedTime);
            return machineDetailResponse;

        } catch (final Exception ex) {
            log.error("getMachineDetailResponseList: Error processing machine details for VIN: {}. Error: {}",
                    machine.getVin(), ex.getMessage(), ex);
            throw new ProcessCustomError("Issue faced while retrieving machine details", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineDetailResponse getMachineDetailResponseListV2(Machine machine, MachineFeedParserData machineFeedparserdata, Boolean skipReports) throws ProcessCustomError {
        log.info("getMachineDetailResponseListV2: Request received for machine: {}, skipReports: {}", machine.getVin(), skipReports);
        long startTime = System.currentTimeMillis();

        String startDate = utilities.getStartDate(loadHistoricalDataForDays);
        String endDate = utilities.getEndDate(1);

        try {
            MachineDetailResponse machineDetailResponse;
            int serviceAlertCount = 0;
            int normalAlertCount = 0;
            int alertCountWithRedEventLevel = 0;
            boolean engineStatus = false;

            String vin = machine.getVin();
            log.debug("getMachineDetailResponseListV2: Processing machine details for VIN: {}", vin);

            boolean fuelDataExcluded = (FuelLevelNAConstant.getExceptionMachines().contains(vin)) ||
                    !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) ||
                    !FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));

            if (skipReports != null && skipReports) {
                fuelDataExcluded = false;
            }

            log.debug("getMachineDetailResponseListV2: Fuel data exclusion flag: {}", fuelDataExcluded);

            List<MachineFuelConsumptionData> updatedMachineFuelConsumptionData = fuelDataExcluded
                    ? machineFuelConsumptionDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate))
                    : new ArrayList<>();
            log.debug("getMachineDetailResponseListV2: Fetched {} fuel consumption data records", updatedMachineFuelConsumptionData.size());

            final List<AlertData> alertData = new ArrayList<>();

            Date connectivityTime = utilities.getStartDateTimeWithMinutes(online);
            log.info("connectivityTime : {}", connectivityTime.toString());

            String engineValue = "off";

            Date lastCommunicationTime;
            if (machineFeedparserdata != null && machineFeedparserdata.getLastModifiedDate() != null) {
                lastCommunicationTime = machineFeedparserdata.getLastModifiedDate();
                log.debug("getMachineDetailResponseListV2: Using last modified date from MachineFeedParserData: {}", lastCommunicationTime);
            } else {
                lastCommunicationTime = machine.getLastCommunicationTime();
                log.debug("getMachineDetailResponseListV2: Using last communication time from Machine: {}", lastCommunicationTime);
            }

            final MachineDetailData machineDetailData;
            if (!updatedMachineFuelConsumptionData.isEmpty()) {
                machineDetailData = new MachineDetailData(
                        engineStatus,
                        engineValue,
                        connectivityTime.before(machine.getStatusAsOnTime()),
                        (connectivityTime.before(machine.getStatusAsOnTime())) ? "Off" : "On",
                		machine.getCoolantTemperatureAlertStatus()!=null ? machine.getCoolantTemperatureAlertStatus() : false,
                        (machine.getCoolantTemperatureAlertStatus()!=null) ? "Fault" : " No Fault",
                        machine.getBatteryConnectedStatus()!=null ? machine.getBatteryConnectedStatus() : false,
                        machine.getBatteryVoltage() != null ? machine.getBatteryVoltage().toString() : "NA", // Null check for battery voltage
                        machine.getEngineOilPressureAlertStatus()!=null ? machine.getEngineOilPressureAlertStatus() : false,
                        (machine.getEngineOilPressureAlertStatus()!=null) ? machine.getEngineOilPressureAlertStatus().toString() : "No Fault",
                        machine.getAirFilterAlertStatus()!=null ? machine.getCoolantTemperatureAlertStatus() : false,
                        (machine.getAirFilterAlertStatus()!=null) ? machine.getAirFilterAlertStatus().toString() : "No Fault",
                        machine.getFuelLevelStatus()!=null ? machine.getFuelLevelStatus() : false,
                        updatedMachineFuelConsumptionData.get(0).getFuelConsumed() != null ?
                                updatedMachineFuelConsumptionData.get(0).getFuelConsumed().toString() : "NA", // Null check for fuel consumed
                        machine.getStatusAsOnTime() != null ? machine.getStatusAsOnTime() : new Date(), // Null check for statusAsOnTime
                        lastCommunicationTime != null ? lastCommunicationTime : new Date(), // Null check for lastCommunicationTime
                        machine.getImage() != null ? machine.getImage() : "", // Null check for image
                        machine.getThumbnail() != null ? machine.getThumbnail() : "" // Null check for thumbnail
                );
            } else {
                machineDetailData = new MachineDetailData(
                        engineStatus,
                        engineValue,
                        connectivityTime.before(machine.getStatusAsOnTime()),
                        (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off",
                		machine.getCoolantTemperatureAlertStatus() !=null ? machine.getCoolantTemperatureAlertStatus() : false,
                        (machine.getCoolantTemperatureAlertStatus()!=null) ? "Fault" : "No Fault",
                        machine.getBatteryConnectedStatus()!=null ? machine.getBatteryConnectedStatus() : false,
                        machine.getBatteryVoltage() != null ? machine.getBatteryVoltage().toString() : "NA", // Null check for battery voltage
                        machine.getEngineOilPressureAlertStatus()!=null ? machine.getEngineOilPressureAlertStatus() : false,
                        (machine.getEngineOilPressureAlertStatus()!=null) ? "Fault" : "No Fault",
                        machine.getAirFilterAlertStatus()!=null ? machine.getAirFilterAlertStatus() : false,
                        (machine.getAirFilterAlertStatus()!=null) ? "Fault" : "No Fault",
                        machine.getFuelLevelStatus()!=null ? machine.getFuelLevelStatus() : false,
                        fuelDataExcluded ? "-" : "NA",
                        machine.getStatusAsOnTime() != null ? machine.getStatusAsOnTime() : new Date(), // Null check for statusAsOnTime
                        lastCommunicationTime != null ? lastCommunicationTime : new Date(), // Null check for lastCommunicationTime
                        machine.getImage() != null ? machine.getImage() : "", // Null check for image
                        machine.getThumbnail() != null ? machine.getThumbnail() : "" // Null check for thumbnail
                );
            }

            log.debug("getMachineDetailResponseListV2: Processing alerts for VIN: {}", vin);
            for (final Alert alert : alertRepository.findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(vin, true, true)) {
                if (alert.getEventType() == EventType.Service) {
                    serviceAlertCount++;
                } else {
                    normalAlertCount++;
                    if (alert.getIsOpen() && alert.getEventLevel() == EventLevel.RED) {
                        alertCountWithRedEventLevel++;
                    }
                }
                alertData.add(new AlertData(alert.getEventGeneratedTime(), alert.getEventLevel(),
                        alert.getEventName(), alert.getEventType(), alert.getId(), alert.getIsOpen(),
                        alert.getIsDtcAlert()));
            }
            log.debug("getMachineDetailResponseListV2: Processed {} alerts", alertData.size());

            if (machineFeedparserdata != null && machineFeedparserdata.getStatusAsOnTime() != null) {
                machineDetailData.setBatteryVoltage(machineFeedparserdata.getBatteryVoltage().toString());
                machineDetailData.setConnectivity(connectivityTime.before(machineFeedparserdata.getStatusAsOnTime()));
                machineDetailData.setConnectivityValue((connectivityTime.before(machineFeedparserdata.getStatusAsOnTime())) ? "On" : "Off");
            }

            final long daysDifference = TimeUnit.DAYS.convert(
                    Math.abs(utilities.getDate(utilities.getStartDate(1)).getTime() - utilities.getDate(startDate).getTime()),
                    TimeUnit.MILLISECONDS) + 1;
            log.debug("getMachineDetailResponseListV2: Days difference for data calculation: {}", daysDifference);

            if (fuelDataExcluded && daysDifference > updatedMachineFuelConsumptionData.size()) {
                addMissingFuelConsumptionData(updatedMachineFuelConsumptionData, vin, startDate, daysDifference, machine);
            }

            List<MachineUtilizationData> updatedMachineUtililizationData = new ArrayList<>();
            List<MachinePerformanceData> updatedMachinePerformanceData = new ArrayList<>();

            if (skipReports == null || !skipReports) {
                log.debug("getMachineDetailResponseListV2: Fetching utilization data...");
                updatedMachineUtililizationData = machineUtilizationDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                log.debug("getMachineDetailResponseListV2: Fetched {} utilization data records", updatedMachineUtililizationData.size());

                if (daysDifference > updatedMachineUtililizationData.size()) {
                    addMissingUtilizationData(updatedMachineUtililizationData, vin, startDate, daysDifference, machine);
                }

                log.debug("getMachineDetailResponseListV2: Fetching performance data...");
                updatedMachinePerformanceData = machinePerformanceDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                log.debug("getMachineDetailResponseListV2: Fetched {} performance data records", updatedMachinePerformanceData.size());

                if (daysDifference > updatedMachinePerformanceData.size()) {
                    addMissingPerformanceData(updatedMachinePerformanceData, vin, startDate, daysDifference, machine);
                }
            }

            machineDetailResponse = new MachineDetailResponse(machineDetailData, alertData,
                    updatedMachineUtililizationData, updatedMachineFuelConsumptionData, updatedMachinePerformanceData);
            machineDetailResponse.setServiceAlertCount(serviceAlertCount);
            machineDetailResponse.setNormalAlertCount(normalAlertCount);
            machineDetailResponse.setAlertWithRedEventLevel(alertCountWithRedEventLevel);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("getMachineDetailResponseListV2: Completed for VIN: {}. Time taken: {}ms", machine.getVin(), elapsedTime);
            return machineDetailResponse;

        } catch (final Exception ex) {
            log.error("getMachineDetailResponseListV2: Error processing machine details for VIN: {}. Error: {}",
                    machine.getVin(), ex.getMessage(), ex);
            throw new ProcessCustomError("Issue faced while retrieving machine details", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineDetailResponse getMachineDetailResponseListV3(Machine machine) throws ProcessCustomError {
        log.info("getMachineDetailResponseListV3: Request received for machine: {}", machine.getVin());
        long startTime = System.currentTimeMillis();

        String startDate = utilities.getStartDate(loadHistoricalDataForDays);
        String endDate = utilities.getEndDate(1);

        try {
            MachineDetailResponse machineDetailResponse = new MachineDetailResponse();

            String vin = machine.getVin();
            log.debug("getMachineDetailResponseListV3: Processing machine details for VIN: {}", vin);

            boolean fuelDataExcluded = (FuelLevelNAConstant.getExceptionMachines().contains(vin)) ||
                    !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) ||
                    !FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));

            log.debug("getMachineDetailResponseListV3: Fuel data exclusion flag: {}", fuelDataExcluded);

            List<MachineFuelConsumptionData> updatedMachineFuelConsumptionData = new ArrayList<>();
            List<MachineUtilizationData> updatedMachineUtilizationData;
            List<MachinePerformanceData> updatedMachinePerformanceData;

            final long daysDifference = TimeUnit.DAYS.convert(
                    Math.abs(utilities.getDate(utilities.getStartDate(1)).getTime() - utilities.getDate(startDate).getTime()),
                    TimeUnit.MILLISECONDS) + 1;

            log.debug("getMachineDetailResponseListV3: Days difference for data calculation: {}", daysDifference);

            if (fuelDataExcluded) {
                log.debug("getMachineDetailResponseListV3: Fetching fuel consumption data...");
                updatedMachineFuelConsumptionData = machineFuelConsumptionDataRepository.findByVinAndDayBetweenOrderByDayAsc(
                        vin, utilities.getDate(startDate), utilities.getDate(endDate));

                if (daysDifference > updatedMachineFuelConsumptionData.size()) {
                    addMissingFuelConsumptionData(updatedMachineFuelConsumptionData, vin, startDate, daysDifference, machine);
                } else {
                    removeFuelConsumptionDataWithNA(updatedMachineFuelConsumptionData);
                }
            }

            log.debug("getMachineDetailResponseListV3: Fetching utilization data...");
            updatedMachineUtilizationData = machineUtilizationDataRepository.findByVinAndDayBetweenOrderByDayAsc(
                    vin, utilities.getDate(startDate), utilities.getDate(endDate));

            if (daysDifference > updatedMachineUtilizationData.size()) {
                addMissingUtilizationData(updatedMachineUtilizationData, vin, startDate, daysDifference, machine);
            }

            log.debug("getMachineDetailResponseListV3: Fetching performance data...");
            updatedMachinePerformanceData = machinePerformanceDataRepository.findByVinAndDayBetweenOrderByDayAsc(
                    vin, utilities.getDate(startDate), utilities.getDate(endDate));

            if (daysDifference > updatedMachinePerformanceData.size()) {
                addMissingPerformanceData(updatedMachinePerformanceData, vin, startDate, daysDifference, machine);
            }

            machineDetailResponse.setUpdatedFuelList(updatedMachineFuelConsumptionData);
            machineDetailResponse.setUpdatedPerformanceList(updatedMachinePerformanceData);
            machineDetailResponse.setUpdatedUtilizationList(updatedMachineUtilizationData);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("getMachineDetailResponseListV3: Completed for VIN: {}. Time taken: {}ms", machine.getVin(), elapsedTime);
            return machineDetailResponse;

        } catch (final Exception ex) {
            log.error("getMachineDetailResponseListV3: Error processing machine details for VIN: {}. Error: {}",
                    machine.getVin(), ex.getMessage(), ex);
            throw new ProcessCustomError("Issue faced while retrieving machine details V3", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper methods to add missing data
    private void addMissingFuelConsumptionData(List<MachineFuelConsumptionData> updatedMachineFuelConsumptionData, String vin, String startDate, long daysDifference, Machine machine) {
        log.debug("getMachineDetailResponseListV3: Adding missing fuel consumption data...");
        List<Date> missingDates = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
        for (MachineFuelConsumptionData fuelConsumptionData : updatedMachineFuelConsumptionData) {
            missingDates.remove(fuelConsumptionData.getDay());
        }
        for (Date date : missingDates) {
            updatedMachineFuelConsumptionData.add(new MachineFuelConsumptionData(date, null, null, machine.getPlatform(), null, machine));
        }
        updatedMachineFuelConsumptionData.sort(Comparator.comparing(MachineFuelConsumptionData::getDay));
        log.debug("getMachineDetailResponseListV3: Added missing fuel consumption data for {} days", missingDates.size());
    }

    private void addMissingUtilizationData(List<MachineUtilizationData> updatedMachineUtilizationData, String vin, String startDate, long daysDifference, Machine machine) {
        log.debug("getMachineDetailResponseListV3: Adding missing utilization data...");
        List<Date> missingDates = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
        for (MachineUtilizationData utilizationData : updatedMachineUtilizationData) {
            missingDates.remove(utilizationData.getDay());
        }
        for (Date date : missingDates) {
            updatedMachineUtilizationData.add(new MachineUtilizationData(date, null, machine.getModel(), null, machine.getPlatform(), machine, null));
        }
        updatedMachineUtilizationData.sort(Comparator.comparing(MachineUtilizationData::getDay));
        log.debug("getMachineDetailResponseListV3: Added missing utilization data for {} days", missingDates.size());
    }

    private void addMissingPerformanceData(List<MachinePerformanceData> updatedMachinePerformanceData, String vin, String startDate, long daysDifference, Machine machine) {
        log.debug("getMachineDetailResponseListV3: Adding missing performance data...");
        List<Date> missingDates = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
        for (MachinePerformanceData performanceData : updatedMachinePerformanceData) {
            missingDates.remove(performanceData.getDay());
        }
        for (Date date : missingDates) {
            updatedMachinePerformanceData.add(new MachinePerformanceData(date, machine.getModel(), machine.getPlatform(), null, null, null, machine));
        }
        updatedMachinePerformanceData.sort(Comparator.comparing(MachinePerformanceData::getDay));
        log.debug("getMachineDetailResponseListV3: Added missing performance data for {} days", missingDates.size());
    }

    // Helper method to remove data with "NA" fuel level
    private void removeFuelConsumptionDataWithNA(List<MachineFuelConsumptionData> updatedMachineFuelConsumptionData) {
        log.debug("getMachineDetailResponseListV3: Removing fuel consumption data with 'NA' fuel level...");
        updatedMachineFuelConsumptionData.removeIf(fuelConsumptionData ->
                StringUtils.hasLength(fuelConsumptionData.getFuelLevel()) &&
                        fuelConsumptionData.getFuelLevel().equals("NA"));
        log.debug("getMachineDetailResponseListV3: Removed fuel consumption data with 'NA' fuel level.");
    }
}
