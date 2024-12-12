package com.wipro.jcb.livelink.app.dataprocess.dataparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.jcb.livelink.app.dataprocess.config.AppConfiguration;
import com.wipro.jcb.livelink.app.dataprocess.constants.Constant;
import com.wipro.jcb.livelink.app.dataprocess.constants.EventName;
import com.wipro.jcb.livelink.app.dataprocess.constants.EventType;
import com.wipro.jcb.livelink.app.dataprocess.constants.UserType;
import com.wipro.jcb.livelink.app.dataprocess.dataparserDAO.MachineDAO;
import com.wipro.jcb.livelink.app.dataprocess.dto.*;
import com.wipro.jcb.livelink.app.dataprocess.service.LivelinkUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 * */
@Slf4j
public class CombinedHistoryDataParser {
    @Autowired
    MachineDAO machineDao;
    @Autowired
    AppConfiguration config;
    @Value("${fcm.apiKey}")
    private String apiKey;
    @Value("${fcm.url}")
    private String fcmUrl;
    @Autowired
    LivelinkUserTokenService livelinkUserTokenService;

    public static void dataParsing(String msgReceived){
        //parsing logic to be implemented
        try {
            // 1. Split the message into individual packets
            String[] packets = msgReceived.split(Constant.MESSAGE_SEPARATOR);

            LinkedList<String> packetList = new LinkedList<>(Arrays.asList(packets));

            // 2. Process and update the packets
            CombinedHistoryDataParser parser = new CombinedHistoryDataParser();
            parser.processAndUpdatePacket(packetList);

        } catch (Exception e) {
            log.error("Error parsing message: {}", e.getMessage(), e);
        }

    }

