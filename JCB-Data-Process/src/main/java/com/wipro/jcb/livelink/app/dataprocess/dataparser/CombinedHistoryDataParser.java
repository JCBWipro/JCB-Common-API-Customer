package com.wipro.jcb.livelink.app.dataprocess.dataparser;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.dataprocess.commonUtils.DataParserUtilities;
import com.wipro.jcb.livelink.app.dataprocess.config.AppConfiguration;
import com.wipro.jcb.livelink.app.dataprocess.constants.Constant;
import com.wipro.jcb.livelink.app.dataprocess.dataparserDAO.CombinedHistoryDAO;
import com.wipro.jcb.livelink.app.dataprocess.dataparserDAO.MachineDAO;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineData;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineEngineStatus;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineFuelConsumption;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineLocation;
import com.wipro.jcb.livelink.app.dataprocess.entity.FeedParserStatistics;
import com.wipro.jcb.livelink.app.dataprocess.entity.FirmwareData;
import com.wipro.jcb.livelink.app.dataprocess.repo.FeedParserStatisticsRepository;
import com.wipro.jcb.livelink.app.dataprocess.repo.FirmwarePacketRepo;
import com.wipro.jcb.livelink.app.dataprocess.repo.MachineFuelHistoryDataRepo;

import lombok.extern.slf4j.Slf4j;

/*
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 * */
@Slf4j
@Component
public class CombinedHistoryDataParser {
	
    @Autowired
    MachineDAO machineDao;
    
    @Autowired
    AppConfiguration config;
    
    @Value("${fcm.apiKey}")
    String apiKey;
    
    @Value("${fcm.url}")
    String fcmUrl;
    
    @Autowired
    CombinedHistoryDAO combinedHistoryDAO;

    @Autowired
    FeedParserStatisticsRepository feedRepo;

//    @Autowired
//    EmailService emailService;

    @Autowired
    FirmwarePacketRepo firmwarePacketRepo;

    @Autowired
    DataParserUtilities dataParserUtilities;

    @Autowired
    MachineFuelHistoryDataRepo machineFuelHistoryDataRepo;

    // Declare variable to count the number of parameters
    private static final AtomicLong packetCount = new AtomicLong(0);

    private final List<MachineData> machineDataList = new LinkedList<>();
    private final List<MachineData> machineDataWithoutFuel = new LinkedList<>();
    private final List<MachineLocation> machinelocationList = new LinkedList<>();
    private final List<MachineEngineStatus> machineEngineStatusList = new LinkedList<>();
    private final List<MachineFuelConsumption> machineFuelConsumptionList = new LinkedList<>();
    private final List<MachineData> machineStatusList = new LinkedList<>();

    public void dataParsing(String msgReceived) {
        try {
        	List<String> messages = new LinkedList<String>();
    		if (msgReceived.contains(Constant.RESPONSE_SUCCESS)) {
    			messages.add(msgReceived);
    		}
    		processAndUpdatePacket(messages);
        } catch (Exception e) {
            log.error("Error parsing message: {}", e.getMessage(), e);
        }
    }

