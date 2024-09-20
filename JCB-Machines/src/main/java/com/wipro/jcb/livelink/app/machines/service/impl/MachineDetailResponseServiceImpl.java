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

        String startDate = utilities.getStartDate(loadHistoricalDataForDays);
        String endDate = utilities.getEndDate(1);

        try {

            MachineDetailResponse ms = new MachineDetailResponse();

            int serviceAlertCount = 0;
            int normalAlertCount = 0;
            int alertCountWithRedEventLevel = 0;

            boolean engineStatus = true;
            if (machine != null) {
                String vin = machine.getVin();

                boolean flag = (FuelLevelNAConstant.getExceptionMachines().contains(vin)) || !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) || !FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));

                final List<MachineFuelConsumptionData> machineFuelConsumptionData = flag ? machineFuelConsumptionDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate)) : new ArrayList<>();
                final List<AlertData> alertData = new ArrayList<>();
                Date connectivityTime = utilities.getStartDateTimeWithMinutes(online);

                String engineValue = "on";

                Date lastCommunicationTime;
                // logger.debug("getMachineDetailResponseList: setting lastCommunicationTime");
                if (machineFeedparserdata != null && machineFeedparserdata.getLastModifiedDate() != null) {
                    // logger.debug("getMachineDetailResponseList: not null machine feed parser
                    // location data last modified");
                    lastCommunicationTime = machineFeedparserdata.getLastModifiedDate();
                } else {
                    // logger.debug("getMachineDetailResponseList: null machine feed parser location
                    // data last modified");
                    lastCommunicationTime = machine.getLastCommunicationTime();
                }
                final MachineDetailData machineDetailData;
                if (!machineFuelConsumptionData.isEmpty()) {
                    machineDetailData = new MachineDetailData(engineStatus, engineValue, connectivityTime.before(machine.getStatusAsOnTime()), (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off", machine.getCoolantTemperatureAlertStatus(), (machine.getCoolantTemperatureAlertStatus()) ? "Fault" : "No Fault", machine.getBatteryConnectedStatus(), machine.getBatteryVoltage().toString(), machine.getEngineOilPressureAlertStatus(), (machine.getEngineOilPressureAlertStatus()) ? "Fault" : "No Fault", machine.getAirFilterAlertStatus(), (machine.getAirFilterAlertStatus()) ? "Fault" : "No Fault", machine.getFuelLevelStatus(), machineFuelConsumptionData.get(0).getFuelConsumed().toString(), machine.getStatusAsOnTime(), lastCommunicationTime, machine.getImage(), machine.getThumbnail());
                } else {

                    machineDetailData = new MachineDetailData(engineStatus, engineValue, connectivityTime.before(machine.getStatusAsOnTime()), (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off", machine.getCoolantTemperatureAlertStatus(), (machine.getCoolantTemperatureAlertStatus()) ? "Fault" : "No Fault", machine.getBatteryConnectedStatus(), machine.getBatteryVoltage().toString(), machine.getEngineOilPressureAlertStatus(), (machine.getEngineOilPressureAlertStatus()) ? "Fault" : "No Fault", machine.getAirFilterAlertStatus(), (machine.getAirFilterAlertStatus()) ? "Fault" : "No Fault", machine.getFuelLevelStatus(), flag ? "-" : "NA", machine.getStatusAsOnTime(), lastCommunicationTime, machine.getImage(), machine.getThumbnail());
                }
                for (final Alert alert : alertRepository.findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(vin, true, true)) {
                    if (alert.getEventType() == EventType.Service) {
                        serviceAlertCount++;
                    } else {
                        normalAlertCount++;
                        if (alert.getIsOpen() && alert.getEventLevel() == EventLevel.RED) {
                            alertCountWithRedEventLevel++;
                        }
                    }
                    alertData.add(new AlertData(alert.getEventGeneratedTime(), alert.getEventLevel(), alert.getEventName(), alert.getEventType(), alert.getId(), alert.getIsOpen(), alert.getIsDtcAlert()));
                }
                if (null != machineFeedparserdata && null != machineFeedparserdata.getStatusAsOnTime()) {
                    machineDetailData.setBatteryVoltage(machineFeedparserdata.getBatteryVoltage().toString());
                    machineDetailData.setConnectivity(connectivityTime.before(machineFeedparserdata.getStatusAsOnTime()));
                    machineDetailData.setConnectivityValue((connectivityTime.before(machineFeedparserdata.getStatusAsOnTime())) ? "On" : "Off");
                }
                List<MachineUtilizationData> machineUtilizationData = machineUtilizationDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));

                List<MachinePerformanceData> machinePerformanceData = machinePerformanceDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                ms = new MachineDetailResponse(machineDetailData, alertData, machineUtilizationData, machineFuelConsumptionData, machinePerformanceData);
                ms.setServiceAlertCount(serviceAlertCount);
                ms.setNormalAlertCount(normalAlertCount);
                ms.setAlertWithRedEventLevel(alertCountWithRedEventLevel);
            }
            return ms;
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("getMachineDetailResponseList :Issue faced while retriving machine details: - {}", ex.getMessage());
            throw new ProcessCustomError("Issue faced while retriving machine details", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineDetailResponse getMachineDetailResponseListV2(Machine machine, MachineFeedParserData machineFeedparserdata, Boolean skipReports) throws ProcessCustomError {

        String startDate = utilities.getStartDate(loadHistoricalDataForDays);
        String endDate = utilities.getEndDate(1);
        try {
            MachineDetailResponse ms = new MachineDetailResponse();
            int serviceAlertCount = 0;
            int normalAlertCount = 0;
            int alertCountWithRedEventLevel = 0;
            boolean engineStatus = false;
            if (machine != null) {
                String vin = machine.getVin();

                boolean flag = (FuelLevelNAConstant.getExceptionMachines().contains(vin)) || !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) || !FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));

                if (skipReports != null && skipReports) flag = false;
                List<MachineFuelConsumptionData> updatedMachineFuelConsumptionData;
                updatedMachineFuelConsumptionData = flag ? machineFuelConsumptionDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate)) : new ArrayList<>();
                final List<AlertData> alertData = new ArrayList<>();
                Date connectivityTime = utilities.getStartDateTimeWithMinutes(online);
                String engineValue = "off";
                Date lastCommunicationTime;
                if (machineFeedparserdata != null && machineFeedparserdata.getLastModifiedDate() != null) {

                    lastCommunicationTime = machineFeedparserdata.getLastModifiedDate();
                } else {
                    lastCommunicationTime = machine.getLastCommunicationTime();
                }
                final MachineDetailData machineDetailData;
                if (!updatedMachineFuelConsumptionData.isEmpty()) {
                    machineDetailData = new MachineDetailData(engineStatus, engineValue, connectivityTime.before(machine.getStatusAsOnTime()), (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off", machine.getCoolantTemperatureAlertStatus(), (machine.getCoolantTemperatureAlertStatus()) ? "Fault" : "No Fault", machine.getBatteryConnectedStatus(), machine.getBatteryVoltage().toString(), machine.getEngineOilPressureAlertStatus(), (machine.getEngineOilPressureAlertStatus()) ? "Fault" : "No Fault", machine.getAirFilterAlertStatus(), (machine.getAirFilterAlertStatus()) ? "Fault" : "No Fault", machine.getFuelLevelStatus(), flag ? updatedMachineFuelConsumptionData.get(0).getFuelConsumed().toString() : "NA", machine.getStatusAsOnTime(), lastCommunicationTime, machine.getImage(), machine.getThumbnail());
                } else {
                    machineDetailData = new MachineDetailData(engineStatus, engineValue, connectivityTime.before(machine.getStatusAsOnTime()), (connectivityTime.before(machine.getStatusAsOnTime())) ? "On" : "Off", machine.getCoolantTemperatureAlertStatus(), (machine.getCoolantTemperatureAlertStatus()) ? "Fault" : "No Fault", machine.getBatteryConnectedStatus(), machine.getBatteryVoltage().toString(), machine.getEngineOilPressureAlertStatus(), (machine.getEngineOilPressureAlertStatus()) ? "Fault" : "No Fault", machine.getAirFilterAlertStatus(), (machine.getAirFilterAlertStatus()) ? "Fault" : "No Fault", machine.getFuelLevelStatus(), flag ? "-" : "NA", machine.getStatusAsOnTime(), lastCommunicationTime, machine.getImage(), machine.getThumbnail());
                }
                for (final Alert alert : alertRepository.findByVinAndIsCustomerVisibleAndIsOpenOrderByEventGeneratedTimeDescIdAsc(vin, true, true)) {

                    if (alert.getEventType() == EventType.Service) {
                        serviceAlertCount++;
                    } else {
                        normalAlertCount++;
                        if (alert.getIsOpen() && alert.getEventLevel() == EventLevel.RED) {
                            alertCountWithRedEventLevel++;
                        }
                    }
                    alertData.add(new AlertData(alert.getEventGeneratedTime(), alert.getEventLevel(), alert.getEventName(), alert.getEventType(), alert.getId(), alert.getIsOpen(), alert.getIsDtcAlert()));
                }
                if (null != machineFeedparserdata && null != machineFeedparserdata.getStatusAsOnTime()) {
                    machineDetailData.setBatteryVoltage(machineFeedparserdata.getBatteryVoltage().toString());
                    machineDetailData.setConnectivity(connectivityTime.before(machineFeedparserdata.getStatusAsOnTime()));
                    machineDetailData.setConnectivityValue((connectivityTime.before(machineFeedparserdata.getStatusAsOnTime())) ? "On" : "Off");
                }
                final long daysDifference = TimeUnit.DAYS.convert(Math.abs(utilities.getDate(utilities.getStartDate(1)).getTime() - utilities.getDate(startDate).getTime()), TimeUnit.MILLISECONDS) + 1;
                if (flag && daysDifference > updatedMachineFuelConsumptionData.size()) {
                    List<Date> list = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
                    for (MachineFuelConsumptionData fuelConsumptionData : updatedMachineFuelConsumptionData) {
                        list.remove(fuelConsumptionData.getDay());
                    }
                    for (Date date : list) {
                        updatedMachineFuelConsumptionData.add(new MachineFuelConsumptionData(date, null, null, machine.getPlatform(), null, machine));
                    }
                    updatedMachineFuelConsumptionData.sort(Comparator.comparing(MachineFuelConsumptionData::getDay));
                }
                List<MachineUtilizationData> updatedMachineUtililizationData = new ArrayList<>();
                List<MachinePerformanceData> updatedMachinePerformanceData = new ArrayList<>();
                if (skipReports == null || !skipReports) {
                    updatedMachineUtililizationData = machineUtilizationDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                    if (daysDifference > updatedMachineUtililizationData.size()) {
                        List<Date> list = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
                        for (MachineUtilizationData utilizationData : updatedMachineUtililizationData) {
                            list.remove(utilizationData.getDay());
                        }
                        for (Date date : list) {
                            updatedMachineUtililizationData.add(new MachineUtilizationData(date, null, machine.getModel(), null, machine.getPlatform(), machine, null));
                        }
                        updatedMachineUtililizationData.sort(Comparator.comparing(MachineUtilizationData::getDay));
                    }
                    updatedMachinePerformanceData = machinePerformanceDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                    if (daysDifference > updatedMachinePerformanceData.size()) {
                        List<Date> list = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
                        for (MachinePerformanceData performaceData : updatedMachinePerformanceData) {
                            list.remove(performaceData.getDay());
                        }
                        for (Date date : list) {
                            updatedMachinePerformanceData.add(new MachinePerformanceData(date, machine.getModel(), machine.getPlatform(), null, null, null, machine));
                        }
                        updatedMachinePerformanceData.sort(Comparator.comparing(MachinePerformanceData::getDay));
                    }
                }
                ms = new MachineDetailResponse(machineDetailData, alertData, null, null, null);
                ms.setServiceAlertCount(serviceAlertCount);
                ms.setNormalAlertCount(normalAlertCount);
                ms.setAlertWithRedEventLevel(alertCountWithRedEventLevel);
                ms.setUpdatedFuelList(updatedMachineFuelConsumptionData);
                ms.setUpdatedPerformanceList(updatedMachinePerformanceData);
                ms.setUpdatedUtilizationList(updatedMachineUtililizationData);
            }
            return ms;
        } catch (final Exception ex) {
            ex.printStackTrace();

            log.error("getMachineDetailResponseList :Issue faced while retrieving machine details :-{}", ex.getMessage());
            throw new ProcessCustomError("Issue faced while retrieving machine details", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineDetailResponse getMachineDetailResponseListV3(Machine machine) throws ProcessCustomError {

        String startDate = utilities.getStartDate(loadHistoricalDataForDays);
        String endDate = utilities.getEndDate(1);
        try {
            MachineDetailResponse ms = new MachineDetailResponse();
            if (machine != null) {
                String vin = machine.getVin();
                boolean flag = (FuelLevelNAConstant.getExceptionMachines().contains(vin)) || !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) || !FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));
                List<MachineFuelConsumptionData> updatedMachineFuelConsumptionData;
                List<MachineFuelConsumptionData> removeMachineFuelConsumptionNA = new ArrayList<>();
                updatedMachineFuelConsumptionData = flag ? machineFuelConsumptionDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate)) : new ArrayList<>();

                final long daysDifference = TimeUnit.DAYS.convert(Math.abs(utilities.getDate(utilities.getStartDate(1)).getTime() - utilities.getDate(startDate).getTime()), TimeUnit.MILLISECONDS) + 1;
                if (flag && daysDifference > updatedMachineFuelConsumptionData.size()) {
                    List<Date> list = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
                    for (MachineFuelConsumptionData fuelConsumptionData : updatedMachineFuelConsumptionData) {
                        list.remove(fuelConsumptionData.getDay());
                        if (!StringUtils.isEmpty(fuelConsumptionData.getFuelLevel()) && fuelConsumptionData.getFuelLevel().equals("NA"))
                            removeMachineFuelConsumptionNA.add(fuelConsumptionData);
                    }
                    if (!removeMachineFuelConsumptionNA.isEmpty())
                        updatedMachineFuelConsumptionData.removeAll(removeMachineFuelConsumptionNA);

                    for (Date date : list) {
                        updatedMachineFuelConsumptionData.add(new MachineFuelConsumptionData(date, null, null, machine.getPlatform(), null, machine));
                    }

                    updatedMachineFuelConsumptionData.sort(new Comparator<MachineFuelConsumptionData>() {
                        @Override
                        public int compare(MachineFuelConsumptionData o1, MachineFuelConsumptionData o2) {
                            return o1.getDay().compareTo(o2.getDay());
                        }
                    });
                } else {
                    for (MachineFuelConsumptionData fuelConsumptionData : updatedMachineFuelConsumptionData) {
                        if (!StringUtils.isEmpty(fuelConsumptionData.getFuelLevel()) && fuelConsumptionData.getFuelLevel().equals("NA"))
                            removeMachineFuelConsumptionNA.add(fuelConsumptionData);
                    }
                    if (!removeMachineFuelConsumptionNA.isEmpty())
                        updatedMachineFuelConsumptionData.removeAll(removeMachineFuelConsumptionNA);
                }
                List<MachineUtilizationData> updatedMachineUtililizationData;
                List<MachinePerformanceData> updatedMachinePerformanceData;
                updatedMachineUtililizationData = machineUtilizationDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                if (daysDifference > updatedMachineUtililizationData.size()) {
                    List<Date> list = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
                    for (MachineUtilizationData utilizationData : updatedMachineUtililizationData) {
                        list.remove(utilizationData.getDay());
                    }
                    for (Date date : list) {
                        updatedMachineUtililizationData.add(new MachineUtilizationData(date, null, machine.getModel(), null, machine.getPlatform(), machine, null));
                    }
                    Collections.sort(updatedMachineUtililizationData, new Comparator<MachineUtilizationData>() {
                        @Override
                        public int compare(MachineUtilizationData o1, MachineUtilizationData o2) {
                            return o1.getDay().compareTo(o2.getDay());
                        }
                    });
                }
                updatedMachinePerformanceData = machinePerformanceDataRepository.findByVinAndDayBetweenOrderByDayAsc(vin, utilities.getDate(startDate), utilities.getDate(endDate));
                if (daysDifference > updatedMachinePerformanceData.size()) {
                    List<Date> list = utilities.getDateMap(utilities.getDate(startDate), utilities.getDate(utilities.getStartDate(1)));
                    for (MachinePerformanceData performaceData : updatedMachinePerformanceData) {
                        list.remove(performaceData.getDay());
                    }
                    for (Date date : list) {
                        updatedMachinePerformanceData.add(new MachinePerformanceData(date, machine.getModel(), machine.getPlatform(), null, null, null, machine));
                    }
                    Collections.sort(updatedMachinePerformanceData, new Comparator<MachinePerformanceData>() {
                        @Override
                        public int compare(MachinePerformanceData o1, MachinePerformanceData o2) {
                            return o1.getDay().compareTo(o2.getDay());
                        }
                    });
                }
                ms = new MachineDetailResponse();
                ms.setUpdatedFuelList(updatedMachineFuelConsumptionData);
                ms.setUpdatedPerformanceList(updatedMachinePerformanceData);
                ms.setUpdatedUtilizationList(updatedMachineUtililizationData);
            }
            assert machine != null;
            log.info("Details Report Success :{} - {}", machine.getVin(), machine.getPremiumFlag());
            return ms;
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("getMachineDetailResponseListV3 :Issue faced while retriving machine details- {}", ex.getMessage() + "-" + machine.getVin());
            throw new ProcessCustomError("Issue faced while retriving machine details V3", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
