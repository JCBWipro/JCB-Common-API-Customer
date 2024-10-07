package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.dto.MachineLocation;
import com.wipro.jcb.livelink.app.machines.dto.MachineLocationDetail;
import com.wipro.jcb.livelink.app.machines.entity.*;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.*;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.reports.*;
import com.wipro.jcb.livelink.app.machines.service.response.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

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
	private MachineLocationHistoryDataRepo machineLocationHistoryRepo;

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

    @Override
    public Machine machineByVin(String vin) {
        return machineRepository.findByVin(vin);
    }

    @Override
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public List<String> getSuggetions(String word, String userName) {
        return List.of();
    }

    @Value("${custom.formatter.timezone}")
    String timezone;

    @Autowired
    MachineFeedParserDataRepo machineFeedParserDataRepo;
    @Autowired
    MachineFeedLocationRepo machineFeedLocationRepo;


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
				/*java.util.Date overDueDate = machine.getServiceDueDate();
				if (overDueDate != null) {
					overDueDate = new DateTime(Long.valueOf(overDueDate.getTime()), DateTimeZone.forID(timezone))
							.plusDays(1).toDate();
				}*/
                /*final String start = utilities.getStartDate(Integer.parseInt(loadServiceHistDays));
                final String end = utilities.getEndDate(1);*/
				/*final List<ServiceHistory> history = serviceHistoryRepository
						.findByVinAndServiceDateBetweenOrderByServiceDateDesc(machine.getVin(),
								utilities.getDate(start), utilities.getDate(end));*/
                final List<ServiceHistoryTimeline> historyline = new ArrayList<>();

                List<MachineServiceHistory> history = machineServiceHistoryRepo.getHistoryDetails(machine.getVin());

                ServiceSchedule serviceSchedule = serviceScheduleRepo.findById(vin);

                java.util.Date overDueDate = serviceSchedule != null ? serviceSchedule.getDueDate() : null;
                if (overDueDate != null) {
                    new DateTime(Long.valueOf(overDueDate.getTime()), DateTimeZone.forID(timezone))
                            .plusDays(1).toDate();
                }
                //final List<ServiceHistoryTimeline> historyline = new ArrayList<>();
                Date serviceDoneAt = null;
                for (final MachineServiceHistory tempObj : history) {
                    historyline.add(new ServiceHistoryTimeline(tempObj.getServiceDoneAt(), tempObj.getJobCardNumber(),
                            tempObj.getComments()));
                    if (history.get(0) != null && history.get(0).getServiceDoneAt() != null && !history.get(0).getServiceDoneAt().equals("NA"))
                        serviceDoneAt = history.get(0).getServiceDoneAt();
                }

                if (null != machineFeedParserData && null != machineFeedLocation
                        && null != machineFeedParserData.getStatusAsOnTime()) {
                    return new MachineServiceInfo(machineFeedParserData.getTotalMachineHours(), serviceSchedule != null && serviceSchedule.getOverDueHours() != null && !serviceSchedule.getOverDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getOverDueHours()) : null,
                            serviceSchedule != null && serviceSchedule.getOverDueDate() != null && !serviceSchedule.getOverDueDate().equals("NA") ? serviceSchedule.getOverDueDate() : null, serviceSchedule != null && serviceSchedule.getDueHours() != null && !serviceSchedule.getDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getDueHours()) : null, serviceSchedule != null && serviceSchedule.getDueDate() != null && !serviceSchedule.getDueDate().equals("NA") ? serviceSchedule.getDueDate() : null,
                            serviceSchedule != null && serviceSchedule.getCurrentCmh() != null && !serviceSchedule.getCurrentCmh().equals("NA") ? Double.parseDouble(serviceSchedule.getCurrentCmh()) : null, serviceDoneAt, machine.getThumbnail(),
                            machine.getVin(), machine.getModel(), machine.getPlatform(), machine.getTag(),
                            machine.getImage(), historyline);
                } else {
                    return new MachineServiceInfo(machine.getTotalMachineHours(), serviceSchedule != null && serviceSchedule.getOverDueHours() != null && !serviceSchedule.getOverDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getOverDueHours()) : null,
                            serviceSchedule != null && serviceSchedule.getOverDueDate() != null && !serviceSchedule.getOverDueDate().equals("NA") ? serviceSchedule.getOverDueDate() : null, serviceSchedule != null && serviceSchedule.getDueHours() != null && !serviceSchedule.getDueHours().equals("NA") ? Double.parseDouble(serviceSchedule.getDueHours()) : null, serviceSchedule != null && serviceSchedule.getDueDate() != null && !serviceSchedule.getDueDate().equals("NA") ? serviceSchedule.getDueDate() : null,
                            serviceSchedule != null && serviceSchedule.getCurrentCmh() != null && !serviceSchedule.getCurrentCmh().equals("NA") ? Double.parseDouble(serviceSchedule.getCurrentCmh()) : null, serviceDoneAt, machine.getThumbnail(),
                            machine.getVin(), machine.getModel(), machine.getPlatform(), machine.getTag(),
                            machine.getImage(), historyline);
                }

            } else {
                return new MachineServiceInfo();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("MachineServiceInfo : Internal Server Error");
            throw new ProcessCustomError("Failed to process MachineServiceInfo Request",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ServiceStatus getMachineServiceStatus(final Machine machine) throws ProcessCustomError {
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
    public ServiceStatus getMachineServiceHistoryStatus(Machine machine) throws ProcessCustomError {
        return null;
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
    }*/

 /*   @Override
    public UtilizationReport getMachineUtilization(String vin, Date startDate, Date endDate) {
        MachineUtilizationResponse utilization = new MachineUtilizationResponse();
        try {
            List<MachineUtilization> machineUtililizationData = machineUtilizationDataRepository
                    .getUtilizationDetails(vin, startDate, endDate);
            utilization.setMachineUtilization(machineUtililizationData);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception occured for Machine Utilization Report API :"+vin+"Exception -"+e.getMessage());
        }

        return utilization;
    }*/


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
            log.info("Delete notification details for the user " + userName);
            //notificationDetailsRepo.deleteById(id);
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
            log.info("Machine Detills for " + vin);
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
        try {
            final Machine machine = machineRepository.findByVin(vin);
            MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(vin);
            MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(vin);
            ServiceSchedule serviceSchedule = serviceScheduleRepo.findById(vin);
            if (machine != null) {
                java.util.Date overDueDate = serviceSchedule != null ? serviceSchedule.getOverDueDate() : null;
                if (overDueDate != null) {
                    overDueDate = new DateTime(Long.valueOf(overDueDate.getTime()), DateTimeZone.forID(timezone))
                            .plusDays(1).toDate();
                }

                final List<ServiceHistoryTimeline> historyline = new ArrayList<>();
                List<MachineServiceHistory> historylinenew = machineServiceHistoryRepo.getHistoryDetails(machine.getVin());

                for (final MachineServiceHistory tempObj : historylinenew) {
                    historyline.add(new ServiceHistoryTimeline(tempObj.getServiceDoneAt(), tempObj.getJobCardNumber(),
                            tempObj.getComments()));
                }
                assert serviceSchedule != null;
                if (null != machineFeedParserData && null != machineFeedLocation
                        && null != machineFeedParserData.getStatusAsOnTime()) {
                    return new MachineServiceInfoV2(machineFeedParserData.getTotalMachineHours(), (serviceSchedule.getOverDueHours() != null && !serviceSchedule.getOverDueHours().equalsIgnoreCase("NA")) ? Double.parseDouble(serviceSchedule.getOverDueHours()) : null,
                            overDueDate, (serviceSchedule.getDueHours() != null && !serviceSchedule.getDueHours().equalsIgnoreCase("NA")) ? Double.parseDouble(serviceSchedule.getDueHours()) : null, serviceSchedule.getDueDate(), !historylinenew.isEmpty() ? Double.parseDouble(historylinenew.get(0).getServiceDone()) : null,
                            !historylinenew.isEmpty() ? historylinenew.get(0).getServiceDoneAt() : null, machine.getThumbnail(), machine.getVin(), machine.getModel(), machine.getPlatform(),
                            machine.getTag(), machine.getImage(), historyline);
                } else {
                    return new MachineServiceInfoV2(serviceSchedule.getCurrentCmh() != null ? Double.parseDouble(serviceSchedule.getCurrentCmh()) : null, (serviceSchedule.getOverDueHours() != null && !serviceSchedule.getOverDueHours().equalsIgnoreCase("NA")) ? Double.parseDouble(serviceSchedule.getOverDueHours()) : null,
                            overDueDate, (serviceSchedule.getDueHours() != null && !serviceSchedule.getDueHours().equalsIgnoreCase("NA")) ? Double.parseDouble(serviceSchedule.getDueHours()) : null, serviceSchedule.getDueDate(), !historylinenew.isEmpty() ? Double.parseDouble(historylinenew.get(0).getServiceDone()) : null,
                            !historylinenew.isEmpty() ? historylinenew.get(0).getServiceDoneAt() : null, machine.getThumbnail(), machine.getVin(), machine.getModel(), machine.getPlatform(),
                            machine.getTag(), machine.getImage(), historyline);
                }

            } else {
                return new MachineServiceInfoV2();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.error("MachineServiceInfo : Internal Server Error : " + vin + "-" + ex.getMessage());
            throw new ProcessCustomError("Failed to process MachineServiceInfo Request",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
			log.info("Exception occured in machinelocation detail " + e.getMessage());
			log.info("Exception occured for Machine Location API -" + vin + "Exception -" + e.getMessage());
		}
		return machineDetails;
	}

}
