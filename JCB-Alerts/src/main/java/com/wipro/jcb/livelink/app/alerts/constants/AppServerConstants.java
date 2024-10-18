package com.wipro.jcb.livelink.app.alerts.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 *
 */
@Component
public class AppServerConstants {
    public static final String DateFormatForMachineUpdate = "dd/MM/yyyy";
    public static final String DateFormatForMachineLiveLocation = "dd/MM/yyyy hh:mm a";
    public static final String DateFormat = "yyyy-MM-dd";
    public static final String DateFormatWithTime = "yyyy-MM-dd HH:mm";
    public static final String DateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String ExpireDateTimeFormat = "dd/MM/yyyy, hh:mm a ";
    public static final String timezone = "Asia/Kolkata";
    public static String livelinkAppServerBaseUrl;
    public static String livelinkAppServerOrgKey;
    // Server send heartbeat after every
    public static final long heartbeatInSecs = 10000;
    public static final long  NOMINTIM_LOCATION= 60000;
    public static final String DateTimeFormatTimezone = "yyyy-MM-dd'T'HH:mm:ss";
    // Check active nodes in time frame
    public static int nodesActiveTimeFrame;
    // Total Server Threshold (NO OF USERS)
    public static int serverThreshold;
    public static int alertDataFetcherTimeFrameInMilis;
    // Individual AlertFetcher Thread Threshold (NO OF USERS)
    public static int alertDataFetcherThreshold;
    // No of AlertFetcher Thread
    public static int alertDataFetcherNoOfThreads;
    // Individual MachineFetcher Thread Threshold (NO OF USERS)
    public static int machineDataFetcherThreshold;
    // No of MachineFetcher Thread
    public static int machineDataFetcherNoOfThreads;
    // Individual EveryDayFetcher Thread Threshold (NO OF USERS)
    public static int everyDataFetcherThreshold;
    // No of EveryDayFetcher Thread
    public static int everyDataFetcherNoOfThreads;
    // Fetch EveryDayFetcher no of days before
    public static int everyDataFetcherInDays;
    // Load Historical data for past
    public static int loadHistoricalDataForDays;
    public static int loadHistoricalDataForServiceHistoryDays;
    // page size predefined for wipro API
    public static int machineDataPageSize;
    public static int alertDataPageSize;
    public static int machineHistoryDataPageSize;
    // This is per machine separate call
    public static int serviceHistoryPageSize;
    public static int loadAlertsDataForDays;
    public static int compactionCoachDataPageSize;
    public static int wheelLoaderDataPageSize;
    public static int excavatorDataPageSize;
    public static int bhlDataPageSize;
    public static int intelliDigDataPageSize;
    public static String salesforceLoginUrl;
    public static String salesforceFeedbackListUrl;
    public static String salesforceUsername;
    public static String salesforcePassword;
    public static String salesforceClientId;
    public static String salesforceClientSecret;

    public static int gensetDataPageSize;
    public static int telehandlerDataPageSize;

