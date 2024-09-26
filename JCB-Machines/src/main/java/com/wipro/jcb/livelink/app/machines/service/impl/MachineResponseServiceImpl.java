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

    private void loadMachineResponseListV2(final Machine machine, final List<MachineResponseV2> machineResponse, final String machineStartDateRange, final String machineEndDateRange, Boolean skipReports, String userName) {
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
                log.info("SkipReport.. :{}-{}", skipReports, userName);
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
                machineResponse.add(new MachineResponseV2.Builder(machine, machinedetails, machineServiceStatus).dateRange(machineStartDateRange + " - " + machineEndDateRange).feedFuelLevel(machineFeedParserData, machine).setFeedData(machineFeedParserData, machineFeedLocation, "-").supportedFeatures(machineService.doSupportFeatures(machine.getVin())).fuelHistoryDayDataList(fuelHistoryDayDataList).engineHistoryDayDataList(engineHistoryDayDataList).utilizationDateRange(format.format(utilities.getDate(utilities.getStartDate(6))) + " - " + format.format(utilities.getDate(utilities.getStartDate(0)))).build());

            } else {
                machine.setStatusAsOnTime(machine.getMachineFeedParserData().getStatusAsOnTime());
                machineResponse.add(new MachineResponseV2.Builder(machine, machinedetails, machineServiceStatus).dateRange(machineStartDateRange + " - " + machineEndDateRange).fuelLevel(machine.getFuelLevel() >= 0 && machine.getFuelLevel() < 6 ? "low" : machine.getFuelLevel() >= 6 && machine.getFuelLevel() <= 15 ? "normal" : "high").supportedFeatures(machineService.doSupportFeatures(machine.getVin())).fuelHistoryDayDataList(fuelHistoryDayDataList).engineHistoryDayDataList(engineHistoryDayDataList).utilizationDateRange(format.format(utilities.getDate(utilities.getStartDate(6))) + " - " + format.format(utilities.getDate(utilities.getStartDate(0)))).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occurred At MachinesV2 : - {} Message{}", vin, e.getMessage());
            log.info("Exception occurred for MachinesV3 API :{} Param {} Exception -{}", userName, vin, e.getMessage());
        }
    }

    @Override
    public MachineListResponseV2 getMachineResponseListV2(String userName, String filter, String search, Boolean skipReports, String pageNumber, String pageSize, String token) throws ProcessCustomError {
        long start = System.currentTimeMillis();
        try {
            Long machineCount = 0L;
            Long notificationCount = 0L;
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
            format.setTimeZone(TimeZone.getTimeZone(timezone));
            final String machineStartDateRange = format.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays)));
            final String machineEndDateRange = format.format(utilities.getDate(utilities.getEndDate(-1)));
            AlertCount alertCount = null;
            utilities.getDate(utilities.getStartDate(loadAlertsDataForDays));
            utilities.getDate(utilities.getEndDate(1));

            if ("0".equals(pageNumber)) {
                machineCount = machineRepository.getCountByUserName(userName);
                if (machineCount == 0) {
                    log.info("Calling RestTemplate Methods For MachineZero :{}", userName);
                    final RestTemplate restTemplate = new RestTemplate();
                    //String url = "http://app.jcbdigital.in/api/v1/livelink-local/user/machineszero";
                    String url = restTemplateUrl + "/api/v1/" + env + "/user/machineszero";

                    // Create headers with JWT token
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<String> entity = new HttpEntity<>(headers);

                    ResponseEntity<String> response = restTemplate.exchange(
                            UriComponentsBuilder.fromHttpUrl(url)
                                    .queryParam("userName", userName)
                                    .toUriString(),
                            HttpMethod.GET,
                            entity,
                            String.class
                    );

                    if (response.getStatusCode() != HttpStatus.OK) {
                        // Handle error response from the external API
                        log.error("Error calling external API: {}", response.getStatusCode());
                        // ...
                    }
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
            final List<MachineResponseV2> machineResponse = new ArrayList<>();
            // logger.debug("Applying filters for getMachineResponseList : " + filter);
            if (!"optional".equals(filter)) {
                final List<String> filterList = new ArrayList<>(Arrays.asList(filter.split(",")));
                if ("optional".equals(search)) {
                    for (final Machine machine : machineRepository.getByUsersUserNameAndModelByCustomer(userName, filterList, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter present and search optional");
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                } else {
                    for (final Machine machine : machineRepository.getByUsersUserNameAndModelAndSearchCriteriaByCustomer(userName, filterList, search, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter present and search present");
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                }
            } else {
                if ("optional".equals(search)) {
                    for (final Machine machine : machineRepository.getByUsersUserNameByCustomer(userName, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter optional and search optional");
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                } else {
                    for (final Machine machine : machineRepository.getByUsersUserNameAndSearchCriteriaByCustomer(userName, search, PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))) {
                        // logger.debug("Loading machine data for filter optional and search present");
                        loadMachineResponseListV2(machine, machineResponse, machineStartDateRange, machineEndDateRange, skipReports, userName);
                    }
                }
            }
            log.info("getMachineResponseList: GetMachineResponseList sending response for user. {}", userName);

            // Sorting of machines on the basis of status as on time
            machineResponse.sort((o1, o2) -> o2.getStatusAsOnTime().compareTo(o1.getStatusAsOnTime()));
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("MachinesV2 API Duration :{}-{}-{}-{}", elapsedTime, userName, filter, search);
            return new MachineListResponseV2(machineResponse, alertCount, machineCount, notificationCount);

        } catch (final Exception ex) {
            log.error("getMachineResponseList: Machine list retrieval failed with message {}", ex.getMessage());
            log.error("getMachineResponseList: Parameter for api  {} ,{} ,{}  ...", userName, filter, search);
            log.info("Exception occured for MachinesV2 API :{}Exception -{}", userName, ex.getMessage());
            ex.printStackTrace();
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineResponseV3 getMachineDetailsListV3(String userName, String vin) throws ProcessCustomError {
        try {
            // Get machine details from the database
            Machine machine = machineRepository.findByVinAndUserName(vin, userName);
            if (machine == null) {
                throw new ProcessCustomError(MessagesList.NO_RECORDS, MessagesList.NO_RECORDS, HttpStatus.NOT_FOUND);
            }
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone(timezone));
            final String machineStartDateRange = format.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays)));
            final String machineEndDateRange = format.format(utilities.getDate(utilities.getEndDate(-1)));

            // Load and return the machine response
            return loadMachineResponseListV3(machine, machineStartDateRange, machineEndDateRange, userName);

        } catch (final ProcessCustomError e) {
            // Re-throw custom errors for handling at a higher level
            throw e;

        } catch (final Exception ex) {
            // Log and handle unexpected exceptions
            log.error("getMachineDetails V3: Machine retrieval failed for user: {}, VIN: {}. Error: {}",
                    userName, vin, ex.getMessage(), ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED,
                    "Failed to retrieve machine details.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MachineResponseV3 loadMachineResponseListV3(final Machine machine, final String machineStartDateRange, final String machineEndDateRange, String userName) {
        MachineResponseV3 machineResponseV3 = new MachineResponseV3();
        long start = System.currentTimeMillis();
        try {
            List<FuelHistoryDataListV2> fuelHistoryDayDataList = new ArrayList<>();
            final String vin = machine.getVin();
            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);
            List<EngineHistoryDataListV2> engineHistoryDayDataList = new ArrayList<>();
            final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
            format.setTimeZone(TimeZone.getTimeZone(timezone));

            boolean flag = true;

            if ((!FuelLevelNAConstant.getExceptionMachines().contains(vin)) && FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) && FuelLevelNAConstant.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8))) {
                flag = false;
                fuelHistoryDayDataList = null;
            }


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
                    if (engineStatus != null && engineStatus.size() > 1) {
                        MachineEngineStatusHistoryData nextValue = addMinutesToDate(engineStatus);
                        engineStatus.add(nextValue);

                    }

                    assert engineStatus != null;
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
            String machineType = getMachineType(vin);

            final MachineDetailResponse machinedetails = machineDetailResponseService.getMachineDetailResponseListV3(machine);

            machineResponseV3 = new MachineResponseV3.Builder(machine, machinedetails, machineType).fuelHistoryDayDataList(fuelHistoryDayDataList).engineHistoryDayDataList(engineHistoryDayDataList).build();
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("MachinesdetailsV3 API Duration {}-{}-{}", elapsedTime, vin, userName);
        } catch (Exception e) {
            log.error("Error Occured : getMachineDetails V3 {}-{}-{}", userName, machine.getVin(), machine.getPremiumFlag());
            log.error("getMachineDetails V3 :{}", e.getMessage());
            log.info("Exception occured for  MachinesdetailsV3 API :{}-Param-{}Exception -{}", userName, machine.getVin(), e.getMessage());
            e.printStackTrace();
        }
        log.info("Details V3 Success :{} - {} - {}", userName, machineResponseV3.getVin(), machineResponseV3.getPremiumFlag());
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
                return "LL2"; // Assuming this is correct logic
            } else {
                return "Unknown"; // Handle cases outside the defined ranges
            }

        } catch (NumberFormatException e) {
            log.error("Invalid firmware version format for VIN: {}", vin, e);
            return "Unknown"; // Handle parsing errors gracefully
        }
    }

    private MachineEngineStatusHistoryData addMinutesToDate(List<MachineEngineStatusHistoryData> engineStatus) {
        if (engineStatus == null || engineStatus.isEmpty()) {
            return null; // Handle empty input gracefully
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
        Date engineValues = engineHistoryData.timestamps.get(engineHistoryData.values.size()-1).getKey();
        Date nextTime = new Date();
        nextTime.setDate(engineValues.getDate());
        nextTime.setHours(engineValues.getHours());
        nextTime.setMinutes(engineValues.getMinutes());
        nextTime.setMonth(engineValues.getMonth());
        nextTime.setSeconds(engineValues.getSeconds());
        nextTime.setTime(engineValues.getTime());
        nextTime.setYear(engineValues.getYear());
        nextTime.setMinutes(engineValues.getMinutes()+2);
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
                    log.info("Calling RestTemplate Methods For MachineZero :" + userName);
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
                // logger.debug("inside pageNumbers");
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
            log.info("MachinesV3 API Duration :" + elapsedTime + "-" + userName + "-" + filter + "-" + search);
            return new MachineListResponseV3(machineResponse, alertCount, machineCount, notificationCount);

        } catch (final Exception ex) {
            log.error("getMachineResponseList: Machine list retrieval failed with message {}", ex.getMessage());
            log.error("getMachineResponseList: Parameter for api  {} ,{} ,{}  ", userName, filter, search);
            ex.printStackTrace();
            log.info("Exception occured for MachinesV3 API :" + userName + "Exception -" + ex.getMessage());
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
                        List<DateValue> date = new ArrayList<DateValue>();
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
        Machine machine=null;
        try {
            machine = machineRepository.findByVinAndUserName(vin, userName);
        }catch(final Exception e) {
            e.printStackTrace();
            log.error("Failed to check vin with user " + e.getMessage());
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
                if(type.equalsIgnoreCase("Engine"))
                {
                    EngineHistoryDataListV3 engineHistoryData = new EngineHistoryDataListV3();
                    engineHistoryData.date = sDate;
                    if(machine.getPremiumFlag()!=null && machine.getPremiumFlag().equalsIgnoreCase("LLPlusmachines")) {
                        if(i!=0) {
                            engineHistoryData.timestamps = new ArrayList<>();
                            engineHistoryData.values = new ArrayList<IntegerValue>();
                            engineHistoryData.message = "Join Livelink premium to access these reports data";
                        }else {
                            engineHistoryData.timestamps = machineEngineStatusHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                            engineHistoryData.values = machineEngineStatusHistoryDataRepo.getByVin(vin, sDate, eDate);
                        }
                    }else {
                        engineHistoryData.timestamps = machineEngineStatusHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                        engineHistoryData.values = machineEngineStatusHistoryDataRepo.getByVin(vin, sDate, eDate);
                    }
                    if(engineHistoryData.values.size()>1)
                    {
                        Date nextTime = addMinutesToDate(engineHistoryData);
                        engineHistoryData.timestamps.add(new DateValue(nextTime));
                        engineHistoryData.values.add(new IntegerValue());
                    }
                    engineHistoryDayDataList.add(engineHistoryData);



                }
                else {

                    if (flag) {
                        FuelHistoryDataListV3 fuelHistoryData = new FuelHistoryDataListV3();
                        fuelHistoryData.date = sDate;
                        if(machine.getPremiumFlag()!=null && machine.getPremiumFlag().equalsIgnoreCase("LLPlusmachines")) {
                            if(i!=0) {
                                fuelHistoryData.timestamps = new ArrayList<>();
                                fuelHistoryData.values = new ArrayList<>();
                                fuelHistoryData.message = "Join Livelink premium to access these reports data";
                            }else {
                                fuelHistoryData.values = machineFuelHistoryDataRepo.getFuelLevelByVin(vin, sDate, eDate);
                                fuelHistoryData.timestamps = machineFuelHistoryDataRepo.getDateByVin(vin, sDate, eDate);
                            }
                        }else {
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
            log.error("Failed to process request with message " + ex.getMessage());
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return engineFuelHistoryUtilizationData;
    }
}
