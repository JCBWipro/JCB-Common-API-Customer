package com.wipro.jcb.livelink.app.machines.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.wipro.jcb.livelink.app.machines.dto.MachineLocation;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineDownQuestion;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.request.GeofenceRequest;
import com.wipro.jcb.livelink.app.machines.request.TimefenceRequest;
import com.wipro.jcb.livelink.app.machines.service.reports.NotificationRemovedResponse;
import com.wipro.jcb.livelink.app.machines.service.reports.StandardMachineBaseResponse;
import com.wipro.jcb.livelink.app.machines.service.reports.UtilizationReport;
import com.wipro.jcb.livelink.app.machines.service.response.Filter;
import com.wipro.jcb.livelink.app.machines.service.response.GeofenceLandmarkResponse;
import com.wipro.jcb.livelink.app.machines.service.response.LocationHistory;
import com.wipro.jcb.livelink.app.machines.service.response.MachineServiceInfo;
import com.wipro.jcb.livelink.app.machines.service.response.MachineServiceInfoV2;
import com.wipro.jcb.livelink.app.machines.service.response.RdMachineDetailsResponse;
import com.wipro.jcb.livelink.app.machines.service.response.RdMachineResponse;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface MachineService {
    /**
     * Find Machine by vin
     *
     * @param vin
     *            is unique identity of machine for which data to be retrieved
     * @return Machine is instance of Machine
     */
    Machine machineByVin(String vin);

    /**
     * Save Machine
     *
     * @param machine
     *            is the instance of machine
     * @return Machine is the instance of machine
     */
    Machine save(Machine machine);

    /**
     * list of words as suggestion based on input word for user identified by
     * accessToken
     *
     * @param word
     *            is used to find like words from data base for search criteria
     * @param userName
     *            identifier for user
     * @return List<String> is the list of sentences
     */
    List<String> getSuggestions(String word, String userName);

    /**
     * List of filters for machine
     *
     * @param userName
     *            identifier for user
     * @return List<Filters> is the list of filter
     */
    List<Filter> getFilters(String userName);

    MachineServiceInfo getMachineServiceInfo(String vin) throws ProcessCustomError;

    ServiceStatus getMachineServiceStatus(final Machine machine) throws ProcessCustomError;

    ServiceStatus getMachineServiceHistoryStatus(final Machine machine) throws ProcessCustomError;

    public List<MachineDownQuestion> getAllMachineDownQuestionList();
    public void setMachineTimeFence(String vin, String startTime, String endTime) throws ProcessCustomError;
    public void setMachineGeoFenceParam(String vin, Double centerLatitude, Double centerLongitude, Long radius) throws ProcessCustomError;
    public boolean doSupportFeatures(final String vin);

    List<Filter> getUserPlatform(String userName);

    String deleteTimefenceDetails(String vin, String userName,String tokenId);

    String storeServiceCallRequestFileUpload(String vin, String customerName, String customerPhone, String contactName, String customerAlternativePhone, String machineHmr, String serviceDealerName, String model, String machineLocation, String warrantyStatus, String contractStatus, String machineStatus, String remarks, List<MultipartFile> image, String userName);

    Double getFuelConsumptionData(String vin);

    RdMachineResponse fetchNewMachines(Date startdate, Date enddate) throws ProcessCustomError;

    StandardMachineBaseResponse getStandardMachineImages();

    NotificationRemovedResponse readAllNotification(String userName);

    NotificationRemovedResponse readNotification(int id , String userName);

    NotificationRemovedResponse deleteAllAlertNotification(String userName);

    NotificationRemovedResponse deleteNotification(Integer id, String userName);

    RdMachineDetailsResponse fetchMachinesDetails(String vin) throws ProcessCustomError;

    MachineServiceInfoV2 getMachineServiceInfoV2(String vin) throws ProcessCustomError;

    void updatePremiumFlag(String userName, String vin, String premiumFlag) throws ProcessCustomError;

    List<String> getIntelliAutoSuggetions(String word);

    String getHigherData(String range, String date);

    String generatePremiumRequestReport() throws ProcessCustomError;
    
    MachineLocation getMachineLocationDetail(String vin, String pageNumber, String pageSize);
    
    List<LocationHistory> getMachineLocationHistory(String vin, String date);
    
    UtilizationReport getMachineUtilization(String vin, Date startDate, Date endDate);
    
    UtilizationReport getFuelUtilization(String vin, Date startDate, Date endDate);
    
    String setGeoFenceParam(GeofenceRequest gfSetRequest,String userName,String machineType,String tokenId) throws Exception;
    
    GeofenceRequest getGeofenceDetails(String vin,String userName,String token);
    
    String deleteGeofenceDetails(GeofenceRequest geofenceParam, String userName, String tokenId);
    
    GeofenceLandmarkResponse getLandmarkDetails(String userName, String landmarkName,String livelinkTokenId,String vin);
    
    String setTimeFenceParam(TimefenceRequest timefenceParam, String userName,String machineType,String tokenId);

}