    public void processAndUpdatePacket(LinkedList<String> packets) {
        List<MachineData> machineDataList = new ArrayList<>();
        List<MachineData> machineDataWithoutFuel = new ArrayList<>();
        final List<MachineData> machineDataWithoutLocation = new LinkedList<>();
        final List<MachineData> machineDataWithoutFuelLocation = new LinkedList<>();
        List<MachineEngineStatus> machineEngineStatusList = new ArrayList<>();
        List<MachineFuelConsumption> machineFuelConsumptionList = new ArrayList<>();
        final List<AlertData> alertDataList = List.of();
        final Set<String> vinList = Set.of();
        final Set<String> vinListForGeo = Set.of();

        for (String packet : packets) {
            try {
                String[] inputStringArray = packet.split("\\|inputPacketString:");
                if (inputStringArray.length < 2) {  // Check for valid split
                    log.warn("Invalid packet format: {}", packet);
                    continue; // Skiping to next packet
                }
                String inputString = inputStringArray[1];
                String firmware = inputString.substring(11, 19);
                JSONObject parser = config.getPacketStr();

                if (parser != null && parser.has(firmware) && !parser.isNull(firmware)) {
                    try {
                        JSONObject packetObj = parser.getJSONObject(firmware);
                        String msgId = inputString.substring(packetObj.getInt("msgidstart"), packetObj.getInt("msgidend"));
                        JSONObject parseMessageId = packetObj.getJSONObject("data");

                        if (parseMessageId.has(msgId) && !"100".equals(msgId)) {
                            JSONObject infoSeq = parseMessageId.getJSONObject(msgId);
                            String vin = inputString.substring(infoSeq.getInt("vinstart"), infoSeq.getInt("vinend"));
                            Timestamp statusAsOnTime = getStatusAsOnTime(inputString.substring(infoSeq.getInt("statusasonstart"),
                                    infoSeq.getInt("statusasonend")));
                            Double hmr = Double.parseDouble(inputString.substring(infoSeq.getInt("hmrstart"), infoSeq.getInt("hmrend")));
                            Double batteryVoltage = Double.parseDouble(inputString.substring(infoSeq.getInt("batteryvoltstart"),
                                    infoSeq.getInt("batteryvoltend")));

                            // Location Parsing (with improved error handling)
                            Double latitude = null;
                            Double longitude = null;
                            String location = inputString.substring(infoSeq.getInt("locationstart"), infoSeq.getInt("locationend"));
                            try {
                                String[] locationParts = location.split("N");
                                if(locationParts.length == 2){
                                    latitude = parseLocationPart(locationParts[0]);

                                    String[] lonParts = locationParts[1].split("E");

                                    if(lonParts.length > 0){
                                        longitude = parseLocationPart(lonParts[0]);
                                    }
                                }

                                if (latitude != null && longitude != null) {
                                    vinListForGeo.add(vin);
                                }
                            } catch (NumberFormatException ex) {
                                log.warn("Invalid location format for VIN {}: {}", vin, location, ex);
                            }


                            vinList.add(vin);


                            Double fuelLevelPerc = 0.0; // Default fuel level
                            int fuelStart = infoSeq.getInt("fuelpercstart");
                            int fuelEnd = infoSeq.getInt("fuelpercend");
                            if (fuelEnd > fuelStart) {
                                fuelLevelPerc = Double.parseDouble(inputString.substring(fuelStart, fuelEnd));
                                machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                            }

                            // Add to appropriate list based on available data
                            if (fuelLevelPerc > 0.0) {
                                if (latitude != null && longitude != null) {
                                    machineDataList.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude, batteryVoltage, fuelLevelPerc));
                                } else {
                                    machineDataWithoutLocation.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude, batteryVoltage, fuelLevelPerc));
                                }
                            } else {
                                if (latitude != null && longitude != null) {
                                    machineDataWithoutFuel.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude, batteryVoltage, fuelLevelPerc));
                                } else {
                                    machineDataWithoutFuelLocation.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude, batteryVoltage, fuelLevelPerc));
                                }
                            }

                            // update engine status for event type packets
                            if (config.getEventmsgid().contains(Integer.parseInt(msgId))) {
                                if ((infoSeq.getInt("alertidstart") - infoSeq.getInt("alertidend")) > 0) {
                                    final String alertId = inputString.substring(infoSeq.getInt("alertidstart"),
                                            infoSeq.getInt("alertidend"));
                                    final Boolean engineStatus = "1"
                                            .equals((inputString.subSequence(infoSeq.getInt("alertstatus"),
                                                    infoSeq.getInt("alertstatus") + 1).toString().trim()));
                                    if ("009".equals(alertId)) {
                                        machineEngineStatusList
                                                .add(new MachineEngineStatus(vin, statusAsOnTime, engineStatus));
                                    }
                                    alertDataList.add(new AlertData(vin, latitude, longitude, engineStatus, alertId,
                                            statusAsOnTime));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        log.error("JSON parsing error for packet: {} - Error: {}", packet, e.getMessage(), e);
                    }
                } else {
                    log.warn("Firmware version {} not found or invalid in packet: {}", firmware, packet);
                }
            } catch (Exception ex) {
                log.error("Error processing packet: {} - Error: {}", packet, ex.getMessage(), ex);
            }
        }

        try {
            machineDao.updateBatchMachineWithFuel(machineDataList);
            machineDao.updateBatchMachineWithoutFuel(machineDataWithoutFuel);
            machineDao.addBatchMachineWithEngineStatus(machineEngineStatusList);
            machineDao.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
            machineDao.updateBatchMachineWithoutFuelLocation(machineDataWithoutFuelLocation);
            machineDao.updateBatchMachineWithFuelWithoutLocation(machineDataWithoutLocation);
        } catch (final Exception ex) {
            log.error("Failed to execute batch update: {}", ex.getMessage(), ex);
        }
    }


    private Double parseLocationPart(String part) {
        try {
            double val = Double.parseDouble(part) / 100;
            double fraction = ((val - (int) val) / 60) * 100;
            DecimalFormat df = new DecimalFormat("0.00000");
            return Double.parseDouble(df.format((int) val + fraction));
        } catch (NumberFormatException e) {
            log.warn("Invalid location part format: {}.  Using 0.0 as default.", part);
            return 0.0; // Return a default value (0.0 in this case)
        }
    }

    private void generateTimeFenceAlert(final Set<String> timefenceVinList) {
        try {
            final Set<String> alertListForVin = new HashSet<>();
            final Set<String> registrationIds = new HashSet<>();
            if (!timefenceVinList.isEmpty()) {
                List<Map<String, Object>> existingAlertList = machineDao.findAlertByVin(timefenceVinList,
                        EventName.TIME_FENCE_ALERT.getName());
                if (existingAlertList.size() != timefenceVinList.size()) {
                    if (!existingAlertList.isEmpty()) {
                        for (Map<String, Object> row : existingAlertList) {
                            final String vin = String.valueOf(row.get(Constant.VIN));
                            timefenceVinList.remove(vin);
                        }
                    }
                    List<Map<String, Object>> machineInfo = machineDao.getMachineForTimefenceAlert(timefenceVinList);
                    if (!machineInfo.isEmpty()) {
                        for (Map<String, Object> row : machineInfo) {
                            final String userName = String.valueOf(row.get(Constant.USER_ID));
                            String vin = String.valueOf(row.get(Constant.VIN));
                            alertListForVin.add(vin);
                            addRegistrationIds(userName, registrationIds);
                        }
                    }
                    sendUserNotification(registrationIds, Constant.TIME_FENCE_ALERT_CASE);
                    if (!alertListForVin.isEmpty()) {
                        List<AlertData> alertData = loadAlertDataList(alertListForVin);
                        log.debug("alert Data size is and alert Data  items {} {}", alertData.size(), alertData);
                        machineDao.addTimefenceAlert(alertData);
                    }
                }
            }
        } catch (final Exception ex) {
            log.error("Failed to add generateTimeFenceAlert with message ", ex);
        }
    }

    private void generateGeoFenceAlert(final Set<String> geofenceVinList) {
        try {
            if (!geofenceVinList.isEmpty()) {
                final Set<String> registrationIds = new HashSet<>();
                final Set<String> alertListExists = new HashSet<>();
                List<Map<String, Object>> existingAlertList = machineDao.findAlertByVin(geofenceVinList,
                        EventName.GEO_FENCE_ALERT.getName());
                if (!existingAlertList.isEmpty()) {
                    for (Map<String, Object> row : existingAlertList) {
                        final String vin = String.valueOf(row.get(Constant.VIN));
                        alertListExists.add(vin);
                    }
                    final Set<String> addGFAlertforVin = new HashSet<>();
                    final Set<String> removeGFAlertforVin = new HashSet<>();
                    List<Map<String, Object>> machineInfo = machineDao.getMachineForGeofenceAlert(geofenceVinList);
                    if (null != machineInfo && !machineInfo.isEmpty()) {
                        for (Map<String, Object> row : machineInfo) {
                            final String userName = String.valueOf(row.get(Constant.USER_ID));
                            String vin = String.valueOf(row.get(Constant.VIN));
                            double radius = Double.parseDouble(row.get(Constant.RADIUS).toString());
                            double distance = Double.parseDouble(row.get(Constant.DISTANCE).toString());
                            if (distance > radius) {
                                if (alertListExists.add(vin)) {
                                    addGFAlertforVin.add(vin);
                                    addRegistrationIds(userName, registrationIds);
                                }
                            } else {
                                removeGFAlertforVin.add(vin);
                            }
                        }
                    }
                    sendUserNotification(registrationIds, Constant.GEO_FENCE_ALERT_CASE);
                    if (!addGFAlertforVin.isEmpty()) {
                        List<AlertData> alertData = loadAlertDataList(addGFAlertforVin);
                        log.debug("alert Data size is and alert Data  items {} {}", alertData.size(), alertData);
                        machineDao.addGeofenceAlert(alertData);
                    }
                    if (!removeGFAlertforVin.isEmpty()) {
                        log.debug("removeGFAlertForVin  Data :{}", removeGFAlertforVin.size());
                        int recordCount = machineDao.removeAlertForGeofence(removeGFAlertforVin);
                        log.warn("No of alerts are removed of Geofence event type is : {}", recordCount);
                    }
                }
            }
        } catch (final Exception ex) {
            log.error("Failed to add generateGeoFenceAlert with message ", ex);
        }
    }

    public List<AlertData> loadAlertDataList(Set<String> alertListForVin) {
        List<AlertData> alertData = new LinkedList<>();
        for (String vin : alertListForVin) {
            String uuid = getUniqueID();
            while (machineDao.findByAlertById(uuid) != 0) {
                uuid = getUniqueID();
            }
            alertData.add(new AlertData(vin, uuid));
        }
        return alertData;
    }

    public String getUniqueID() {
        final UUID idOne = UUID.randomUUID();
        return idOne.toString();
    }

    public void sendUserNotification(final Set<String> registrationIds, String eventName) {
        try {
            if (!registrationIds.isEmpty()) {
                buildNotificationOnEventType(eventName, registrationIds);
            }
            log.info("sendUserNotification : end ");
        } catch (Exception ex) {
            log.error("sendUserNotification:Error occurred while sending notification for  eventName  {}", eventName,
                    ex);
        }
    }

    private void buildNotificationOnEventType(final String eventName, final Set<String> registrationIds) {
        switch (eventName) {
            case Constant.TIME_FENCE_ALERT_CASE:
                sendNotification(getMsgForTimefenceAlert(), registrationIds);
                break;
            case Constant.GEO_FENCE_ALERT_CASE:
                sendNotification(getMsgForGeofenceAlert(), registrationIds);
                break;
            default:
                log.warn("No eventType is matching for alert ");
                break;
        }
    }

    public void sendNotification(PushNotification pushNotification, final Set<String> registrationIds) {
        try {
            if (null != pushNotification) {
                pushNotification.setRegistration_ids(registrationIds);
                final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add("Authorization", "key =" + apiKey);
                headers.add("Content-Type", "application/json");
                final RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                final HttpEntity<PushNotification> request = new HttpEntity<>(pushNotification, headers);
                final ResponseEntity<String> out = restTemplate.exchange(fcmUrl, HttpMethod.POST, request,
                        String.class);
                if (out.getStatusCode() == HttpStatus.OK) {
                    log.info("PUSH NOTIFICATION SEND");
                } else {
                    log.error("PUSH NOTIFICATION FAILED ");
                }
                log.debug("registrationIds : {}", registrationIds);
            }
        } catch (Exception ex) {
            log.error("Exception while processing Notification {}", pushNotification, registrationIds, ex);
            throw ex;
        }

    }

    public AlertNotification getMsgForTimefenceAlert() {
        final AlertNotification message = new AlertNotification();
        message.setContent_available(false);
        message.setNotification(new AlertNotificationNotification("Machine is used outside Operating Hours",
                "New " + EventType.UTILIZATION.getName() + " Alert", "default"));
        message.setData(new AlertNotificationData("Machine is used outside Operating Hours",
                EventType.UTILIZATION.getName() + " Alert", EventType.UTILIZATION.getName(),
                "New " + "Utilization" + " Alert"));
        message.setRegistration_ids(new HashSet<String>());
        return message;
    }

    public AlertNotification getMsgForGeofenceAlert() {
        final AlertNotification message = new AlertNotification();
        message.setContent_available(false);
        message.setNotification(new AlertNotificationNotification("Machine is deviated from specified range",
                "New " + EventType.LANDMARK.getName() + " Alert", "default"));
        message.setData(new AlertNotificationData("Machine is deviated from specified range",
                EventType.LANDMARK.getName() + " Alert", EventType.LANDMARK.getName(),
                "New " + EventType.LANDMARK.getName() + " Alert"));
        message.setRegistration_ids(new HashSet<String>());
        return message;
    }

    private <V> void addRegistrationIds(final String userName, final Set<String> registrationIds) {
        try {
            LinkedHashMap<?, ?> map = livelinkUserTokenService.getUserTokenByUsername(userName);
            if (isCustomer(map)) {
                @SuppressWarnings("unchecked")
                HashMap<String, V> tokenKey = new ObjectMapper().convertValue(map.get("accessToken"), HashMap.class);
                for (Map.Entry<String, V> entry : tokenKey.entrySet()) {
                    String key = entry.getKey();
                    if (!key.equals("@class")) {
                        @SuppressWarnings("unchecked")
                        HashMap<String, V> appToken = new ObjectMapper().convertValue(tokenKey.get(key), HashMap.class);
                        if (appToken.get("enable").toString().equalsIgnoreCase("true")) {
                            registrationIds.add(appToken.get("appFCMKey").toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Failed to add RegistrationIds");
        }
    }

    private Boolean isCustomer(LinkedHashMap<?, ?> map) {
        boolean isValid = false;
        if (map != null && null != map.get("accessToken") && null != map.get("userType")) {
            isValid = map.get("userType").toString().equalsIgnoreCase(UserType.CUSTOMER.getName());
        }
        return isValid;
    }

    private Timestamp getStatusAsOnTime(final String statusAsOn) {
        DateFormat parseFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp statusAsOnTime = null;
        try {
            Date parseDate = parseFormat.parse(statusAsOn);
            statusAsOnTime = new Timestamp(parseDate.getTime() + 19800000L);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return statusAsOnTime;
    }

}