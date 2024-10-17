package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.alerts.constants.EventType;
import com.wipro.jcb.livelink.app.alerts.constants.MessagesList;
import com.wipro.jcb.livelink.app.alerts.dto.AlertResponse;
import com.wipro.jcb.livelink.app.alerts.entity.Alert;
import com.wipro.jcb.livelink.app.alerts.entity.Machine;
import com.wipro.jcb.livelink.app.alerts.entity.MachineServiceHistory;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.repo.AlertRepository;
import com.wipro.jcb.livelink.app.alerts.repo.MachineServiceHistoryRepo;
import com.wipro.jcb.livelink.app.alerts.service.AlertInfoResponeService;
import com.wipro.jcb.livelink.app.alerts.service.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-10-2024
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class AlertInfoResponseServiceImpl implements AlertInfoResponeService {

    @Value("${livelinkserver.loadHistoricalDataForServiceHistoryDays}")
    String loadServiceHistDays;

    @Autowired
    MachineServiceHistoryRepo machineServiceHistoryRepo;

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    Utilities utilities;

    @Override
    public AlertResponse getAlerts(String userName, String startDate, String endDate, int pageNumber, int pageSize,
                                   String filter, String search, Boolean isVTwo) throws ProcessCustomError {
        log.info("getAlerts: Received request for alerts with parameters: userName={}, startDate={}, endDate={}, pageNumber={}, pageSize={}, filter={}, search={}, isVTwo={}",
                userName, startDate, endDate, pageNumber, pageSize, filter, search, isVTwo);
        long startTime = System.currentTimeMillis();
        final AlertResponse alertResponse = new AlertResponse();
        try {
            final List<AlertObject> health = new ArrayList<>();
            final List<AlertObject> security = new ArrayList<>();
            final List<AlertObject> utilization = new ArrayList<>();
            final List<AlertObject> location = new ArrayList<>();
            AlertCount alertCount = null;

            // If it's the first page, fetch alert counts
            if (pageNumber == 0) {
                log.debug("getAlerts: Fetching alert counts.");
                List<AlertCountByEventType> alertList = getAlertCounts(userName, search, isVTwo);
                alertCount = populateAlertCount(alertList);
            }

            // Apply filter if provided
            if (!"optional".equals(filter) && filter != null) {
                log.debug("getAlerts: Applying filter: {}", filter);
                final List<String> filterList = new ArrayList<>(Arrays.asList(filter.split(",")));
                if ("optional".equals(search)) {
                    log.debug("getAlerts: Fetching alerts by filter only.");
                    fetchAlertsByFilter(userName, filterList, pageNumber, pageSize, isVTwo, health, security, location, utilization, startDate, endDate);
                } else {
                    log.debug("getAlerts: Fetching alerts by filter and search.");
                    fetchAlertsByFilterAndSearch(userName, search, filterList, pageNumber, pageSize, health, security, location, utilization, startDate, endDate);
                }
            } else {
                log.debug("getAlerts: Fetching alerts without filter.");
                if ("optional".equals(search)) {
                    log.debug("getAlerts: Fetching alerts by eventType only.");
                    fetchAlertsByEventType(userName, pageNumber, pageSize, isVTwo, health, security, location, utilization, startDate, endDate);
                } else {
                    log.debug("getAlerts: Fetching alerts by search only.");
                    fetchAlertsBySearch(userName, search, pageNumber, pageSize, health, security, location, utilization, startDate, endDate);
                }
            }

            // Populate the response object
            alertResponse.setHealth(health);
            alertResponse.setSecurity(security);
            alertResponse.setUtilization(utilization);
            alertResponse.setLocation(location);
            alertResponse.setAlertCount(alertCount);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("getAlerts: Completed in {} milliseconds for user {} and search term {}", elapsedTime, userName, search);
            return alertResponse;
        } catch (final Exception ex) {
            log.error("getAlerts: Internal Server Error for user {} and search term {}. Error: {}", userName, search, ex.getMessage(), ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to get alert counts based on event type
    private List<AlertCountByEventType> getAlertCounts(String userName, String search, Boolean isVTwo) {
        if (!isVTwo) {
            return alertRepository.getAlertCountByEventType(userName, false, true);
        } else {
            return "optional".equals(search) ?
                    alertRepository.getAlertCountByEventType(userName, true) :
                    alertRepository.getAlertCountByEventTypeLikeVin(userName, true, search);
        }
    }

    // Helper method to populate the AlertCount object from a list of AlertCountByEventType objects
    private AlertCount populateAlertCount(List<AlertCountByEventType> alertList) {
        AlertCount alertCount = new AlertCount();
        for (AlertCountByEventType alertCountByEventType : alertList) {
            switch (alertCountByEventType.getEventType()) {
                case Health:
                    alertCount.setHealth(alertCountByEventType.getMachineCount().intValue());
                    break;
                case Security:
                    alertCount.setSecurity(alertCountByEventType.getMachineCount().intValue());
                    break;
                case Landmark:
                    alertCount.setLocation(alertCountByEventType.getMachineCount().intValue());
                    break;
                case Service:
                    alertCount.setService(alertCountByEventType.getMachineCount().intValue());
                    break;
                case Utilization:
                    alertCount.setUtilization(alertCountByEventType.getMachineCount().intValue());
                    break;
                default:
                    break;
            }
        }
        return alertCount;
    }

    // Helper method to fetch alerts based on filter criteria
    private void fetchAlertsByFilter(String userName, List<String> filterList, int pageNumber, int pageSize, Boolean isVTwo,
                                     List<AlertObject> health, List<AlertObject> security, List<AlertObject> location, List<AlertObject> utilization,
                                     String startDate, String endDate) throws ProcessCustomError {
        for (EventType eventType : EventType.values()) {
            if (!eventType.equals(EventType.Service)) {
                final List<Alert> alerts = isVTwo ?
                        alertRepository.getAlertsUsingFilterVTwo(userName, eventType, filterList, PageRequest.of(pageNumber, pageSize)) :
                        alertRepository.getAlertsUsingFilter(userName, eventType, filterList, PageRequest.of(pageNumber, pageSize));
                loadAlertsByType(alerts, eventType, health, security, location, utilization, startDate, endDate);
            }
        }
    }

    // Helper method to fetch alerts based on filter and search criteria
    private void fetchAlertsByFilterAndSearch(String userName, String search, List<String> filterList, int pageNumber, int pageSize,
                                              List<AlertObject> health, List<AlertObject> security, List<AlertObject> location, List<AlertObject> utilization,
                                              String startDate, String endDate) throws ProcessCustomError {
        for (EventType eventType : EventType.values()) {
            if (!eventType.equals(EventType.Service)) {
                final List<Alert> alerts = alertRepository.getAlertsUsingFilterSearch(userName, eventType, search, filterList, PageRequest.of(pageNumber, pageSize));
                loadAlertsByType(alerts, eventType, health, security, location, utilization, startDate, endDate);
            }
        }
    }

    // Helper method to fetch alerts based on event type
    private void fetchAlertsByEventType(String userName, int pageNumber, int pageSize, Boolean isVTwo,
                                        List<AlertObject> health, List<AlertObject> security, List<AlertObject> location, List<AlertObject> utilization,
                                        String startDate, String endDate) throws ProcessCustomError {
        for (EventType eventType : EventType.values()) {
            if (!eventType.equals(EventType.Service)) {
                final List<Alert> alerts = isVTwo ?
                        alertRepository.findAlertByEventTypeOrderByEventGeneratedTimeDesc(userName, eventType, PageRequest.of(pageNumber, pageSize)) :
                        alertRepository.findAlertByEventType(userName, eventType, PageRequest.of(pageNumber, pageSize));
                loadAlertsByType(alerts, eventType, health, security, location, utilization, startDate, endDate);
            }
        }
    }

    // Helper method to fetch alerts based on search criteria
    private void fetchAlertsBySearch(String userName, String search, int pageNumber, int pageSize,
                                     List<AlertObject> health, List<AlertObject> security, List<AlertObject> location, List<AlertObject> utilization,
                                     String startDate, String endDate) throws ProcessCustomError {
        for (EventType eventType : EventType.values()) {
            if (!eventType.equals(EventType.Service)) {
                final List<Alert> alerts = alertRepository.getAlertsUsingSearch(userName, eventType, search, PageRequest.of(pageNumber, pageSize));
                loadAlertsByType(alerts, eventType, health, security, location, utilization, startDate, endDate);
            }
        }
    }

    // Helper method to load alert details based on event type
    private void loadAlertsByType(List<Alert> alerts, EventType eventType, List<AlertObject> health, List<AlertObject> security,
                                  List<AlertObject> location, List<AlertObject> utilization, String startDate, String endDate) throws ProcessCustomError {
        if (!alerts.isEmpty()) {
            switch (eventType) {
                case Health:
                    loadAlertDetailsFromList(health, alerts, startDate, endDate);
                    break;
                case Security:
                    loadAlertDetailsFromList(security, alerts, startDate, endDate);
                    break;
                case Landmark:
                    loadAlertDetailsFromList(location, alerts, startDate, endDate);
                    break;
                case Utilization:
                    loadAlertDetailsFromList(utilization, alerts, startDate, endDate);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public AlertInfoResponse getAlertInfo(final Alert alert, String startDate, String endDate) throws ProcessCustomError {
        log.info("getAlertInfo: Received request for alert info with alertId={}, startDate={}, endDate={}", alert.getId(), startDate, endDate);
        try {
            final Machine machine = alert.getMachine();
            final List<ServiceHistoryDetails> historyLine = new ArrayList<>();
            final String start = utilities.getStartDate(Integer.parseInt(loadServiceHistDays));
            List<MachineServiceHistory> serviceHistory = machineServiceHistoryRepo.findByVinAndServiceDateBetweenOrderByServiceDateDesc(alert.getVin(), utilities.getDate(start),
                    utilities.getDate(endDate));

            // Populate service history details
            for (final MachineServiceHistory tempObj : serviceHistory) {
                historyLine.add(new ServiceHistoryDetails(tempObj.getServiceDoneAt(), tempObj.getJobCardNumber(),
                        tempObj.getComments()));
            }

            if (machine != null) {
                log.debug("getAlertInfo: Returning alert info for alertId={}", alert.getId());
                return new AlertInfoResponse(new AlertInfoData(alert.getEventDescription(),
                        "NA".equals(alert.getLocation()) ? "-" : alert.getLocation(), machine.getPlatform(),
                        machine.getDealer().getName(), machine.getDealer().getPhonenumber(), alert.getEventType(), "-",
                        machine.getModel(), alert.getIsDtcAlert()), historyLine);
            } else {
                log.warn("getAlertInfo: Machine not found for alertId={}", alert.getId());
                return new AlertInfoResponse(new AlertInfoData(), new ArrayList<>());
            }
        } catch (final Exception ex) {
            log.error("getAlertInfo: Error fetching alert info for alertId={}. Error: {}", alert.getId(), ex.getMessage(), ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to load alert details from a list of Alert objects
    private void loadAlertDetailsFromList(final List<AlertObject> alertObjectList, final List<Alert> alerts,
                                          String startDate, String endDate) throws ProcessCustomError {
        log.debug("loadAlertDetailsFromList: Loading alert details for {} alerts.", alerts.size());
        try {
            for (final Alert alert : alerts) {
                final Machine machine = alert.getMachine();
                alertObjectList.add(new AlertObject(alert.getId(), alert.getVin(), alert.getEventName(),
                        alert.getEventLevel(), alert.getEventGeneratedTime(), machine.getTag(),
                        machine.getThumbnail(), alert.getReadFlag(), machine.getPlatform(),
                        getAlertInfo(alert, startDate, endDate), alert.getIsOpen(), alert.getIsDtcAlert()));
            }
        } catch (final Exception ex) {
            log.error("loadAlertDetailsFromList: Internal Server Error. Error: {}", ex.getMessage(), ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}