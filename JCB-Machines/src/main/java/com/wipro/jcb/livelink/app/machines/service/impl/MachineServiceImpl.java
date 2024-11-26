package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.cache.impl.LivelinkUserTokenServiceImpl;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.MachineLocation;
import com.wipro.jcb.livelink.app.machines.dto.MachineLocationDetail;
import com.wipro.jcb.livelink.app.machines.entity.*;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.*;
import com.wipro.jcb.livelink.app.machines.request.GeofenceRequest;
import com.wipro.jcb.livelink.app.machines.request.NotificationDetails;
import com.wipro.jcb.livelink.app.machines.service.MachineFeatureInfoService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.reports.*;
import com.wipro.jcb.livelink.app.machines.service.response.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:17-09-2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Service
@Transactional
@PropertySource("application.properties")
public class MachineServiceImpl implements MachineService {

    @Autowired
    MachineRepository machineRepository;
    @Autowired
    Utilities utilities;
    @Autowired
    ServiceScheduleRepo serviceScheduleRepo;

    @Autowired
    MachineServiceHistoryRepo machineServiceHistoryRepo;

    @Autowired
    MachineBHLDataRepo machineBHLDataRepo;

    @Autowired
    MachineLocationHistoryDataRepo machineLocationHistoryRepo;

    @Autowired
    MachineUtilizationDataRepository machineUtilizationDataRepository;

    @Autowired
    MachineFeatureInfoService machineFeatureService;

    @Autowired
    MachineFeatureDataRepo machineFeatureDataRepo;

    @Autowired
    MachineWheelLoaderDataRepository machineWheelLoaderDataRepository;

    @Autowired
    MachineExcavatorRepo machineExcavatorRepo;

    @Autowired
    MachineCompactionCoachDataRepository machineCompactionCoachDataRepository;

    @Autowired
    MachineTelehandlerDataRepository machineTelehandlerDataRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    MachineGeofenceRepository machineGeofenceRepository;

    @Autowired
	LivelinkUserTokenServiceImpl livelinkUserTokenServiceImpl;

    @Value("${machine.approachingservicedays}")
    int machineApproachingServiceDays;

    @Value("${machine.servicedueminhours}")
    Double serviceMinDueHours;

    @Value("${livelink.premium.image.machine-utilization}")
    String machineUtilizationImg;

    @Value("${livelink.premium.image.fuel-utilization}")
    String fuelUtilizationImg;

    @Value("${livelink.premium.image.machine-location}")
    String machineLocationImg;

    @Value("${livelink.premium.image.machine-operating-system}")
    String machineOSImg;

    @Value("${livelink.premium.image.machine-utilization-ios}")
    String machineUtilizationImgPath;

    @Value("${livelink.premium.image.fuel-utilization-ios}")
    String fuelUtilizationImgPath;

    @Value("${livelink.premium.image.machine-location-ios}")
    String machineLocationImgPath;

    @Value("${livelink.premium.image.machine-operating-system-ios}")
    String machineOSImgPath;

    @Value("${livelink.premium.image.machine-navigation}")
    String machineNavigationImg;

    @Value("${livelink.premium.image.machine-fencing}")
    String machineFencingImg;

    @Value("${livelink.premium.image.machine-fencing-ios}")
    String machineFencingImgIos;

    @Value("${livelink.premium.image.machine-navigation-ios}")
    String machineNavigationImgIos;

    @Value("${custom.formatter.timezone}")
    String timezone;

    @Autowired
    MachineFeedParserDataRepo machineFeedParserDataRepo;
    
    @Autowired
    MachineFeedLocationRepo machineFeedLocationRepo;
    
    @Value("${livelinkserver.connectTimeout}")
    int connectTimeout;

    @Value("${livelinkserver.readTimeout}")
    int readTimeout;


    @Override
    public Machine machineByVin(String vin) {
        return machineRepository.findByVin(vin);
    }