    @Autowired
    public AppServerConstants(@Value("${livelinkserver.livelinkAppServerBaseUrl}") String livelinkAppServerBaseUrl,
                              @Value("${livelinkserver.livelinkAppServerOrgKey}") String livelinkAppServerOrgKey,
                              @Value("${datafetcher.node.nodesActiveTimeFrame}") int nodesActiveTimeFrame,
                              @Value("${datafetcher.node.serverThreshold}") int serverThreshold,
                              @Value("${datafetcher.node.alertDataFetcherTimeFrameInMilis}") int alertDataFetcherTimeFrameInMilis,
                              @Value("${datafetcher.node.alertDataFetcherThreshold}") int alertDataFetcherThreshold,
                              @Value("${datafetcher.node.alertDataFetcherNoOfThreads}") int alertDataFetcherNoOfThreads,
                              @Value("${datafetcher.node.machineDataFetcherThreshold}") int machineDataFetcherThreshold,
                              @Value("${datafetcher.node.machineDataFetcherNoOfThreads}") int machineDataFetcherNoOfThreads,
                              @Value("${datafetcher.node.everyDataFetcherThreshold}") int everyDataFetcherThreshold,
                              @Value("${datafetcher.node.everyDataFetcherNoOfThreads}") int everyDataFetcherNoOfThreads,
                              @Value("${datafetcher.node.everyDataFetcherInDays}") int everyDataFetcherInDays,
                              @Value("${livelinkserver.loadHistoricalDataForDays}") int loadHistoricalDataForDays,
                              @Value("${livelinkserver.loadHistoricalDataForServiceHistoryDays}") int loadHistoricalDataForServiceHistoryDays,
                              @Value("${livelinkserver.machineDataPageSize}") int machineDataPageSize,
                              @Value("${livelinkserver.alertDataPageSize}") int alertDataPageSize,
                              @Value("${livelinkserver.machineHistoryDataPageSize}") int machineHistoryDataPageSize,
                              @Value("${livelinkserver.serviceHistoryPageSize}") int serviceHistoryPageSize,
                              @Value("${controller.customer.machines.pageSize}") String machineInfoDataPageSize,
                              @Value("${controller.customer.alerts.pageSize}") String alertInfoDataPageSize,
                              @Value("${controller.customer.servicealerts.pageSize}") String serviceInfoDataPageSize,
                              @Value("${livelinkserver.loadAlertsDataForDays}") int LoadAlertsDataForDays,
                              @Value("${livelinkserver.compactionCoachDataPageSize}") int compactionCoachDataPageSize,
                              @Value("${livelinkserver.wheelLoaderDataPageSize}") int wheelLoaderDataPageSize,
                              @Value("${livelinkserver.excavatorDataPageSize}")int excavatorDataPageSize,
                              @Value("${livelinkserver.bhlDataPageSize}")int bhlDataPageSize,
                              @Value("${livelinkserver.gensetDataPageSize}")int gensetDataPageSize,
                              @Value("${livelinkserver.intelliDigDataPageSize}") int intelliDigDataPageSize,
                              @Value("${livelinkserver.salesforceLoginUrl}") String salesforceLoginUrl,
                              @Value("${livelinkserver.salesforceFeedbackListUrl}") String salesforceFeedbackListUrl,
                              @Value("${livelinkserver.salesforceUsername}") String salesforceUsername,
                              @Value("${livelinkserver.salesforcePassword}") String salesforcePassword,
                              @Value("${livelinkserver.salesforceClientId}") String salesforceClientId,
                              @Value("${livelinkserver.salesforceClientSecret}") String salesforceClientSecret,
                              @Value("${livelinkserver.telehandlerDataPageSize}") int telehandlerDataPageSize) {
        super();
        AppServerConstants.livelinkAppServerBaseUrl = livelinkAppServerBaseUrl;
        AppServerConstants.livelinkAppServerOrgKey = livelinkAppServerOrgKey;
        AppServerConstants.nodesActiveTimeFrame = nodesActiveTimeFrame;
        AppServerConstants.serverThreshold = serverThreshold;
        AppServerConstants.alertDataFetcherTimeFrameInMilis = alertDataFetcherTimeFrameInMilis;
        AppServerConstants.alertDataFetcherThreshold = alertDataFetcherThreshold;
        AppServerConstants.alertDataFetcherNoOfThreads = alertDataFetcherNoOfThreads;
        AppServerConstants.machineDataFetcherThreshold = machineDataFetcherThreshold;
        AppServerConstants.machineDataFetcherNoOfThreads = machineDataFetcherNoOfThreads;
        AppServerConstants.everyDataFetcherThreshold = everyDataFetcherThreshold;
        AppServerConstants.everyDataFetcherNoOfThreads = everyDataFetcherNoOfThreads;
        AppServerConstants.everyDataFetcherInDays = everyDataFetcherInDays;
        AppServerConstants.loadHistoricalDataForDays = loadHistoricalDataForDays;
        AppServerConstants.loadHistoricalDataForServiceHistoryDays = loadHistoricalDataForServiceHistoryDays;
        AppServerConstants.machineDataPageSize = machineDataPageSize;
        AppServerConstants.alertDataPageSize = alertDataPageSize;
        AppServerConstants.serviceHistoryPageSize = serviceHistoryPageSize;
        loadAlertsDataForDays = LoadAlertsDataForDays;
        AppServerConstants.machineHistoryDataPageSize = machineHistoryDataPageSize;
        AppServerConstants.compactionCoachDataPageSize = compactionCoachDataPageSize;
        AppServerConstants.wheelLoaderDataPageSize = wheelLoaderDataPageSize;
        AppServerConstants.excavatorDataPageSize = excavatorDataPageSize;
        AppServerConstants.bhlDataPageSize = bhlDataPageSize;
        AppServerConstants.gensetDataPageSize = gensetDataPageSize;
        AppServerConstants.intelliDigDataPageSize = intelliDigDataPageSize;
        AppServerConstants.salesforceLoginUrl = salesforceLoginUrl;
        AppServerConstants.salesforceFeedbackListUrl = salesforceFeedbackListUrl;
        AppServerConstants.salesforceUsername = salesforceUsername;
        AppServerConstants.salesforcePassword = salesforcePassword;
        AppServerConstants.salesforceClientId = salesforceClientId;
        AppServerConstants.salesforceClientSecret = salesforceClientSecret;
        AppServerConstants.telehandlerDataPageSize = telehandlerDataPageSize;

    }

