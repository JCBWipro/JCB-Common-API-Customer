package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.commonUtils.DateValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.DoubleValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.IntegerValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.FuelLevelNAConstant;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.EngineHistoryDataListV2;
import com.wipro.jcb.livelink.app.machines.dto.FuelHistoryDataListV2;
import com.wipro.jcb.livelink.app.machines.dto.MachineDetailResponse;
import com.wipro.jcb.livelink.app.machines.dto.MachineResponseV2;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineEnginestatusHistory;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedLocation;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.*;
import com.wipro.jcb.livelink.app.machines.service.MachineDetailResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wipro.jcb.livelink.app.machines.constants.AppServerConstants.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class MachineResponseServiceImpl implements MachineResponseService {

    @Autowired
    Utilities utilities;

    @Autowired
    MachineRepository machineRepository;

    @Autowired
    MachineFuelHistoryDataRepo machineFuelHistoryDataRepo;

    @Autowired
    MachineFeedParserDataRepo machineFeedParserDataRepo;

    @Autowired
    MachineFeedLocationRepo machineFeedLocationRepo;

    @Autowired
    MachineEngineStatusHistoryDataRepo machineEngineStatusHistoryDataRepo;

    @Autowired
    MachineDetailResponseService machineDetailResponseService;

    @Autowired
    MachineService machineService;

    @Value("${livelinkserver.resttemplateurl}")
    String restTemplateUrl;

    @Value("${server.evn.baseurl}")
    String env;

    private void loadMachineResponseListV2(final Machine machine, final List<MachineResponseV2> machineResponse,
                                           final String machineStartDateRange, final String machineEndDateRange,
                                           Boolean skipReports) throws ProcessCustomError {

        final String vin = machine.getVin();
        log.debug("Loading machine response data for VIN: {}", vin);

        try {
            // Prepare data structures for fuel and engine history
            List<FuelHistoryDataListV2> fuelHistoryDayDataList = new ArrayList<>();
            List<EngineHistoryDataListV2> engineHistoryDayDataList = new ArrayList<>();

            log.debug("Fetching MachineFeedParserData for VIN: {}", vin);
            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);

            log.debug("Fetching MachineFeedLocation for VIN: {}", vin);
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);

            // Date formatting for utilization data
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone(timezone));

            // Determine if fuel data should be excluded for this machine
            boolean excludeFuelData = shouldExcludeFuelData(vin, machine.getPlatform());
            if (excludeFuelData) {
                fuelHistoryDayDataList = null;
                log.debug("Fuel data excluded for VIN: {}", vin);
            }

            // Retrieve and process historical data if not skipped
            if (skipReports == null || !skipReports) {
                log.debug("Retrieving historical data for VIN: {}", vin);
                for (int i = 6; i >= 0; i--) {
                    Date startDate = utilities.getDate(utilities.getStartDate(i));
                    Date endDate = utilities.getDate(utilities.getStartDate(i - 1));

                    // Process and add engine history data
                    log.debug("Preparing engine history data for VIN: {}, startDate: {}, endDate: {}", vin, startDate, endDate);
                    engineHistoryDayDataList.add(prepareEngineHistoryData(vin, startDate, endDate, i));

                    // Process and add fuel history data if not excluded
                    if (!excludeFuelData) {
                        log.debug("Preparing fuel history data for VIN: {}, startDate: {}, endDate: {}", vin, startDate, endDate);
                        fuelHistoryDayDataList.add(prepareFuelHistoryData(vin, startDate, endDate, i));
                    }
                }
            } else {
                // If skipping reports, retrieve only the latest engine status
                Date startDate = utilities.getDate(utilities.getStartDate(0));
                Date endDate = utilities.getDate(utilities.getStartDate(-1));
                log.debug("Fetching latest engine status for VIN: {}, startDate: {}, endDate: {}", vin, startDate, endDate);
                MachineEnginestatusHistory engineStatusHistory =
                        machineEngineStatusHistoryDataRepo.findByVinAndBetweenDate(vin, startDate, endDate);

                // Update engine status based on latest history
                if (engineStatusHistory != null) {
                    boolean engineStatus = engineStatusHistory.getDateTime().after(machine.getStatusAsOnTime())
                            ? engineStatusHistory.getIsEngineOn()
                            : "on".equalsIgnoreCase(machine.getEngineStatus());
                    machine.setEngineStatus(String.valueOf(engineStatus));
                    log.debug("Updated engine status for VIN: {} to: {}", vin, engineStatus);
                }
            }

            // Retrieve and set machine details
            log.debug("Fetching machine details for VIN: {}", vin);
            final MachineDetailResponse machineDetails =
                    machineDetailResponseService.getMachineDetailResponseListV2(machine, machineFeedParserData, skipReports);

            // Update engine status based on latest engine history data
            log.debug("Updating engine status in machine details for VIN: {}", vin);
            updateEngineStatus(machine, machineDetails, engineHistoryDayDataList);

            // Determine machine service status
            log.debug("Determining machine service status for VIN: {}", vin);
            ServiceStatus machineServiceStatus = machineService.getMachineServiceHistoryStatus(machine);

            if (machineServiceStatus == null) {
                log.warn("Machine service status is null for VIN: {}", vin);
                machineServiceStatus = ServiceStatus.NORMAL;
            }

            // Build and add the machine response based on available data
            log.debug("Building machine response for VIN: {}", vin);
            buildMachineResponse(machine, machineResponse, machineStartDateRange, machineEndDateRange,
                    machineFeedParserData, machineFeedLocation, format, machineDetails, machineServiceStatus,
                    fuelHistoryDayDataList, engineHistoryDayDataList);

        } catch (Exception e) {
            log.error("Error loading machine response data for VIN {}: {}", vin, e.getMessage(), e);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Checks if fuel data should be excluded for a given VIN and platform.
     *
     * @param vin      The machine's VIN.
     * @param platform The machine's platform.
     * @return True if fuel data should be excluded, false otherwise.
     */
    private boolean shouldExcludeFuelData(String vin, String platform) {
        return (!FuelLevelNAConstant.getExceptionMachines().contains(vin))
                && FuelLevelNAConstant.getFuellevelnaconfig().containsKey(platform)
                && FuelLevelNAConstant.getFuellevelnaconfig().get(platform).contains(vin.substring(3, 8));
    }

    /**
     * Prepares engine history data for a given date range.
     *
     * @param vin       The machine's VIN.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @param i         The index for data processing logic.
     * @return The prepared engine history data.
     */
    private EngineHistoryDataListV2 prepareEngineHistoryData(String vin, Date startDate, Date endDate, int i) {
        EngineHistoryDataListV2 engineHistoryData = new EngineHistoryDataListV2();
        engineHistoryData.date = startDate;

        if (i <= 2) {
            List<MachineEngineStatusHistoryData> engineStatusList =
                    machineEngineStatusHistoryDataRepo.getEngineDetails(vin, startDate, endDate);
            engineHistoryData.timestamps = new ArrayList<>();
            engineHistoryData.values = new ArrayList<>();

            for (MachineEngineStatusHistoryData engineStatus : engineStatusList) {
                engineHistoryData.timestamps.add(new DateValue(engineStatus.getData()));
                engineHistoryData.values.add(new IntegerValue(engineStatus.getStatus()));
            }
        } else {
            engineHistoryData.timestamps = new ArrayList<>();
            engineHistoryData.values = new ArrayList<>();
        }

        return engineHistoryData;
    }

    /**
     * Prepares fuel history data for a given date range.
     *
     * @param vin       The machine's VIN.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @param i         The index for data processing logic.
     * @return The prepared fuel history data.
     */
    private FuelHistoryDataListV2 prepareFuelHistoryData(String vin, Date startDate, Date endDate, int i) {
        FuelHistoryDataListV2 fuelHistoryData = new FuelHistoryDataListV2();
        fuelHistoryData.date = startDate;

        if (i <= 2) {
            List<MachineFuelHistoryData> fuelDetailsList =
                    machineFuelHistoryDataRepo.getFuelDetails(vin, startDate, endDate);
            fuelHistoryData.timestamps = new ArrayList<>();
            fuelHistoryData.values = new ArrayList<>();

            for (MachineFuelHistoryData fuelData : fuelDetailsList) {
                fuelHistoryData.timestamps.add(new DateValue(fuelData.getData()));
                fuelHistoryData.values.add(new DoubleValue(fuelData.getValues()));
            }
        } else {
            fuelHistoryData.timestamps = new ArrayList<>();
            fuelHistoryData.values = new ArrayList<>();
        }

        return fuelHistoryData;
    }

    /**
     * Updates the engine status in the machine details based on the latest engine history.
     *
     * @param machine                  The machine entity.
     * @param machineDetails           The machine details response object.
     * @param engineHistoryDayDataList The list of engine history data.
     */
    private void updateEngineStatus(Machine machine, MachineDetailResponse machineDetails,
                                    List<EngineHistoryDataListV2> engineHistoryDayDataList) {
        if (!engineHistoryDayDataList.isEmpty()
                && engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values != null
                && !(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.isEmpty())) {

            Date latestTimestamp = engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1)
                    .timestamps.get(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).timestamps.size() - 1)
                    .getKey();

            if (latestTimestamp.after(machine.getStatusAsOnTime())) {
                boolean engineStatus = engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1)
                        .values.get(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.size() - 1)
                        .getVal() == 1;
                machineDetails.getMachine().setEngineStatus(engineStatus);
            } else {
                machineDetails.getMachine().setEngineStatus("on".equalsIgnoreCase(machine.getEngineStatus()));
            }
            machineDetails.getMachine()
                    .setEngineValue(machineDetails.getMachine().getEngineStatus() ? "on" : "off");
        }
    }

    /**
     * Builds and adds the machine response to the list.
     *
     * @param machine                  The machine entity.
     * @param machineResponse          The list to add the response to.
     * @param machineStartDateRange    The start date for data retrieval.
     * @param machineEndDateRange      The end date for data retrieval.
     * @param machineFeedParserData    The parsed feed data for the machine.
     * @param machineFeedLocation      The location data for the machine.
     * @param format                   The date format for utilization data.
     * @param machineDetails           The machine details response object.
     * @param machineServiceStatus     The service status of the machine.
     * @param fuelHistoryDayDataList   The list of fuel history data.
     * @param engineHistoryDayDataList The list of engine history data.
     */
    private void buildMachineResponse(Machine machine, List<MachineResponseV2> machineResponse,
                                      String machineStartDateRange, String machineEndDateRange,
                                      MachineFeedParserData machineFeedParserData, MachineFeedLocation machineFeedLocation,
                                      SimpleDateFormat format, MachineDetailResponse machineDetails,
                                      ServiceStatus machineServiceStatus,
                                      List<FuelHistoryDataListV2> fuelHistoryDayDataList,
                                      List<EngineHistoryDataListV2> engineHistoryDayDataList) {

        if (null != machineFeedParserData && null != machineFeedLocation
                && null != machineFeedParserData.getStatusAsOnTime()) {

            machineResponse.add(new MachineResponseV2.Builder(machine, machineDetails, machineServiceStatus)
                    .dateRange(machineStartDateRange + " - " + machineEndDateRange)
                    .feedFuelLevel(machineFeedParserData, machine)
                    .setFeedData(machineFeedParserData, machineFeedLocation, "-")
                    .supportedFeatures(machineService.doSupportFeatures(machine.getVin()))
                    .fuelHistoryDayDataList(fuelHistoryDayDataList)
                    .engineHistoryDayDataList(engineHistoryDayDataList)
                    .utilizationDateRange(
                            format.format(utilities.getDate(utilities.getStartDate(6))) + " - "
                                    + format.format(utilities.getDate(utilities.getStartDate(0))))
                    .build());

        } else {
            machine.setStatusAsOnTime(machine.getMachineFeedParserData().getStatusAsOnTime());
            machineResponse.add(new MachineResponseV2.Builder(machine, machineDetails, machineServiceStatus)
                    .dateRange(machineStartDateRange + " - " + machineEndDateRange)
                    .fuelLevel(getFuelLevel(machine.getFuelLevel()))
                    .supportedFeatures(machineService.doSupportFeatures(machine.getVin()))
                    .fuelHistoryDayDataList(fuelHistoryDayDataList)
                    .engineHistoryDayDataList(engineHistoryDayDataList)
                    .utilizationDateRange(
                            format.format(utilities.getDate(utilities.getStartDate(6))) + " - "
                                    + format.format(utilities.getDate(utilities.getStartDate(0))))
                    .build());
        }
    }

    /**
     * Determines the fuel level category based on the fuel level value.
     *
     * @param fuelLevel The fuel level value.
     * @return The fuel level category ("low", "normal", or "high").
     */
    private String getFuelLevel(Double fuelLevel) {
        if (fuelLevel >= 0 && fuelLevel < 6) {
            return "low";
        } else if (fuelLevel >= 6 && fuelLevel <= 15) {
            return "normal";
        } else {
            return "high";
        }
    }

    @Override
    public MachineListResponseV2 getMachineResponseListV2(String userName, String filter, String search, Boolean skipReports, String pageNumber, String pageSize) throws ProcessCustomError {
        long start = System.currentTimeMillis();
        log.info("getMachineResponseListV2: Request received for user: {}, filter: {}, search: {}, pageNumber: {}, pageSize: {}",
                userName, filter, search, pageNumber, pageSize);
        try {
            Long machineCount = 0L;
            Long notificationCount = 0L;
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
            format.setTimeZone(TimeZone.getTimeZone(timezone));
            final String machineStartDateRange = format.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays)));
            final String machineEndDateRange = format.format(utilities.getDate(utilities.getEndDate(-1)));
            AlertCount alertCount = new AlertCount();
            utilities.getDate(utilities.getStartDate(loadAlertsDataForDays));
            utilities.getDate(utilities.getEndDate(1));

            if ("0".equals(pageNumber)) {
                log.debug("getMachineResponseListV2: Fetching machine count for user: {}", userName);
                machineCount = machineRepository.getCountByUserName(userName);
                log.debug("getMachineResponseListV2: Machine count for user {} is: {}", userName, machineCount);

                if (machineCount == 0) {
                    log.info("getMachineResponseListV2: No machines found for user: {}. Triggering external API call...", userName);

                    //restTemplateUrl and env are configured in application.properties file
                    String url = restTemplateUrl + "/api/v1/" + env + "/user/machineszero";

                    // Log the URL for debugging
                    log.debug("getMachineResponseListV2: Calling external API at URL: {}", url);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<String> entity = new HttpEntity<>(headers);

                    ResponseEntity<String> response = new RestTemplate().exchange(
                            UriComponentsBuilder.fromHttpUrl(url)
                                    .queryParam("userName", userName)
                                    .toUriString(),
                            HttpMethod.GET,
                            entity,
                            String.class
                    );

                    if (response.getStatusCode() == HttpStatus.OK) {
                        log.info("getMachineResponseListV2: External API call successful.");
                    } else {
                        log.error("getMachineResponseListV2: Error calling external API. Status code: {}, Response body: {}",
                                response.getStatusCode(), response.getBody());
                    }
                }

                if (machineCount > 0) {
                    alertCount.setHealth(0);
                    alertCount.setSecurity(0);
                    alertCount.setLocation(0);
                    alertCount.setService(0);
                    alertCount.setUtilization(0);
                }
            }

            final List<MachineResponseV2> machineResponse = new ArrayList<>();
            log.debug("getMachineResponseListV2: Applying filters: filter={}, search={}", filter, search);

            if (!"optional".equals(filter)) {
                final List<String> filterList = new ArrayList<>(Arrays.asList(filter.split(",")));
                if ("optional".equals(search)) {
                    log.debug("getMachineResponseListV2: Fetching machines with filter: {}", filterList);
                    for (final Machine machine : machineRepository.getByUsersUserNameAndModelByCustomer(userName, filterList, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports);
                    }
                } else {
                    log.debug("getMachineResponseListV2: Fetching machines with filter: {} and search: {}", filterList, search);
                    for (final Machine machine : machineRepository.getByUsersUserNameAndModelAndSearchCriteriaByCustomer(userName, filterList, search, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports);
                    }
                }
            } else {
                if ("optional".equals(search)) {
                    log.debug("getMachineResponseListV2: Fetching all machines for user: {}", userName);
                    for (final Machine machine : machineRepository.getByUsersUserNameByCustomer(userName, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports);
                    }
                } else {
                    log.debug("getMachineResponseListV2: Fetching machines with search: {}", search);
                    for (final Machine machine : machineRepository.getByUsersUserNameAndSearchCriteriaByCustomer(userName, search, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports);
                    }
                }
            }

            log.info("getMachineResponseListV2: Retrieved {} machines for user: {}", machineResponse.size(), userName);

            // Sorting of machines on the basis of status as on time
            machineResponse.sort((o1, o2) -> o2.getStatusAsOnTime().compareTo(o1.getStatusAsOnTime()));

            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("getMachineResponseListV2: Response sent for user: {}. Total time taken: {}ms", userName, elapsedTime);
            return new MachineListResponseV2(machineResponse, alertCount, machineCount, notificationCount);

        } catch (final Exception ex) {
            log.error("getMachineResponseListV2: Error processing request for user: {}. Error: {}", userName, ex.getMessage(), ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineResponseV3 getMachineDetailsListV3(String userName, String vin) throws ProcessCustomError {
        log.info("getMachineDetailsListV3: Request received for user: {}, VIN: {}", userName, vin);
        long startTime = System.currentTimeMillis();

        try {
            // Get machine details from the database
            log.debug("getMachineDetailsListV3: Fetching machine details from database...");
            Machine machine = machineRepository.findByVinAndUserName(vin, userName);

            if (machine == null) {
                log.warn("getMachineDetailsListV3: No machine found for user: {}, VIN: {}", userName, vin);
                throw new ProcessCustomError(MessagesList.NO_RECORDS, MessagesList.NO_RECORDS, HttpStatus.NOT_FOUND);
            }

            log.debug("getMachineDetailsListV3: Machine details fetched successfully.");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));

            String machineStartDateRange = dateFormat.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays)));
            String machineEndDateRange = dateFormat.format(utilities.getDate(utilities.getEndDate(-1)));

            log.debug("getMachineDetailsListV3: Date range calculated:from {} to {}", machineStartDateRange, machineEndDateRange);

            // Load and return the machine response
            MachineResponseV3 machineResponse = loadMachineResponseListV3(machine, userName);

            log.info("getMachineDetailsListV3: Response prepared successfully. Time taken: {}ms", System.currentTimeMillis() - startTime);
            return machineResponse;

        } catch (ProcessCustomError e) {
            log.error("getMachineDetailsListV3: ProcessCustomError occurred: {}", e.getMessage(), e);
            throw e;

        } catch (Exception ex) {
            log.error("getMachineDetailsListV3: Unexpected error occurred: {}", ex.getMessage(), ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED,
                    "Failed to retrieve machine details.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MachineResponseV3 loadMachineResponseListV3(final Machine machine,
                                                        String userName) {

        log.info("loadMachineResponseListV3: Started for user: {}, VIN: {}", userName, machine.getVin());
        long startTime = System.currentTimeMillis();

        MachineResponseV3 machineResponseV3 = new MachineResponseV3();

        try {
            List<FuelHistoryDataListV2> fuelHistoryDayDataList = new ArrayList<>();
            final String vin = machine.getVin();

            log.debug("loadMachineResponseListV3: Fetching MachineFeedParserData...");
            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);

            log.debug("loadMachineResponseListV3: Fetching MachineFeedLocation...");
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);

            List<EngineHistoryDataListV2> engineHistoryDayDataList = new ArrayList<>();
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone(timezone));

            boolean fuelDataExcluded = (!FuelLevelNAConstant.getExceptionMachines().contains(vin)) &&
                    FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) &&
                    FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));

            if (fuelDataExcluded) {
                log.warn("loadMachineResponseListV3: Fuel data excluded for VIN: {}", vin);
                fuelHistoryDayDataList = null;
            }

            for (int i = 6; i >= 0; i--) {
                Date startDate = utilities.getDate(utilities.getStartDate(i));
                Date endDate = utilities.getDate(utilities.getStartDate(i - 1));

                EngineHistoryDataListV2 engineHistoryData = new EngineHistoryDataListV2();
                engineHistoryData.date = startDate;

                if (i <= 2) {
                    log.debug("loadMachineResponseListV3: Fetching engine status details...");
                    List<MachineEngineStatusHistoryData> engineStatus = machineEngineStatusHistoryDataRepo.getEngineDetails(vin, startDate, endDate);

                    engineHistoryData.timestamps = new ArrayList<>();
                    engineHistoryData.values = new ArrayList<>();

                    if (engineStatus != null && engineStatus.size() > 1) {
                        engineStatus.add(addMinutesToDate(engineStatus));
                    }

                    if (engineStatus != null) {
                        for (MachineEngineStatusHistoryData engineDetails : engineStatus) {
                            engineHistoryData.timestamps.add(new DateValue(engineDetails.getData()));
                            engineHistoryData.values.add(new IntegerValue(engineDetails.getStatus()));
                        }
                    }
                }

                engineHistoryDayDataList.add(engineHistoryData);

                if (!fuelDataExcluded) {
                    FuelHistoryDataListV2 fuelHistoryData = new FuelHistoryDataListV2();
                    fuelHistoryData.date = startDate;

                    if (i <= 2) {
                        log.debug("loadMachineResponseListV3: Fetching fuel details...");
                        List<MachineFuelHistoryData> fuelDetails = machineFuelHistoryDataRepo.getFuelDetails(vin, startDate, endDate);

                        fuelHistoryData.timestamps = new ArrayList<>();
                        fuelHistoryData.values = new ArrayList<>();

                        for (MachineFuelHistoryData fuelData : fuelDetails) {
                            fuelHistoryData.timestamps.add(new DateValue(fuelData.getData()));
                            fuelHistoryData.values.add(new DoubleValue(fuelData.getValues()));
                        }
                    }

                    fuelHistoryDayDataList.add(fuelHistoryData);
                }
            }

            String machineType = getMachineType(vin);

            log.debug("loadMachineResponseListV3: Fetching machine details...");
            final MachineDetailResponse machinedetails = machineDetailResponseService.getMachineDetailResponseListV3(machine);

            machineResponseV3 = new MachineResponseV3.Builder(machine, machinedetails, machineType)
                    .fuelHistoryDayDataList(fuelHistoryDayDataList)
                    .engineHistoryDayDataList(engineHistoryDayDataList)
                    .build();

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("loadMachineResponseListV3: Completed for VIN: {}. Time taken: {}ms", vin, elapsedTime);

        } catch (Exception e) {
            log.error("loadMachineResponseListV3: Error processing data for user: {}, VIN: {}. Error: {}",
                    userName, machine.getVin(), e.getMessage(), e);
        }

        log.info("loadMachineResponseListV3: Returning response for user: {}, VIN: {} ,Response is: {}", userName, machineResponseV3.getVin(), machineResponseV3);
        return machineResponseV3;
    }

    public String getMachineType(String vin) {
        try {
            String firmwareVersion = machineRepository.getFirmwareVersionByVin(vin);

            if (firmwareVersion == null || firmwareVersion.isEmpty()) {
                return "No Firmware";
            }

            // Extracting major version assuming dot-separated version string
            int majorVersion = Integer.parseInt(firmwareVersion.split("\\.")[0]);

            if (majorVersion >= 7 && majorVersion < 30) {
                return "LL2";
            } else if (majorVersion >= 30 && majorVersion <= 50) {
                return "LL4";
            } else if (majorVersion > 50) {
                return "LL2";
            } else {
                return "Unknown";
            }

        } catch (NumberFormatException e) {
            log.error("Invalid firmware version format for VIN: {}", vin, e);
            return "Unknown";
        }
    }

    private MachineEngineStatusHistoryData addMinutesToDate(List<MachineEngineStatusHistoryData> engineStatus) {
        if (engineStatus == null || engineStatus.isEmpty()) {
            return null;
        }

        MachineEngineStatusHistoryData lastStatus = engineStatus.get(engineStatus.size() - 1);
        Date engineValues = lastStatus.getData();

        // Using Calendar for date manipulation
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(engineValues);
        calendar.add(Calendar.MINUTE, 2);

        MachineEngineStatusHistoryData nextValue = new MachineEngineStatusHistoryData();
        nextValue.setStatus(0);
        nextValue.setData(calendar.getTime());
        return nextValue;
    }

    private Date addMinutesToDate(EngineHistoryDataListV3 engineHistoryData) {
        Date engineValues = engineHistoryData.timestamps.get(engineHistoryData.values.size() - 1);
        Date nextTime = new Date();
        nextTime.setDate(engineValues.getDate());
        nextTime.setHours(engineValues.getHours());
        nextTime.setMinutes(engineValues.getMinutes());
        nextTime.setMonth(engineValues.getMonth());
        nextTime.setSeconds(engineValues.getSeconds());
        nextTime.setTime(engineValues.getTime());
        nextTime.setYear(engineValues.getYear());
        nextTime.setMinutes(engineValues.getMinutes() + 2);
        return nextTime;
    }

    @Override
    public MachineListResponseV3 getMachineResponseListV3(String userName, String filter, String search, Boolean skipReports, String pageNumber, String pageSize, String token) throws ProcessCustomError {
        long start = System.currentTimeMillis();
        try {
            Long machineCount;
            Long notificationCount = 0L;
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
            format.setTimeZone(TimeZone.getTimeZone(timezone));
            final String machineStartDateRange = format.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays)));
            final String machineEndDateRange = format.format(utilities.getDate(utilities.getEndDate(-1)));
            AlertCount alertCount = null;
            utilities.getDate(utilities.getStartDate(loadAlertsDataForDays));
            utilities.getDate(utilities.getEndDate(1));
            machineCount = machineRepository.getCountByUserName(userName);
            if ("0".equals(pageNumber)) {
                if (machineCount == 0) {
                    log.info("Calling RestTemplate Methods For MachineZero :{}", userName);
                    final RestTemplate restTemplate = new RestTemplate();
                    //String url = "http://app.jcbdigital.in/api/v1/livelink-local/user/machineszero";
                    String url = restTemplateUrl + "/api/v1/" + env + "/user/machineszero";
                    //logger.info("URL "+url);
                    final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                    headers.add("Authorization", "Basic bGl2ZWxpbms6fGleZXxpXms=");
                    headers.add("Content-Type", "application/json");
                    headers.add("accessToken", token);
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("userName", userName);
                    HttpEntity<?> entity = new HttpEntity<>(headers);
                    restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

                }
                if (machineCount > 0) {

                    alertCount = new AlertCount();
                    alertCount.setHealth(0);
                    alertCount.setSecurity(0);
                    alertCount.setLocation(0);
                    alertCount.setService(0);
                    alertCount.setUtilization(0);
                }
            }
            final List<MachineResponseView> machineResponse = new ArrayList<>();
            if (!"optional".equals(filter)) {
                final List<String> filterList = new ArrayList<>(Arrays.asList(filter.split(",")));
                if ("optional".equals(search)) {
                    for (final Machine machine : machineRepository.getByUsersUserNameAndModelByCustomer(userName, filterList, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter present and serach optional");
                        loadMachineResponseListNewV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                } else {
                    for (final Machine machine : machineRepository.getByUsersUserNameAndModelAndSearchCriteriaByCustomer(userName, filterList, search, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter present and serach present");
                        loadMachineResponseListNewV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                }
            } else {
                if ("optional".equals(search)) {
                    for (final Machine machine : machineRepository.getByUsersUserNameByCustomer(userName, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter optional and serach optional");
                        loadMachineResponseListNewV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                } else {
                    for (final Machine machine : machineRepository.getByUsersUserNameAndSearchCriteriaByCustomer(userName, search, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter optional and serach present");
                        loadMachineResponseListNewV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                }
            }
            log.info("getMachineResponseList: GetMachineResponseList sending response for user {}", userName);

            // Sorting of machines on the basis of status as on time
            machineResponse.sort((o1, o2) -> o2.getStatusAsOnTime().compareTo(o1.getStatusAsOnTime()));
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("MachinesV3 API Duration :{}-{}-{}-{}", elapsedTime, userName, filter, search);
            return new MachineListResponseV3(machineResponse, alertCount, machineCount, notificationCount);

        } catch (final Exception ex) {
            log.error("getMachineResponseList: Machine list retrieval failed with message {}", ex.getMessage());
            log.error("getMachineResponseList: Parameter for api  {} ,{} ,{}  ", userName, filter, search);
            ex.printStackTrace();
            log.info("Exception occured for MachinesV3 API :{}Exception -{}", userName, ex.getMessage());
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void loadMachineResponseListNewV2(Machine machine, List<MachineResponseView> machineResponse, String machineStartDateRange, String machineEndDateRange, Boolean skipReports, String userName) {
        final String vin = machine.getVin();
        try {

            List<FuelHistoryDataListV2> fuelHistoryDayDataList = new ArrayList<>();

            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);
            List<EngineHistoryDataListV2> engineHistoryDayDataList = new ArrayList<>();
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
            format.setTimeZone(TimeZone.getTimeZone(timezone));
            MachineEnginestatusHistory machineEnginestatusHistory = null;
            boolean flag = true;

            if ((!FuelLevelNAConstant.getExceptionMachines().contains(vin)) && FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) && FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8))) {
                flag = false;
                fuelHistoryDayDataList = null;
            }

            if (skipReports == null || !skipReports) {
                log.info("SkipReport :-{}-{}", skipReports, userName);
                for (int i = 6; i >= 0; i--) {
                    Date startDate = utilities.getDate(utilities.getStartDate(i));
                    Date endDate = utilities.getDate(utilities.getStartDate(i - 1));
                    EngineHistoryDataListV2 engineHistoryData = new EngineHistoryDataListV2();
                    engineHistoryData.date = startDate;
                    if (i > 2) {
                        engineHistoryData.timestamps = new ArrayList<>();
                        engineHistoryData.values = new ArrayList<>();

                    } else {

                        List<MachineEngineStatusHistoryData> engineStatus = machineEngineStatusHistoryDataRepo.getEngineDetails(vin, startDate, endDate);
                        List<DateValue> date = new ArrayList<>();
                        List<IntegerValue> status = new ArrayList<>();

                        for (MachineEngineStatusHistoryData engineDetails : engineStatus) {
                            date.add(new DateValue(engineDetails.getData()));
                            status.add(new IntegerValue(engineDetails.getStatus()));

                        }
                        engineStatus.clear();
                        engineHistoryData.timestamps.addAll(date);
                        engineHistoryData.values.addAll(status);
                    }
                    engineHistoryDayDataList.add(engineHistoryData);
                    if (flag) {
                        FuelHistoryDataListV2 fuelHistoryData = new FuelHistoryDataListV2();
                        fuelHistoryData.date = startDate;
                        if (i > 2) {
                            fuelHistoryData.timestamps = new ArrayList<>();
                            fuelHistoryData.values = new ArrayList<>();
                        } else {
                            List<MachineFuelHistoryData> fuelDetails = machineFuelHistoryDataRepo.getFuelDetails(vin, startDate, endDate);
                            List<DoubleValue> values = new ArrayList<>();
                            List<DateValue> data = new ArrayList<>();
                            for (MachineFuelHistoryData fuelData : fuelDetails) {
                                values.add(new DoubleValue(fuelData.getValues()));
                                data.add(new DateValue(fuelData.getData()));

                            }
                            fuelDetails.clear();
                            fuelHistoryData.timestamps.addAll(data);
                            fuelHistoryData.values.addAll(values);
                        }
                        fuelHistoryDayDataList.add(fuelHistoryData);
                    }
                }
            } else {
                Date startDate = utilities.getDate(utilities.getStartDate(0));
                Date endDate = utilities.getDate(utilities.getStartDate(-1));
                machineEnginestatusHistory = machineEngineStatusHistoryDataRepo.findByVinAndBetweenDate(vin, startDate, endDate);
            }
            final MachineDetailResponse machinedetails = machineDetailResponseService.getMachineDetailResponseListV2(machine, machineFeedParserData, skipReports);
            if (!engineHistoryDayDataList.isEmpty() && engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values != null && !(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.isEmpty())) {
                if (engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).timestamps.get(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).timestamps.size() - 1).getKey().after(machine.getStatusAsOnTime())) {
                    machinedetails.getMachine().setEngineStatus(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.get(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.size() - 1).getVal() == 1);
                } else {
                    machinedetails.getMachine().setEngineStatus("on".equalsIgnoreCase(machine.getEngineStatus()));
                }
                machinedetails.getMachine().setEngineValue(machinedetails.getMachine().getEngineStatus() ? "on" : "off");
            } else if (machineEnginestatusHistory != null) {
                machinedetails.getMachine().setEngineStatus(machineEnginestatusHistory.getDateTime().after(machine.getStatusAsOnTime()) ? machineEnginestatusHistory.getIsEngineOn() : "on".equalsIgnoreCase(machine.getEngineStatus()));
                machinedetails.getMachine().setEngineValue(machinedetails.getMachine().getEngineStatus() ? "on" : "off");
            }

            final ServiceStatus machineServiceStatus = machineService.getMachineServiceHistoryStatus(machine);

            if (null != machineFeedParserData && null != machineFeedLocation && null != machineFeedParserData.getStatusAsOnTime()) {
                machineResponse.add(new MachineResponseView.Builder(machine, machinedetails, machineServiceStatus).dateRange(machineStartDateRange + " - " + machineEndDateRange).feedFuelLevel(machineFeedParserData, machine).setFeedData(machineFeedParserData, machineFeedLocation, "-").supportedFeatures(machineService.doSupportFeatures(machine.getVin())).fuelHistoryDayDataList(fuelHistoryDayDataList).engineHistoryDayDataList(engineHistoryDayDataList).utilizationDateRange(format.format(utilities.getDate(utilities.getStartDate(6))) + " - " + format.format(utilities.getDate(utilities.getStartDate(0)))).build());

            } else {
                machine.setStatusAsOnTime(machine.getMachineFeedParserData().getStatusAsOnTime());
                machineResponse.add(new MachineResponseView.Builder(machine, machinedetails, machineServiceStatus).dateRange(machineStartDateRange + " - " + machineEndDateRange).fuelLevel(machine.getFuelLevel() >= 0 && machine.getFuelLevel() < 6 ? "low" : machine.getFuelLevel() >= 6 && machine.getFuelLevel() <= 15 ? "normal" : "high").supportedFeatures(machineService.doSupportFeatures(machine.getVin())).fuelHistoryDayDataList(fuelHistoryDayDataList).engineHistoryDayDataList(engineHistoryDayDataList).utilizationDateRange(format.format(utilities.getDate(utilities.getStartDate(6))) + " - " + format.format(utilities.getDate(utilities.getStartDate(0)))).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occurred At MachinesV2 : {} Message{}", vin, e.getMessage());
            log.info("Exception occurred for MachinesV3 API :{} VIN {} Exception -{}", userName, vin, e.getMessage());
        }
    }

    @Override
    public Machine getMachineDetails(String vin, String userName) {
        Machine machine = null;
        try {
            machine = machineRepository.findByVinAndUserName(vin, userName);
        } catch (final Exception e) {
            e.printStackTrace();
            log.error("Failed to check vin with user {}", e.getMessage());
        }

        return machine;
    }

    @Override
    public EngineFuelHistoryUtilizationDataV2 getEngineFuelDetailData(String vin, Date startDate, Date endDate,
                                                                      String type) throws ProcessCustomError {
        EngineFuelHistoryUtilizationDataV2 engineFuelHistoryUtilizationData = new EngineFuelHistoryUtilizationDataV2();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
        format.setTimeZone(TimeZone.getTimeZone(timezone));
        Machine machine = machineRepository.findByVin(vin);
        engineFuelHistoryUtilizationData.setVin(vin);
        engineFuelHistoryUtilizationData.setDateRange(format.format(startDate) + " - " + format.format(endDate));
        List<EngineHistoryDataListV3> engineHistoryDayDataList = new ArrayList<>();
        List<FuelHistoryDataListV3> fuelHistoryDayDataList = new ArrayList<>();
        try {
            final long startDateDayDifference = TimeUnit.DAYS.convert(
                    utilities.getDate(utilities.getStartDate(0)).getTime() - startDate.getTime(),
                    TimeUnit.MILLISECONDS);
            final long endDateDayDifference = TimeUnit.DAYS.convert(
                    utilities.getDate(utilities.getStartDate(0)).getTime() - endDate.getTime(), TimeUnit.MILLISECONDS);
            boolean flag = (FuelLevelNAConstant.getExceptionMachines().contains(vin))
                    || !FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) || !FuelLevelNAConstant
                    .getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8));
            for (int i = (int) startDateDayDifference; i >= (int) endDateDayDifference; i--) {
                Date sDate = utilities.getDate(utilities.getStartDate(i));
                Date eDate = utilities.getDate(utilities.getStartDate(i - 1));
                if (type.equalsIgnoreCase("Engine")) {
                    EngineHistoryDataListV3 engineHistoryData = new EngineHistoryDataListV3();
                    engineHistoryData.date = sDate;
                    if (machine.getPremiumFlag() != null && machine.getPremiumFlag().equalsIgnoreCase("LLPlusmachines")) {
                        if (i != 0) {
                            engineHistoryData.timestamps = new ArrayList<>();
                            engineHistoryData.values = new ArrayList<>();
                            engineHistoryData.message = "Join Livelink premium to access these reports data";
                        } else {
                            engineHistoryData.timestamps = machineEngineStatusHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                            engineHistoryData.values = machineEngineStatusHistoryDataRepo.getByVin(vin, sDate, eDate);
                        }
                    } else {
                        engineHistoryData.timestamps = machineEngineStatusHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                        engineHistoryData.values = machineEngineStatusHistoryDataRepo.getByVin(vin, sDate, eDate);
                    }
                    if (engineHistoryData.values.size() > 1) {
                        Date nextTime = addMinutesToDate(engineHistoryData);
                        engineHistoryData.timestamps.add(new DateValue(nextTime).getKey());
                        engineHistoryData.values.add(new IntegerValue().getVal());
                    }
                    engineHistoryDayDataList.add(engineHistoryData);


                } else {

                    if (flag) {
                        FuelHistoryDataListV3 fuelHistoryData = new FuelHistoryDataListV3();
                        fuelHistoryData.date = sDate;
                        if (machine.getPremiumFlag() != null && machine.getPremiumFlag().equalsIgnoreCase("LLPlusmachines")) {
                            if (i != 0) {
                                fuelHistoryData.timestamps = new ArrayList<>();
                                fuelHistoryData.values = new ArrayList<>();
                                fuelHistoryData.message = "Join Livelink premium to access these reports data";
                            } else {
                                fuelHistoryData.values = machineFuelHistoryDataRepo.getFuelLevelByVin(vin, sDate, eDate);
                                fuelHistoryData.timestamps = machineFuelHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                            }
                        } else {
                            fuelHistoryData.values = machineFuelHistoryDataRepo.getFuelLevelByVin(vin, sDate, eDate);
                            fuelHistoryData.timestamps = machineFuelHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                        }
                        fuelHistoryDayDataList.add(fuelHistoryData);
                    }
                }


            }
            engineFuelHistoryUtilizationData.setEngineHistoryDayDataList(engineHistoryDayDataList);
            engineFuelHistoryUtilizationData.setFuelHistoryDayDataList(fuelHistoryDayDataList);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("Failed to process request with message {}", ex.getMessage());
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return engineFuelHistoryUtilizationData;
    }
    
	/**
	 * This getMachinetype() is to fetch MachineType by vin
	 */
	public String getMachinetype(String vin) {
		String machineType = "";
		try {
			String firmware_version = machineRepository.getFirmwareVersionByVin(vin);
			if (firmware_version != null && !firmware_version.isEmpty()) {
				log.info("Response : Vin {} Firmware Version {}", vin, firmware_version);
				int versionValues = Integer.parseInt(firmware_version.split("\\.")[0]);
                log.info("versionValues{}", versionValues);
				if (versionValues >= 7 && versionValues < 30) {
					machineType = MessagesList.LL2;
				} else if (versionValues >= 30 && versionValues <= 50) {
					machineType = MessagesList.LL4;
				} else if (versionValues > 50) {
					machineType = MessagesList.LL2;
				}

			} else {
				machineType = MessagesList.NO_FIRMWARE;
			}

		} catch (Exception e) {
			e.printStackTrace();
            log.error("Exception occurred at getMachinetype {}", vin);
		}
		return machineType;
	}
}