    @Override
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public List<String> getSuggestions(String word, String userName) {
        log.debug("generating suggestion list ");
        final List<String> suggestions = new ArrayList<>();
        log.debug("generating suggestion list for machines of Customer ");
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionModel(userName, word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionPlatform(userName, word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionTag(userName, word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionLocation(userName, word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionVin(userName, word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionSite(userName, word));
        return suggestions;
    }

    @Override
    public List<Filter> getFilters(String userName) {
        List<String> models;
        final List<Filter> filters = new ArrayList<>();
        models = machineRepository.findDistinctModelForUsers(userName);
        for (String model : models) {
            final Filter f = new Filter(model);
            filters.add(f);
        }
        // Adding all distinct platforms
        List<String> platforms;
        platforms = machineRepository.findDistinctPlatformForUsers(userName);
        for (String platform : platforms) {
            final Filter f = new Filter(platform);
            filters.add(f);
        }
        return filters;
    }

    @Override
    public MachineServiceInfo getMachineServiceInfo(String vin) throws ProcessCustomError {
        try {
            final Machine machine = machineRepository.findByVin(vin);
            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);
            if (machine != null) {
                final List<ServiceHistoryTimeline> historyLine = new ArrayList<>();

                List<MachineServiceHistory> history = machineServiceHistoryRepo.getHistoryDetails(machine.getVin());

                ServiceSchedule serviceSchedule = serviceScheduleRepo.findById(vin);

                java.util.Date overDueDate = serviceSchedule != null ? serviceSchedule.getDueDate() : null;
                if (overDueDate != null) {
                    new DateTime(Long.valueOf(overDueDate.getTime()), DateTimeZone.forID(timezone))
                            .plusDays(1).toDate();
                }
                Date serviceDoneAt = null;
                for (final MachineServiceHistory tempObj : history) {
                    historyLine.add(new ServiceHistoryTimeline(tempObj.getServiceDoneAt(), tempObj.getJobCardNumber(),
                            tempObj.getComments()));
                    // Check if the history list is not empty
                    serviceDoneAt = history.get(0).getServiceDoneAt();
                }

                if (null != machineFeedParserData && null != machineFeedLocation
                        && null != machineFeedParserData.getStatusAsOnTime()) {
                    log.info("getMachineServiceInfo: Found machine feed data for VIN: {}", vin);
                    return new MachineServiceInfo(
                            machineFeedParserData.getTotalMachineHours(),
                            serviceSchedule != null && serviceSchedule.getOverDueHours() != null && !serviceSchedule.getOverDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getOverDueHours()) : null,
                            serviceSchedule != null && serviceSchedule.getOverDueDate() != null ? serviceSchedule.getOverDueDate() : null,
                            serviceSchedule != null && serviceSchedule.getDueHours() != null && !serviceSchedule.getDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getDueHours()) : null,
                            serviceSchedule != null && serviceSchedule.getDueDate() != null ? serviceSchedule.getDueDate() : null,
                            serviceSchedule != null && serviceSchedule.getCurrentCmh() != null && !serviceSchedule.getCurrentCmh().equals("NA") ? Double.parseDouble(serviceSchedule.getCurrentCmh()) : null,
                            serviceDoneAt,
                            machine.getThumbnail(),
                            machine.getVin(),
                            machine.getModel(),
                            machine.getPlatform(),
                            machine.getTag(),
                            machine.getImage(),
                            historyLine
                    );
                } else {
                    log.info("getMachineServiceInfo: Machine feed data not found for VIN: {}, using machine data", vin);
                    return new MachineServiceInfo(
                            machine.getTotalMachineHours(),
                            serviceSchedule != null && serviceSchedule.getOverDueHours() != null && !serviceSchedule.getOverDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getOverDueHours()) : null,
                            serviceSchedule != null && serviceSchedule.getOverDueDate() != null ? serviceSchedule.getOverDueDate() : null,
                            serviceSchedule != null && serviceSchedule.getDueHours() != null && !serviceSchedule.getDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getDueHours()) : null,
                            serviceSchedule != null && serviceSchedule.getDueDate() != null ? serviceSchedule.getDueDate() : null,
                            serviceSchedule != null && serviceSchedule.getCurrentCmh() != null && !serviceSchedule.getCurrentCmh().equals("NA") ? Double.parseDouble(serviceSchedule.getCurrentCmh()) : null,
                            serviceDoneAt,
                            machine.getThumbnail(),
                            machine.getVin(),
                            machine.getModel(),
                            machine.getPlatform(),
                            machine.getTag(),
                            machine.getImage(),
                            historyLine
                    );
                }

            } else {
                log.info("getMachineServiceInfo: Machine not found for VIN: {}", vin);
                return new MachineServiceInfo();
            }
        } catch (final Exception ex) {
            log.error("getMachineServiceInfo: Internal Server Error: {}", ex.getMessage());
            throw new ProcessCustomError("Failed to process MachineServiceInfo Request",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ServiceStatus getMachineServiceStatus(final Machine machine) {
        try {

            if (machine != null && machine.getServiceDueDate() != null) {
                if (machine.getTotalMachineHours() >= machine.getServiceDueHours()) {
                    return ServiceStatus.SERVICE_OVERDUE_HOUR;
                }
                if (machine.getServiceDueDate().before(utilities.getDateTime(utilities.getStartDateTime(0)))) {
                    return ServiceStatus.SERVICE_OVERDUE_DATE;
                }
                if ((machine.getTotalMachineHours() > (machine.getServiceDueHours() - serviceMinDueHours))) {
                    return ServiceStatus.SERVICE_DUE_HOUR;
                }
                if (machine.getServiceDueDate().after(utilities.getDateTime(utilities.getStartDateTime(0)))
                        && machine.getServiceDueDate().before(
                        utilities.getDateTime(utilities.getEndDateTime(machineApproachingServiceDays)))) {
                    return ServiceStatus.SERVICE_DUE_DATE;
                }
            }
            return ServiceStatus.NORMAL;
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("MachineServiceInfo : Internal Server Error");
            return ServiceStatus.NORMAL;
        }
    }

    @Override
    public ServiceStatus getMachineServiceHistoryStatus(Machine machine) {
        return ServiceStatus.NORMAL;
    }

    @Override
    public List<MachineDownQuestion> getAllMachineDownQuestionList() {
        return List.of();
    }

    @Override
    public void setMachineTimeFence(String vin, String startTime, String endTime) throws ProcessCustomError {
        try {
            final Machine machine = machineRepository.findByVin(vin);
            machine.setStartTime(startTime);
            machine.setEndTime(endTime);
            machineRepository.save(machine);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("Error while setting timefence parameter", ex);
            throw ex;
        }
    }

    @Override
    public void setMachineGeoFenceParam(String vin, Double centerLatitude, Double centerLongitude, Long radius) {
        try {
            final Machine machine = machineRepository.findByVin(vin);
            machine.setCenterLat(centerLatitude);
            machine.setCenterLong(centerLongitude);
            machine.setRadius(radius);
            machineRepository.save(machine);
        } catch (final Exception ex) {
            log.error("Error while setting geofence parameter", ex);
            throw ex;
        }

    }

    @Override
    public boolean doSupportFeatures(String vin) {
        return false;
    }

    @Override
    public List<Filter> getUserPlatform(String userName) {
        return List.of();
    }

    @Override
    public String deleteTimefenceDetails(String vin, String userName, String tokenId) {
        return "";
    }

    @Override
    public String storeServiceCallRequestFileUpload(String vin, String customerName, String customerPhone, String contactName, String customerAlternativePhone, String machineHmr, String serviceDealerName, String model, String machineLocation, String warrantyStatus, String contractStatus, String machineStatus, String remarks, List<MultipartFile> image, String userName) {
        return "";
    }

    @Override
    public Double getFuelConsumptionData(String vin) {
        return 0.0;
    }

    @Override
    public RdMachineResponse fetchNewMachines(Date startdate, Date enddate) throws ProcessCustomError {
        RdMachineResponse response = new RdMachineResponse();
        try {

            Date startDate;
            Date endDate;
            if (startdate != null && enddate != null) {
                startDate = startdate;
                endDate = enddate;
            } else {
                startDate = utilities.getDate(utilities.getStartDate(0));
                endDate = utilities.getDateTime(utilities.getStartDateTime(0));

            }
            endDate.setHours(23);
            endDate.setMinutes(59);
            endDate.setSeconds(59);

            log.info("Date : " + startDate + " - " + endDate);
            List<RdVinImeiResponse> machineList = machineRepository.getNewMachines(startDate, endDate);
            response.setMachineList(machineList);
            log.info("fetchNewMachines Size " + machineList.size());

            return response;
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("fetchNewMachines : Internal Server Error");
            throw new ProcessCustomError("Failed to process fetchNewMachines Request",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@Override
    public Double getFuelConsumptionData(String vin) {
        Double fuelLevel = 0.0;
        try {
            Date yesterday = utilities.getDate(utilities.getStartDate(1));
            log.info("Date " + yesterday);
            if (machineFeatureService.isExist(vin)) {
                List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlag(vin, true);
                for (MachineFeatureInfo machineFeatureInfo : list) {
                    switch (machineFeatureInfo.getType()) {
                        case "CANBHL":
                            return fuelLevel = machineBHLDataRepo.getFuelConsumption(vin, yesterday);
                        case "CANWLS":
                            return fuelLevel = machineWheelLoaderDataRepository.getFuelConsumption(vin, yesterday);
                        case "CANEXCAVATOR":
                            return fuelLevel = machineExcavatorRepo.getFuelConsumption(vin, yesterday);
                        case "CANCOMPACTOR":
                            return fuelLevel = machineCompactionCoachDataRepository.getFuelConsumption(vin, yesterday);
                        case "CANTELEHANDLER":
                            return fuelLevel = machineTelehandlerDataRepository.getFuelConsumption(vin, yesterday);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fuelLevel;
    }
*/
    @Override
    public UtilizationReport getMachineUtilization(String vin, Date startDate, Date endDate) {
        MachineUtilizationResponse utilization = new MachineUtilizationResponse();
        try {
            List<MachineUtilization> machineUtilizationData = machineUtilizationDataRepository
                    .getUtilizationDetails(vin, startDate, endDate);
            utilization.setMachineUtilization(machineUtilizationData);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception occurred for Machine Utilization Report API :{}Exception -{}", vin, e.getMessage());
        }

        return utilization;
    }


    @Override
    public StandardMachineBaseResponse getStandardMachineImages() {
        StandardMachineBaseResponse standardMachineBaseResponse = new StandardMachineBaseResponse();
        List<StdMachineImagesResponse> machineImageList = new ArrayList<>();

        StdMachineImagesResponse imgResponseOne = new StdMachineImagesResponse();
        imgResponseOne.setId(1);
        imgResponseOne.setImageName("Machine Utilization");
        imgResponseOne.setImageUrl(machineUtilizationImg);
        imgResponseOne.setImagePath(machineUtilizationImgPath);

        StdMachineImagesResponse imgResponseTwo = new StdMachineImagesResponse();
        imgResponseTwo.setId(2);
        imgResponseTwo.setImageName("Fuel Utilization");
        imgResponseTwo.setImageUrl(fuelUtilizationImg);
        imgResponseTwo.setImagePath(fuelUtilizationImgPath);

        StdMachineImagesResponse imgResponseThree = new StdMachineImagesResponse();
        imgResponseThree.setId(3);
        imgResponseThree.setImageName("Machine Location");
        imgResponseThree.setImageUrl(machineLocationImg);
        imgResponseThree.setImagePath(machineLocationImgPath);

        StdMachineImagesResponse imgResponseFour = new StdMachineImagesResponse();
        imgResponseFour.setId(4);
        imgResponseFour.setImageName("Machine Operating System");
        imgResponseFour.setImageUrl(machineOSImg);
        imgResponseFour.setImagePath(machineOSImgPath);

        StdMachineImagesResponse imgResponseFive = new StdMachineImagesResponse();
        imgResponseFive.setId(5);
        imgResponseFive.setImageName("Machine Navigation");
        imgResponseFive.setImageUrl(machineNavigationImg);
        imgResponseFive.setImagePath(machineNavigationImgIos);

        StdMachineImagesResponse imgResponseSix = new StdMachineImagesResponse();
        imgResponseSix.setId(6);
        imgResponseSix.setImageName("Machine Fencing");
        imgResponseSix.setImageUrl(machineFencingImg);
        imgResponseSix.setImagePath(machineFencingImgIos);

        machineImageList.add(imgResponseOne);
        machineImageList.add(imgResponseTwo);
        machineImageList.add(imgResponseThree);
        machineImageList.add(imgResponseFour);
        machineImageList.add(imgResponseFive);
        machineImageList.add(imgResponseSix);
        standardMachineBaseResponse.setData(machineImageList);

        return standardMachineBaseResponse;
    }

    @Override
    public NotificationRemovedResponse readAllNotification(String userName) {
        return null;
    }

    @Override
    public NotificationRemovedResponse readNotification(int id, String userName) {
        return null;
    }


    @Override
    public NotificationRemovedResponse deleteAllAlertNotification(String userName) {
        NotificationRemovedResponse response = new NotificationRemovedResponse();
        try {
            log.info("Delete notification details for the user {}", userName);
            String message = "Notification(s) deleted successfully";
            response.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    @Override
    public NotificationRemovedResponse deleteNotification(Integer id, String userName) {
        NotificationRemovedResponse response = new NotificationRemovedResponse();
        try {
            log.info("Delete notification details for the user {}", userName);
            String message = "Notification(s) deleted successfully";
            response.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    @Override
    public RdMachineDetailsResponse fetchMachinesDetails(String vin) throws ProcessCustomError {
        RdMachineDetailsResponse response = new RdMachineDetailsResponse();
        try {
            log.info("Machine Details for {}", vin);
            RdMachineDetails machineDetails = machineRepository.getMachinesDetails(vin);

            if (machineDetails != null) {
                MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
                MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);
                if (machineFeedParserData != null && machineFeedLocation != null) {
                    machineDetails.setLatitude(machineFeedLocation.getLatitude());
                    machineDetails.setLongitude(machineFeedLocation.getLongitude());
                    AddressResponse address = utilities.getLocationDetails(String.valueOf(machineFeedLocation.getLatitude()),
                            String.valueOf(machineFeedLocation.getLongitude()));
                    machineDetails.setAddress(address.getLocation());

                } else {
                    Machine machine = machineRepository.findByVin(vin);
                    machineDetails.setLatitude(machine.getLatitude());
                    machineDetails.setLongitude(machine.getLongitude());
                    AddressResponse address = utilities.getLocationDetails(String.valueOf(machine.getLatitude()),
                            String.valueOf(machine.getLongitude()));
                    machineDetails.setAddress(address.getLocation());
                }
            }

            response.setMachineDetails(machineDetails);
            return response;
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("fetchNewMachines :- Internal Server Error");
            throw new ProcessCustomError("Failed to process fetchNewMachines Request",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public MachineServiceInfoV2 getMachineServiceInfoV2(String vin) throws ProcessCustomError {
        log.debug("Entering getMachineServiceInfoV2 for VIN: {}", vin);
        try {
            final Machine machine = machineRepository.findByVin(vin);
            if (machine == null) {
                log.warn("Machine not found for VIN: {}", vin);
                return new MachineServiceInfoV2();
            }

            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);
            ServiceSchedule serviceSchedule = serviceScheduleRepo.findById(vin);

            java.util.Date overDueDate = serviceSchedule != null ? serviceSchedule.getOverDueDate() : null;
            if (overDueDate != null) {
                overDueDate = new DateTime(Long.valueOf(overDueDate.getTime()), DateTimeZone.forID(timezone))
                        .plusDays(1).toDate();
            }

            final List<ServiceHistoryTimeline> historyLine = new ArrayList<>();
            List<MachineServiceHistory> historyLineNew = machineServiceHistoryRepo.getHistoryDetails(machine.getVin());

            for (final MachineServiceHistory tempObj : historyLineNew) {
                historyLine.add(new ServiceHistoryTimeline(tempObj.getServiceDoneAt(), tempObj.getJobCardNumber(),
                        tempObj.getComments()));
            }

            if (machineFeedParserData != null && machineFeedLocation != null && machineFeedParserData.getStatusAsOnTime() != null) {
                log.debug("Found machine feed data for VIN: {}", vin);
                return new MachineServiceInfoV2(
                        machineFeedParserData.getTotalMachineHours(),
                        parseDoubleSafely(serviceSchedule != null ? serviceSchedule.getOverDueHours() : null),
                        overDueDate,
                        parseDoubleSafely(serviceSchedule != null ? serviceSchedule.getDueHours() : null),
                        serviceSchedule != null ? serviceSchedule.getDueDate() : null,
                        !historyLineNew.isEmpty() ? Double.parseDouble(historyLineNew.get(0).getServiceDone()) : null,
                        !historyLineNew.isEmpty() ? historyLineNew.get(0).getServiceDoneAt() : null,
                        machine.getThumbnail(),
                        machine.getVin(),
                        machine.getModel(),
                        machine.getPlatform(),
                        machine.getTag(),
                        machine.getImage(),
                        historyLine
                );
            } else {
                log.debug("Machine feed data not found for VIN: {}, using machine data", vin);
                return new MachineServiceInfoV2(
                        serviceSchedule != null ? Double.parseDouble(serviceSchedule.getCurrentCmh()) : null,
                        parseDoubleSafely(serviceSchedule != null ? serviceSchedule.getOverDueHours() : null),
                        overDueDate,
                        parseDoubleSafely(serviceSchedule != null ? serviceSchedule.getDueHours() : null),
                        serviceSchedule != null ? serviceSchedule.getDueDate() : null,
                        !historyLineNew.isEmpty() ? Double.parseDouble(historyLineNew.get(0).getServiceDone()) : null,
                        !historyLineNew.isEmpty() ? historyLineNew.get(0).getServiceDoneAt() : null,
                        machine.getThumbnail(),
                        machine.getVin(),
                        machine.getModel(),
                        machine.getPlatform(),
                        machine.getTag(),
                        machine.getImage(),
                        historyLine
                );
            }
        } catch (final Exception ex) {
            log.error("Error in getMachineServiceInfoV2 for VIN: {} - {}", vin, ex.getMessage(), ex);
            throw new ProcessCustomError("Failed to process MachineServiceInfo Request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Double parseDoubleSafely(String value) {
        if (value != null && !value.equalsIgnoreCase("NA")) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                log.warn("Invalid number format: {}", value);
            }
        }
        return null;
    }

    @Override
    public void updatePremiumFlag(String userName, String vin, String premiumFlag) throws ProcessCustomError {
        try {

            final Machine machine = machineRepository.findByVin(vin);
            if (machine != null) {
                // machineRepository.updatePremiumFlag(premiumFlag,vin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occured while updating the permium flag {}", vin);
            throw new ProcessCustomError("Failed to updating the permium flag ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public List<String> getIntelliAutoSuggetions(String word) {
        return List.of();
    }

    @Override
    public String getHigherData(String range, String date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if ("optional".equals(range)) {
                range = "500";
            }
            Date yesterDay;
            if ("optional".equals(date)) {
                yesterDay = utilities.getDate(utilities.getStartDate(1));
            } else {

                yesterDay = utilities.getDate(date);
            }

            log.info("Data " + range + "-" + dateformat.format(yesterDay));
            long averageCount = machineBHLDataRepo.getBhlAverageCountYesterday(Double.parseDouble(range), yesterDay);
            long totalFuelCount = machineBHLDataRepo.getBhlFuelCountYesterday(Double.parseDouble(range), yesterDay);
            log.info("Fuel and Average Values " + totalFuelCount + "-" + averageCount);
            if (totalFuelCount != 0 || averageCount != 0) {
                return "Higher Data Sent To Client";
            } else {
                return "No Higher Data For " + dateformat.format(yesterDay);
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception in getHigherData API" + e.getMessage());
        }
        return null;
    }

    @Override
    public String generatePremiumRequestReport() {
        return "";
    }

    @Override
    public MachineLocation getMachineLocationDetail(String vin, String pageNumber, String pageSize) {
        log.info("In getMachineLocationDetail() vin:{} pageNumber:{} pageSize:{}", vin, pageNumber, pageSize);
        MachineLocation machineDetails = new MachineLocation();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            List<MachineLocationDetail> dataList = new ArrayList<>();
            machineDetails.setVin(vin);
            machineDetails.setNextPage(true);
            Date locationLastDate = utilities.getDate(utilities.getStartDate(29));
            log.info("Ddata " + locationLastDate + "-" + pageNumber + "-" + pageSize);
            Date startDate = null;
            Date endDate = null;
            Integer startcount = 0;
            Integer endcount = Integer.parseInt(pageSize);
            if (Integer.parseInt(pageNumber) == 0) {
                startcount = 0;
                endcount = Integer.parseInt(pageSize) - 1;
            } else {
                startcount = Integer.parseInt(pageNumber) * Integer.parseInt(pageSize);
                endcount = (Integer.parseInt(pageNumber) + 1) * (Integer.parseInt(pageSize)) - 1;
            }
            if (locationLastDate.equals(utilities.getDate(utilities.getStartDate(endcount)))
                    || locationLastDate.after(utilities.getDate(utilities.getStartDate(endcount)))) {
                machineDetails.setNextPage(false);
            }
            log.info("count " + endcount + "-" + startcount);
            for (int i = endcount; i >= startcount; i--) {

                startDate = utilities.getDate(utilities.getStartDate(i));
                endDate = utilities.getDate(utilities.getStartDate(i - 1));
                MachineLocationDetail details = new MachineLocationDetail();
                details.setDate(startDate);

                MachineLocationHistoryData firstLocation = null;
                firstLocation = machineLocationHistoryRepo.getMachineFirstLocation(vin, startDate, endDate);
                MachineLocationHistoryData lastLocation = null;
                lastLocation = machineLocationHistoryRepo.getMachineLastLocation(vin, startDate, endDate);

                if (firstLocation != null && firstLocation.getLatitude() != null
                        && firstLocation.getLongitude() != null) {
                    details.setStartTime(sdf.format(firstLocation.getDateTime()));
                    details.setStartLatitude(firstLocation.getLatitude());
                    details.setStartLongitude(firstLocation.getLongitude());
                    details.setStartLocation(
                            "Start Location(" + firstLocation.getLatitude() + "," + firstLocation.getLongitude() + ")");
                }

                if (lastLocation != null && lastLocation.getLatitude() != null && lastLocation.getLongitude() != null) {
                    details.setEndTime(sdf.format(lastLocation.getDateTime()));
                    details.setEndLatitude(lastLocation.getLatitude());
                    details.setEndLongitude(lastLocation.getLongitude());
                    details.setEndLocation(
                            "End Location(" + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + ")");
                }

                dataList.add(details);
            }
            Collections.sort(dataList, new Comparator<MachineLocationDetail>() {
                @Override
                public int compare(MachineLocationDetail o1, MachineLocationDetail o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });

            machineDetails.setData(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception occurred in machineLocation detail {}", e.getMessage());
            log.info("Exception occurred for Machine Location API -{}Exception -{}", vin, e.getMessage());
        }
        return machineDetails;
    }

    @Override
    public List<LocationHistory> getMachineLocationHistory(String vin, String date) {
        Set<LocationHistory> locationHistory = new HashSet<>();
        List<LocationHistory> machineLocationHistory = new ArrayList<>();
        Date fromDate = utilities.getDate(date);
        Date toDate = utilities.getDate(date);
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.HOUR, 23);
        calender.set(Calendar.MINUTE, 59);
        calender.set(Calendar.SECOND, 59);
        toDate = calender.getTime();
        try {
            log.info("Machine Location History Data - {} - {} - {}", vin, fromDate, toDate);
            locationHistory = machineLocationHistoryRepo.getMachineLocationHistoryData(vin, fromDate, toDate);
            log.info("locationHistory {}", locationHistory.size());
            Map<String, LocationHistory> locationDetails = new HashMap<>();
            locationHistory.forEach(location -> {
                locationDetails.put(location.getDateTime(), location);
            });
            log.info("locationHistory {}", locationDetails.size());
            machineLocationHistory = new ArrayList<>(locationDetails.values());
            machineLocationHistory.sort(Comparator.comparing(LocationHistory::getDateTime));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occurred in getMachineLocationHistory{}", e.getMessage());
            log.info("Exception occurred for Machine Location History API :{}Exception -{}", vin, e.getMessage());
        }
        return machineLocationHistory;
    }

    @Override
    public UtilizationReport getFuelUtilization(String vin, Date startDate, Date endDate) {
        MachineFuelUtilizationResponse utilizationResponse = new MachineFuelUtilizationResponse();
        try {
            if (machineFeatureService.isExist(vin)) {
                List<MachineFeatureInfo> list = machineFeatureDataRepo.findByVinAndFlagOrderByDayDescLimit1(vin, true, PageRequest.of(0, 1));
                label:
                for (MachineFeatureInfo machineFeatureInfo : list) {
                    log.info("Calendar View Data VIN Type :{} - {}", vin, machineFeatureInfo.getType());
                    List<FuelConsumption> fuelConsumptionList = new ArrayList<FuelConsumption>();
                    switch (machineFeatureInfo.getType()) {
                        case "CANBHL":
                        case "HBBHL":
                            fuelConsumptionList = machineBHLDataRepo.getFuelConsumptionReport(vin, startDate, endDate);
                            utilizationResponse.setFuelUtilization(fuelConsumptionList);
                            break label;
                        case "CANWLS":
                        case "INTELLILOAD":
                            fuelConsumptionList = machineWheelLoaderDataRepository.getFuelConsumptionReport(vin, startDate, endDate);
                            utilizationResponse.setFuelUtilization(fuelConsumptionList);
                            break label;
                        case "CANEXCAVATOR":
                        case "HBEXCAVATOR":
                            fuelConsumptionList = machineExcavatorRepo.getFuelConsumptionReport(vin, startDate, endDate);
                            utilizationResponse.setFuelUtilization(fuelConsumptionList);
                            break label;
                        case "CANCOMPACTOR":
                        case "INTELLICOMPACTOR":
                            fuelConsumptionList = machineCompactionCoachDataRepository.getFuelConsumptionReport(vin, startDate, endDate);
                            utilizationResponse.setFuelUtilization(fuelConsumptionList);
                            break label;
                        case "CANTELEHANDLER":
                            fuelConsumptionList = machineTelehandlerDataRepository.getFuelConsumptionReport(vin, startDate, endDate);
                            utilizationResponse.setFuelUtilization(fuelConsumptionList);
                            break label;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception occurred for Fuel Utilization Report API :{}Exception -{}", vin, e.getMessage());
        }
        return utilizationResponse;
    }
    
    @Override
	public String setGeoFenceParam(GeofenceRequest gfSetRequest, String userName, String machineType,
			String livelinkTokenId) throws Exception {
		String geofenceResponse = "SUCCESS";
		String livelinkToken = "37aa1b15_20240522150705";
		try {
			final Client client = Client.create();
			client.setConnectTimeout(connectTimeout);
			client.setReadTimeout(readTimeout);
			final String setGeofence = AppServerConstants.livelinkAppServerBaseUrl+MessagesList.SET_LANDMARK_DETAILS;

			JSONObject object = new JSONObject();
			object.put("LoginID", userName);
			object.put("Landmark_Name", gfSetRequest.getLandmarkName().toString());
			String radiusValue[] = gfSetRequest.getRadius().toString().split("\\.");
			String radius = null;
			if (radiusValue[1].length() > 1) {
				radius = radiusValue[0] + "." + radiusValue[1].charAt(0);
			} else {
				radius = gfSetRequest.getRadius().toString();
			}
			if (gfSetRequest.getAddress() != null && !gfSetRequest.getAddress().isEmpty()) {
				String address = gfSetRequest.getAddress().replace("+", "");
				object.put("Address", address);
				log.info("Address From App - " + gfSetRequest.getVin() + "-" + address);
			} else {
				AddressResponse address = utilities.getLocationDetails(String.valueOf(gfSetRequest.getLatitude()),
						String.valueOf(gfSetRequest.getLongitude()));
				object.put("Address", address.getLocation());
				log.info("Address From Nominatim - " + gfSetRequest.getVin() + "-" + address.getLocation());
			}
			object.put("Radius", Double.parseDouble(radius));
			object.put("Latitude", String.valueOf(gfSetRequest.getLatitude()));
			object.put("Longitude", String.valueOf(gfSetRequest.getLongitude()));
			object.put("IsArrival", gfSetRequest.getIsArrival());
			object.put("IsDeparture", gfSetRequest.getIsDepature());

			if (gfSetRequest.getLandmarkId() != null && gfSetRequest.getLandmarkId() != 0) {
				object.put("Landmark_id", gfSetRequest.getLandmarkId().intValue());
			}
			object.put("MobileNumber",gfSetRequest.getMobileNumber() != null ? gfSetRequest.getMobileNumber().toString() : null);

			if (machineType != null && machineType.equals("LL4")) {
				JSONObject daytimeNotification = new JSONObject();
				if (gfSetRequest.getNotificationDetails() != null) {
					daytimeNotification.put("Push notification",
							gfSetRequest.getNotificationDetails().getPushNotification());
					daytimeNotification.put("SMS", gfSetRequest.getNotificationDetails().getSms());
					object.put("NotificationDetails", daytimeNotification);
				}
			}

			if (gfSetRequest.getVin() != null && !gfSetRequest.getVin().isEmpty()) {
				object.put("MachineList", gfSetRequest.getVin());
			}

			log.info("Set Geofence Object :" + gfSetRequest.getLandmarkId() + "-" + object);
			final WebResource geofenceDatawebResource = client.resource(setGeofence);
			final ClientResponse geofenceDataResponse = geofenceDatawebResource
					.header("Orgkey", AppServerConstants.livelinkAppServerOrgKey)
					.header("TokenId", livelinkToken)
					.type("application/json")
					.post(ClientResponse.class, object.toString());

			if (geofenceDataResponse.getStatus() == HttpServletResponse.SC_OK) {

				String responseStatus = geofenceDataResponse.getEntity(String.class);
				if (responseStatus.equalsIgnoreCase("SUCCESS")) {
					log.info("Success Data -" + gfSetRequest.getVin() + "-" + gfSetRequest.getLandmarkId());
					if (gfSetRequest.getVin() != null && !gfSetRequest.getVin().isEmpty()) {
						String machineCount[] = gfSetRequest.getVin().split(",");
						for (String machine : machineCount) {
							log.info("Data - " + machine + "-" + gfSetRequest.getLandmarkId());
							log.info("geofence details available " + machine);
							MachineGeofence machineGeofence = new MachineGeofence();
							machineGeofence.setAddress(gfSetRequest.getAddress());
							if (gfSetRequest.getLandmarkId() != null) {
								machineGeofence.setLandmarkId(gfSetRequest.getLandmarkId());
							}

							machineGeofence.setLandmarkName(gfSetRequest.getLandmarkName());
							machineGeofence.setVin(machine);
							machineGeofence.setRadius(gfSetRequest.getRadius());
							machineGeofence.setLatitude(gfSetRequest.getLatitude());
							machineGeofence.setLongitude(gfSetRequest.getLongitude());
							machineGeofence.setIsArrival(gfSetRequest.getIsArrival());
							machineGeofence.setIsDepature(gfSetRequest.getIsDepature());
							machineGeofence.setMachineType(machineType);
							if (gfSetRequest.getMobileNumber() != null)
								machineGeofence.setMobileNumber(gfSetRequest.getMobileNumber());
							if (machineType != null && machineType.equals("LL4")) {
								if (gfSetRequest.getNotificationDetails() != null) {
									machineGeofence.setSms(gfSetRequest.getNotificationDetails().getSms());
									machineGeofence.setPush(gfSetRequest.getNotificationDetails().getPushNotification());
								}
							}
							machineGeofenceRepository.save(machineGeofence);
							log.info("Data Saved - " + machineGeofence.getVin());
						}
						if (gfSetRequest.getLandmarkId() != null && gfSetRequest.getLandmarkId() != 0) {
							log.info("Geofence Data Updated Successfully");
							geofenceResponse = MessagesList.UPDATESUCCESS;
						} else {
							log.info("Geofence Data Created successfully");
							geofenceResponse = MessagesList.SUCCESS;
						}
					}
				} else {
					log.info("Geofence Create Status :" + responseStatus + "-" + gfSetRequest.getLandmarkId());
					if (responseStatus.contains("TokenId is null or Invalid")) {

						final User user = userRepository.findByUserName(userName);
						if (user != null) {
							livelinkToken = null;
							livelinkToken = utilities.updateLivelinkServerToken(user, true);
							return setGeoFenceParam(gfSetRequest, userName, machineType, livelinkToken);
						}
					} else if (responseStatus.contains("–")) {
						responseStatus = responseStatus.split("–")[1];
					}
					return responseStatus;
				}
			} else {
				log.error("Set Geofence Wipro API Response Status: " + geofenceDataResponse.getStatus()
						+ "geofenceDataResponse :" + geofenceDataResponse);

				if (geofenceDataResponse.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
					final User user = userRepository.findByUserName(userName);
					if (user != null) {
						livelinkToken = null;
						livelinkToken = utilities.updateLivelinkServerToken(user, true);
						return setGeoFenceParam(gfSetRequest, userName, machineType, livelinkToken);
					}
				} else {
					return MessagesList.FAILURE;
				}
			}
		} catch (final Exception ex) {
			log.error("Error while setting geofence parameter", ex);
			return MessagesList.FAILURE;

		}
		return geofenceResponse;
	}
    
	@Override
	public GeofenceRequest getGeofenceDetails(String vin, String userName, String livelinkTokenId) {
		MachineGeofence geofenceData = new MachineGeofence();
		GeofenceRequest geofenceResponse = new GeofenceRequest();
		String livelinkToken = "37aa1b15_20240522150705";

		log.info("userName and livelinkToken :" + userName + "-" + livelinkToken);
		final Client client = Client.create();
		client.setConnectTimeout(connectTimeout);
		client.setReadTimeout(readTimeout);
		final String getGeofenceURL = AppServerConstants.livelinkAppServerBaseUrl + MessagesList.GET_LANDMARK_DETAILS;
		final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();

		queryParams.add("VIN", vin);

		final WebResource getGeofencewebResource = client.resource(getGeofenceURL).queryParams(queryParams);
		final ClientResponse getGeofenceDataResponse = getGeofencewebResource.accept("application/json")
				.header("Orgkey", AppServerConstants.livelinkAppServerOrgKey)
				.header("TokenId", livelinkToken)
				.type("application/json")
				.get(ClientResponse.class);

		log.info("Wipro API for getGeofence :" + getGeofenceDataResponse);
		try {
			if (getGeofenceDataResponse.getStatus() == HttpServletResponse.SC_OK) {
				String goefenceStringResponse = getGeofenceDataResponse.getEntity(String.class);
				if (getGeofenceDataResponse.getLength()>1) {
					final JSONObject geofenceObj = new JSONObject(goefenceStringResponse);
					log.info("geofenceObj :" + geofenceObj);
					if (geofenceObj != null && !geofenceObj.keySet().contains("errno")) {

						geofenceData.setVin(geofenceObj.get("vin")!= null ? geofenceObj.get("vin").toString() : "");
						geofenceData.setMobileNumber(geofenceObj.get("mobileNumber")!= null ? geofenceObj.get("mobileNumber").toString() : "");
						geofenceData.setLandmarkId(geofenceObj.get("landmarkID").toString()!= null ? Integer.parseInt(geofenceObj.get("landmarkID").toString()) : 0);
						geofenceData.setLandmarkName(geofenceObj.get("landmarkName")!= null ? geofenceObj.get("landmarkName").toString() : "");

						// Set GeoFence Details
						String geofence = geofenceObj.get("geofenceDetails").toString();
						JSONObject geofenceObject = new JSONObject(geofence);
						geofenceData.setRadius(geofenceObject.get("Radius") != null? Double.valueOf(geofenceObject.get("Radius").toString()): 0.0);
						geofenceData.setLatitude(geofenceObject.get("LAT") != null ? geofenceObject.get("LAT").toString() : "");
						geofenceData.setLongitude(geofenceObject.get("LONG") != null ? geofenceObject.get("LONG").toString() : "");
						geofenceData.setAddress(geofenceObject.get("Address") != null ? geofenceObject.get("Address").toString() : "");
						geofenceData.setIsArrival(geofenceObject.get("IsArrival") != null ? geofenceObject.get("IsArrival").toString() : "");
						geofenceData.setIsDepature(geofenceObject.get("IsDeparture") != null ? geofenceObject.get("IsDeparture").toString() : "");

						// DaytimeNotification Details
						if (geofenceObj.get("notificationDetails") != null
								&& !geofenceObj.get("notificationDetails").toString().equals("null")) {
							String daytime = geofenceObj.get("notificationDetails").toString();
							JSONObject daytimeObject = new JSONObject(daytime);
							geofenceData.setPush(daytimeObject.get("Push notification") != null? daytimeObject.get("Push notification").toString() : "");
							geofenceData.setSms(daytimeObject.get("SMS") != null ? daytimeObject.get("SMS").toString() : "");
						}

						machineGeofenceRepository.save(geofenceData);
						geofenceResponse = new GeofenceRequest();
						geofenceResponse.setVin(geofenceData.getVin());
						geofenceResponse.setLandmarkName(geofenceData.getLandmarkName());
						geofenceResponse.setLandmarkId(geofenceData.getLandmarkId());
						geofenceResponse.setAddress(geofenceData.getAddress());
						geofenceResponse.setRadius(geofenceData.getRadius());
						geofenceResponse.setLatitude(geofenceData.getLatitude());
						geofenceResponse.setLongitude(geofenceData.getLongitude());
						geofenceResponse.setIsArrival(geofenceData.getIsArrival());
						geofenceResponse.setIsDepature(geofenceData.getIsDepature());
						geofenceResponse.setMobileNumber(geofenceData.getMobileNumber());

						NotificationDetails notificationDetails = new NotificationDetails();
						notificationDetails.setPushNotification(geofenceData.getPush());
						notificationDetails.setSms(geofenceData.getSms());
						geofenceResponse.setNotificationDetails(notificationDetails);

					} else {
						log.info("Error - " + goefenceStringResponse + "-" + geofenceObj.getString("message") + "- vin "
								+ vin + " - userName" + userName);
						if (goefenceStringResponse.contains("TokenId is null or Invalid")) {
							final User user = userRepository.findByUserName(userName);
							if (user != null) {
								livelinkToken = null;
								livelinkToken = utilities.updateLivelinkServerToken(user, true);
								if (livelinkToken != null) {
									return getGeofenceDetails(vin, userName, livelinkToken);
								} else {
									return geofenceResponse;
								}
							}
						} else {
							if (goefenceStringResponse.contains("No data available")) {
								MachineGeofence geofenceDetails = machineGeofenceRepository.findByVin(vin);
								if (geofenceDetails != null) {
									machineGeofenceRepository.deleteById(vin);
									log.info("Geofence data deleted for the vin " + vin);
								}
							}
						}
					}
				}
			} else {
				log.info("getgeofence response status: " + getGeofenceDataResponse.getStatus() + "-" + vin);
				if (getGeofenceDataResponse.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
					final User user = userRepository.findByUserName(userName);
					if (user != null) {
						livelinkToken = null;
						livelinkToken = utilities.updateLivelinkServerToken(user, true);
					}
				} else if (getGeofenceDataResponse.getStatus() == HttpServletResponse.SC_NO_CONTENT) {

					MachineGeofence geofenceDetails = machineGeofenceRepository.findByVin(vin);
					if (geofenceDetails != null) {
						machineGeofenceRepository.deleteById(vin);
						log.info("Geofence data deleted for the vin " + vin);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in get geofence API " + e.getMessage());
		}
		return geofenceResponse;
	}
	
	@Override
	public String deleteGeofenceDetails(GeofenceRequest geofenceParam, String userName, String livelinkTokenId) {
		String response = null;
		String livelinkToken = "37aa1b15_20240522150705";
		try {
			if (geofenceParam.getLandmarkId() != null) {

				final Client client = Client.create();
				client.setConnectTimeout(connectTimeout);
				client.setReadTimeout(readTimeout);

				final String deleteGeofence = AppServerConstants.livelinkAppServerBaseUrl+ MessagesList.DELETE_LANDMARK_DETAILS;

				JSONObject object = new JSONObject();
				object.put("LoginID", userName);
				object.put("Landmark_id", geofenceParam.getLandmarkId());

				final WebResource deletefenceDatawebResource = client.resource(deleteGeofence);
				final ClientResponse deletefenceDataResponse = deletefenceDatawebResource
						.header("Orgkey", AppServerConstants.livelinkAppServerOrgKey)
						.header("TokenId", livelinkToken)
						.type("application/json")
						.post(ClientResponse.class, object.toString());
				
				if (deletefenceDataResponse.getStatus() == HttpServletResponse.SC_OK) {

					String responseStatus = deletefenceDataResponse.getEntity(String.class);
					if (responseStatus.equalsIgnoreCase(MessagesList.SUCCESS)) {

						machineGeofenceRepository.deleteByLandmarkId(geofenceParam.getLandmarkId());
						log.info("Geofence Delete Status:{}", responseStatus);
						return responseStatus;
					} else {
						log.info("Geofence Delete Status:{}", responseStatus);
						if (responseStatus.contains("TokenId is null or Invalid")) {
							final User user = userRepository.findByUserName(userName);
							if (user != null) {
								livelinkToken = null;
								livelinkToken = utilities.updateLivelinkServerToken(user, true);
								return deleteGeofenceDetails(geofenceParam, userName, livelinkToken);
							}
						} else {
							return responseStatus;
						}
					}
				} else {
					log.error("Set Geofence Wipro API Response");
					log.error("Status:{}", deletefenceDataResponse.getStatus());
					log.error("geofenceDataResponse:{}", deletefenceDataResponse);
					log.error("VIN:{}", geofenceParam.getVin());

					if (deletefenceDataResponse.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
						final User user = userRepository.findByUserName(userName);
						if (user != null) {
							livelinkToken = null;
							livelinkToken = utilities.updateLivelinkServerToken(user, true);
							return deleteGeofenceDetails(geofenceParam, userName, livelinkToken);
						}
					} else {
						return MessagesList.FAILURE;
					}
				}
			} else {
				log.error("No data found in selected landmark " + geofenceParam.getLandmarkId());
				response = MessagesList.FAILURE;
			}
		} catch (Exception e) {
			response = MessagesList.FAILURE;
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public GeofenceLandmarkResponse getLandmarkDetails(String userName, String landmarkName, String livelinkTokenId,
			String vin) {
		long start = System.currentTimeMillis();
		String livelinkToken = "37aa1b15_20240522150705";
		GeofenceRequest getGeofence = getGeofenceDetails(vin, userName, livelinkTokenId);
		List<GeofenceRequest> geofenceList = new ArrayList<>();
		if (getGeofence.getVin()!= null && getGeofence.getLandmarkId()!= 0) {
			geofenceList.add(getGeofence);
		}
		log.info("GeofenceList :" + geofenceList.size());
		List<GeofenceRequest> geofenceResponseList = new ArrayList<>();
		GeofenceLandmarkResponse landmarkResponse = new GeofenceLandmarkResponse();
		
		final Client client = Client.create();
		client.setConnectTimeout(connectTimeout);
		client.setReadTimeout(readTimeout);
		final String getLandmarkDetailURL = AppServerConstants.livelinkAppServerBaseUrl+ MessagesList.GET_LANDMARK_DETAILS_FOR_USER;
		final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("LoginID", userName);

		log.info("Landmark API Token and user : " + livelinkToken + "-" + userName);
		try {
			final WebResource getLandmarkWebResource = client.resource(getLandmarkDetailURL).queryParams(queryParams);
			final ClientResponse getLandmarkDataResponse = getLandmarkWebResource.accept("application/json")
					.header("Orgkey", AppServerConstants.livelinkAppServerOrgKey)
					.header("TokenId", livelinkToken)
					.type("application/json")
					.get(ClientResponse.class);

			if (getLandmarkDataResponse.getStatus() == HttpServletResponse.SC_OK) {
				String LandmarkStringResponse = getLandmarkDataResponse.getEntity(String.class);
				if (!LandmarkStringResponse.contains("No data available")
						&& !LandmarkStringResponse.contains("TokenId is null or Invalid")) {
					JSONArray jsonArray = new JSONArray(LandmarkStringResponse);
					if (jsonArray.length() > 0) {
						log.info("Data Size" + jsonArray.length());
						for (int i = 0; i < jsonArray.length(); i++) {
							final JSONObject responseobj = jsonArray.getJSONObject(i);

							if (responseobj != null) {
								String vinNumber = responseobj.get("vin").toString();
								log.info("vinNumber " + vinNumber);
								if (vinNumber != null && !vinNumber.isEmpty() && !vinNumber.equals("null")) {
									log.info("Data " + getGeofence.getVin() + "-" + getGeofence.getLandmarkId() + "-"
											+ vinNumber);
									if (getGeofence.getVin() != null && getGeofence.getLandmarkId() != 0
											&& vinNumber.contains(getGeofence.getVin())) {
										log.info("List Added " + getGeofence.getVin() + "-" + vinNumber);
										GeofenceRequest geofence = geofenceList.get(0);
										geofence.setVin(vinNumber);
										geofenceList.set(0, geofence);
									} else {
										log.info("List Added " + vinNumber);
										landmarkListDetails(geofenceList, responseobj);
									}
								} else {
									log.info("landmarkID :" + responseobj.get("landmarkID").toString());
									if (responseobj.get("landmarkID").toString() != null) {
										landmarkListDetails(geofenceList, responseobj);
									}
								}
							} else {
								log.info("GetLandmark Details Empty");
							}
						}
						if (geofenceList != null && !geofenceList.isEmpty()) {

							if (!"optional".equals(landmarkName)) {
								for (GeofenceRequest landmarkDetails : geofenceList) {
									if (landmarkDetails.getLandmarkName().toLowerCase()
											.contains(landmarkName.toLowerCase())) {
										geofenceResponseList.add(landmarkDetails);
									}
								}

							} else {
								geofenceResponseList.addAll(geofenceList);
							}
						}
						landmarkResponse.setLandmark(landmarkName);
						landmarkResponse.setLandmarkDetails(geofenceResponseList);
					}
				} else {
					log.info("Empty Reposnse " + LandmarkStringResponse);
					if (LandmarkStringResponse.contains("TokenId is null or Invalid")) {
						final User user = userRepository.findByUserName(userName);
						if (user != null) {
							livelinkToken = null;
							livelinkToken = utilities.updateLivelinkServerToken(user, true);
							return getLandmarkDetails(userName, landmarkName, livelinkToken, vin);
						}
					} else {
						log.error("GetLandmarkDetailsForUser API Response " + userName + "-"
								+ getLandmarkDataResponse.getStatus());
					}
				}
			} else {
				log.error("GetLandmarkDetailsForUser API Response " + userName + "-"
						+ getLandmarkDataResponse.getStatus());
				if (getLandmarkDataResponse.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
					final User user = userRepository.findByUserName(userName);
					if (user != null) {
						livelinkToken = null;
						livelinkToken = utilities.updateLivelinkServerToken(user, true);
						return getLandmarkDetails(userName, landmarkName, livelinkToken, vin);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error in  LandmarkDetails API " + e.getMessage());
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("LandmarkDetails API Duration :" + elapsedTime + "-" + userName);
		return landmarkResponse;
	}

	private void landmarkListDetails(List<GeofenceRequest> geofenceList, final JSONObject responseobj) {
		GeofenceRequest geofenceResponse = new GeofenceRequest();
		geofenceResponse.setVin(responseobj.get("vin")!= null ? responseobj.get("vin").toString() : "");
		geofenceResponse.setMobileNumber(responseobj.get("mobileNumber")!= null ? responseobj.get("mobileNumber").toString() : "");
		geofenceResponse.setLandmarkId(responseobj.get("landmarkID").toString()!= null ? Integer.parseInt(responseobj.get("landmarkID").toString()) : 0);
		geofenceResponse.setLandmarkName(responseobj.get("landmarkName")!= null ? responseobj.get("landmarkName").toString() : "");
		
		String geofence = responseobj.get("geofenceDetails").toString();
		JSONObject geofenceObject = new JSONObject(geofence);
		geofenceResponse.setRadius(geofenceObject.get("Radius")!=null ? Double.valueOf(geofenceObject.get("Radius").toString()) : 0);
		geofenceResponse.setLatitude(geofenceObject.get("LAT")!= null ? geofenceObject.get("LAT").toString() : "");
		geofenceResponse.setLongitude(geofenceObject.get("LONG")!= null ? geofenceObject.get("LONG").toString() : "");
		geofenceResponse.setAddress(geofenceObject.get("Address")!=null ? geofenceObject.get("Address").toString() : "");
		geofenceResponse.setIsArrival(geofenceObject.get("IsArrival")!=null ? geofenceObject.get("IsArrival").toString() : "");
		geofenceResponse.setIsDepature(geofenceObject.get("IsDeparture")!=null ? geofenceObject.get("IsDeparture").toString() : "");

		if (responseobj.get("notificationDetails").toString()!= null
				&& !responseobj.get("notificationDetails").toString().equals("null")) {

			String daytime = responseobj.get("notificationDetails").toString();
			JSONObject daytimeObject = new JSONObject(daytime);
			NotificationDetails notificationDetails = new NotificationDetails();
			notificationDetails.setPushNotification(daytimeObject.get("Push notification").toString());
			notificationDetails.setSms(daytimeObject.get("SMS").toString());
			geofenceResponse.setNotificationDetails(notificationDetails);
		}
		geofenceList.add(geofenceResponse);
	}
}