    public void processAndUpdatePacket(List<String> packets) {
        log.info("packets size from queue: {}", packets.size());
        final Set<String> vinList = new LinkedHashSet<>();
        final Set<String> vinListforGeo = new LinkedHashSet<>();
        Date lastDate = dataParserUtilities.getDate(dataParserUtilities.getStartDate(14));
        packetCount.addAndGet(packets.size());
        long start = System.currentTimeMillis();
        for (final String packet : packets) {
            try {
                final String[] inputStringArray = packet.split(Constant.MESSAGE_SEPARATOR);
                final String inputString = inputStringArray[1];
                final String firmware = inputString.substring(11, 19);
                final JSONObject parser = config.getPacketStr();
                if (parser.has(firmware) && (!parser.isNull(firmware))) {
                    try {
                        final JSONObject packetObj = parser.getJSONObject(inputString.substring(11, 19));
                        final String msgId = inputString.substring(packetObj.getInt("msgidstart"),
                                packetObj.getInt("msgidend"));
                        final JSONObject parseMessageId = packetObj.getJSONObject("data");
                        if (parseMessageId.has(msgId) && !"100".equals(msgId)) {
                            final JSONObject infoSeq = parseMessageId.getJSONObject(msgId);
                            final String vin = inputString.substring(infoSeq.getInt("vinstart"),
                                    infoSeq.getInt("vinend"));
                            final String statusAsOn = inputString.substring(infoSeq.getInt("statusasonstart"),
                                    infoSeq.getInt("statusasonend"));
                            String hrmValues = inputString.substring(infoSeq.getInt("hmrstart"), infoSeq.getInt("hmrend"));
                            Double hmr = null;
                            if (!hrmValues.isEmpty()) {
                                hmr = Double.parseDouble(hrmValues);
                            } else {
                                log.info("Empty HMR {}", vin);
                            }
                            final String location = inputString.substring(infoSeq.getInt("locationstart"),
                                    infoSeq.getInt("locationend"));
                            String batterValues = inputString.substring(infoSeq.getInt("batteryvoltstart"), infoSeq.getInt("batteryvoltend"));
                            Double batteryVoltage = null;
                            if (!batterValues.isEmpty()) {
                                batteryVoltage = Double.parseDouble(batterValues);
                            } else {
                                log.info("Empty Battery {}", vin);
                            }
                            Timestamp statusAsOnTime = getStatusAsOnTime(statusAsOn);

                            Double fuelLevelPerc = 0.0;
                            Double latitude = null;
                            Double longitude = null;
                            try {
                                // below change is to parse location whenever N and E exist in the location
                                if (location.contains("N") && location.contains("E")) {
                                    final double lat = Double.parseDouble(location.split("N")[0]) / 100;
                                    final double fractionLat = ((lat - (int) lat) / 60) * 100;
                                    latitude = (int) lat + fractionLat;
                                    final double lon = Double.parseDouble(location.split("N")[1].split("E")[0]) / 100;
                                    final double fractionLon = ((lon - (int) lon) / 60) * 100;
                                    longitude = (int) lon + fractionLon;
                                } else {
                                    MachineLocation machineLocation = combinedHistoryDAO.getExistingMachineLocation(vin);
                                    latitude = machineLocation.getLatitude();
                                    longitude = machineLocation.getLongitude();
                                }
                                vinListforGeo.add(vin);
                                machinelocationList.add(new MachineLocation(vin, statusAsOnTime, latitude, longitude));
                            } catch (Exception ex) {
                                log.error("getting exception while parsing loaction {} ", ex.getMessage());
                            }

                            vinList.add(vin);
                            // Update machine usage data in machine table
                            if ((infoSeq.getInt("fuelpercend") - infoSeq.getInt("fuelpercstart")) > 0
                                    && Double.parseDouble(inputString.substring(infoSeq.getInt("fuelpercstart"),
                                    infoSeq.getInt("fuelpercend"))) != 110) {
                                fuelLevelPerc = Double.parseDouble(inputString
                                        .substring(infoSeq.getInt("fuelpercstart"), infoSeq.getInt("fuelpercend")));
                                machineDataList.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude,
                                        batteryVoltage, fuelLevelPerc));

                                if (statusAsOnTime.after(lastDate)) {

                                    if (config.getIgnitionmsgid().contains(Integer.parseInt(msgId))) {
                                        if (infoSeq.has("ignitionstatus")) {
                                            final Boolean ignitionStatus = "1"
                                                    .equals((inputString.subSequence(infoSeq.getInt("ignitionstatus"),
                                                            infoSeq.getInt("ignitionstatus") + 1).toString().trim()));
                                            if (ignitionStatus.equals(true)) {
                                                machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                                                combinedHistoryDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                            } else {
                                                try {
                                                    log.info("Ignition Status is False : " + vin + "-" + statusAsOnTime + "-" + fuelLevelPerc);

                                                    Double fuelLevel = machineFuelHistoryDataRepo.getFuelHistoryRecord(vin);
                                                    if (fuelLevel != null) {
                                                        machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevel));
                                                        combinedHistoryDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                                    } else {
                                                        log.error("No Existing Records {}", vin);
                                                    }

                                                } catch (Exception e) {
                                                    log.info("Exception in get fuel data {}-{}", e.getMessage(), vin);
                                                    e.printStackTrace();
                                                }

                                            }
                                        } else {
                                            log.info("Ignition Not Integrated: {}", vin);
                                            machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                                            combinedHistoryDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                        }
                                    } else {
                                        machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                                        combinedHistoryDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                        log.info("Ignition Msg Id Not Configured :" + msgId + "-" + vin);
                                    }
                                }

                            } else {

                                if (hmr != null && batteryVoltage != null) {
                                    machineDataWithoutFuel.add(new MachineData(vin, hmr, statusAsOnTime, latitude,
                                            longitude, batteryVoltage, fuelLevelPerc));
                                } else {
                                    machineStatusList.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude,
                                            batteryVoltage, fuelLevelPerc));
                                }

                            }
                            try {
                                if ((infoSeq.getInt("alertidend") - infoSeq.getInt("alertidstart")) > 0) {
                                    final String alertId = inputString.substring(infoSeq.getInt("alertidstart"),
                                            infoSeq.getInt("alertidend"));
                                    final Boolean enginestatus = "1"
                                            .equals((inputString.subSequence(infoSeq.getInt("alertstatus"),
                                                    infoSeq.getInt("alertstatus") + 1).toString().trim()));
                                    if ("009".equals(alertId)) {


                                        if (statusAsOnTime.after(lastDate)) {
                                            machineEngineStatusList
                                                    .add(new MachineEngineStatus(vin, statusAsOnTime, enginestatus));
                                        } else {
                                            log.info("machineEngineStatusList : statusAsOnTime is greaterthan 15 Days {}-{}-{}", vin, statusAsOnTime, lastDate);
                                        }

                                    }

                                }
                            } catch (Exception e) {
                                log.error("Exception in engine status block -{}-{}", packet, e.getMessage());
                            }
                        } else {
                            log.info("Msg id - {} not integrated for the firmware - {}", msgId, firmware);
                        }
                    } catch (final JSONException e) {
                        log.error("Failed to extract data from  input string {}", packet);
                        e.printStackTrace();
                    }
                } else {
                    log.info("Missing Firmware Packet : {}", packet);
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat updatedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = dataParserUtilities.getDate(df.format(date));
                    FirmwareData data = new FirmwareData();
                    data.setFirmware(firmware);
                    Long count = firmwarePacketRepo.countByFirmwareAndDay(firmware, date);
                    if (count != 0) {
                        data = firmwarePacketRepo.findByFirmware(firmware, date);
                        data.setCount(data.getCount() + 1);
                        data.setLast_updated_at(dataParserUtilities.getDateTime(updatedf.format(new Date())));

                    } else {
                        data.setCount(1);
                        data.setLast_updated_at(dataParserUtilities.getDateTime(updatedf.format(new Date())));
                        data.setDay(date);

                    }
                    firmwarePacketRepo.save(data);
                }
            } catch (final Exception ex) {
                log.error("Failed to parse packet {}", packet);
                ex.printStackTrace();
            }
        }
        try {
            log.info("Executing batches...");

            log.info("FeedProcessor: adding machine feed data");
            combinedHistoryDAO.updateBatchMachineFeedData(machineDataWithoutFuel);
            log.info("FeedProcessor: finished adding machine feed data adding machine feed data with fuel");
            combinedHistoryDAO.updateBatchMachineFeedDataHavingFuel(machineDataList);
            log.info("FeedProcessor: finished adding machine feed data with fuel adding machine location data");
            combinedHistoryDAO.updateStatusOnTime(machineStatusList);
            log.info("FeedProcessor: finished adding machine feed data with hmr and battery ");
            combinedHistoryDAO.updateBatchMachineLocation(machinelocationList);
            log.info("FeedProcessor: finished adding machine location data adding machine engine status data");
            combinedHistoryDAO.addBatchMachineWithEngineStatus(machineEngineStatusList);
            log.info("FeedProcessor: finished adding machine engine status data adding machine fuel consumption data");
            combinedHistoryDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
            log.info("FeedProcessor: finished adding machine fuel consumption data");

            machineDataList.clear();
            machineDataWithoutFuel.clear();
            machinelocationList.clear();
            machineEngineStatusList.clear();
            machineFuelConsumptionList.clear();
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("Time for 1000 packets  {}", elapsedTime);
        } catch (final Exception ex) {
            log.error("Failed to execute batch");
            ex.printStackTrace();
        }
    }

    private Timestamp getStatusAsOnTime(final String statusason) {
        DateFormat parseFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp statusAsOnTime = null;
        try {
            Date parseDate = parseFormat.parse(statusason);
            statusAsOnTime = new java.sql.Timestamp(parseDate.getTime() + 19800000L);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return statusAsOnTime;
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void publish() throws ParseException {

        // Reset to 0
        long hourlyCount = packetCount.getAndSet(0);

        // Create date details
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        // Use Madrid's time zone to format the date in
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        dateformat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        date = df.parse(df.format(date));

        // Set created date and count
        FeedParserStatistics stats = new FeedParserStatistics();
        stats.setCreatedAt(date);
        stats.setData(hourlyCount);

        // Save the value in database
        log.info("Hourly count value is {}", hourlyCount);
        feedRepo.save(stats);
        if (hourlyCount == 0) {
            log.info("Start Feedparser Mail");
            //emailService.sendFeedParserStatusMail("Feedparser", dateformat.format(date), 0);
        }
    }
}