    @PostConstruct
    public static String getDateFormat() {
        return DateFormat;
    }
    @PostConstruct
    public static String getDateFormatWithTime() {
        return DateFormat;
    }

    @PostConstruct
    public static String getDateTimeFormat() {
        return DateTimeFormat;
    }

    @PostConstruct
    public static String getLivelinkAppServerBaseUrl() {
        return livelinkAppServerBaseUrl;
    }

    @PostConstruct
    public static String getLivelinkAppServerOrgKey() {
        return livelinkAppServerOrgKey;
    }

    @PostConstruct
    public static int getNodesActiveTimeFrame() {
        return nodesActiveTimeFrame;
    }

    @PostConstruct
    public static int getServerThreshold() {
        return serverThreshold;
    }

    @PostConstruct
    public static int getAlertDataFetcherTimeFrameInMilis() {
        return alertDataFetcherTimeFrameInMilis;
    }

    @PostConstruct
    public static int getAlertDataFetcherThreshold() {
        return alertDataFetcherThreshold;
    }

    @PostConstruct
    public static int getAlertDataFetcherNoOfThreads() {
        return alertDataFetcherNoOfThreads;
    }

    @PostConstruct
    public static int getMachineDataFetcherThreshold() {
        return machineDataFetcherThreshold;
    }

    @PostConstruct
    public static int getMachineDataFetcherNoOfThreads() {
        return machineDataFetcherNoOfThreads;
    }

    @PostConstruct
    public static int getEveryDataFetcherThreshold() {
        return everyDataFetcherThreshold;
    }

    @PostConstruct
    public static int getEveryDataFetcherNoOfThreads() {
        return everyDataFetcherNoOfThreads;
    }

    @PostConstruct
    public static int getEveryDataFetcherInDays() {
        return everyDataFetcherInDays;
    }

    @PostConstruct
    public static int getLoadHistoricalDataForDays() {
        return loadHistoricalDataForDays;
    }

    @PostConstruct
    public static int getLoadHistoricalDataForServiceHistoryDays() {
        return loadHistoricalDataForServiceHistoryDays;
    }

    @PostConstruct
    public static int getMachineDataPageSize() {
        return machineDataPageSize;
    }

    @PostConstruct
    public static int getAlertDataPageSize() {
        return alertDataPageSize;
    }

    @PostConstruct
    public static int getMachineHistoryDataPageSize() {
        return machineHistoryDataPageSize;
    }

    @PostConstruct
    public static int getServiceHistoryPageSize() {
        return serviceHistoryPageSize;
    }

    @PostConstruct
    public static int getLoadAlertsDataForDays() {
        return loadAlertsDataForDays;
    }

    @PostConstruct
    public static int getWheelLoaderDataPageSize() {
        return wheelLoaderDataPageSize;
    }

    @PostConstruct
    public static int getCompactionCoachDataPageSize() {
        return compactionCoachDataPageSize;
    }

    @PostConstruct
    public static int getTelehandlerDataPageSize() {
        return telehandlerDataPageSize;
    }



    @PostConstruct
    public static String getSalesforceLoginUrl() {
        return salesforceLoginUrl;
    }

    @PostConstruct
    public static String getSalesforceFeedbackListUrl() {
        return salesforceFeedbackListUrl;
    }

    @PostConstruct
    public static String getSalesforceUsername() {
        return salesforceUsername;
    }

    @PostConstruct
    public static String getSalesforcePassword() {
        return salesforcePassword;
    }

    @PostConstruct
    public static String getSalesforceClientId() {
        return salesforceClientId;
    }

    @PostConstruct
    public static String getSalesforceClientSecret() {
        return salesforceClientSecret;
    }
}
