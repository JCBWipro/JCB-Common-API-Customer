/*
package com.wipro.jcb.livelink.app.dataprocess.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

*/
/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 *//*

public class CombinedHistoryDataParser {

    public static void dataParsing(String msgReceived){
        //parsing logic to be implemented
    }

    {
        private final Logger logger = LoggerFactory.getLogger(CombinedHistoryDataParser.class);
        @Autowired
        MachineDAO machineDao;
        @Autowired
        AppConfiguration config;
        @Autowired
        AppServerTokenService appServerTokenService;
        @Value("${fcm.apiKey}")
        private String apiKey;
        @Value("${fcm.url}")
        private String fcmUrl;
        @Autowired
        TestMachineDAO testMachineDAO;
        @Autowired
        LivelinkUserTokenService livelinkUserTokenService;

        @Autowired
        private FeedParserStatisticsRepository feedRepo;

        @Autowired
        private EmailService emailService;

        @Autowired
        private FirmwarePacketRepo firmwarePacketRepo;



        @Autowired
        Utilities utilities;

        @Autowired
        MachineLocationHistoryRepo machineLocationHistoryRepo;

        @Autowired
        MachineFeedParserDataRepo machineFeedParserDataRepo;

        @Autowired
        MachineFuelHistoryDataRepo machineFuelHistoryDataRepo;

        SimpleDateFormat newdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Declare variable to count the number of parameters
        private static volatile long packetCount = 0;

        private final List<MachineData> machineDataList = new LinkedList<>();
        private final List<MachineData> machineDataWithoutFuel = new LinkedList<>();
        private final List<MachineLocation> machinelocationList = new LinkedList<>();
        private final List<MachineEngineStatus> machineEngineStatusList = new LinkedList<>();
        private final List<MachineFuelConsumption> machineFuelConsumptionList = new LinkedList<>();
        private final List<MachineData> machineStatusList = new LinkedList<>();

        public void processAndUpdatePacket(List<String> packets) {
        logger.info("packets size from queue: {}", packets.size());
        final Set<String> vinList = new LinkedHashSet<>();
        final Set<String> vinListforGeo = new LinkedHashSet<>();
        Date lastDate =	utilities.getDate(utilities.getStartDate(14));
        packetCount = packetCount + packets.size();
        long start = System.currentTimeMillis();
        for (final String packet : packets) {
            //logger.info("Packets "+packet);
            try {
                final String[] inputStringArray = packet.split("\\|inputPacketString:");
                final String inputString = inputStringArray[1];
                final String firware = inputString.substring(11, 19);
                final JSONObject parser = config.getPacketStr();
                if (parser.has(firware) && (!parser.isNull(firware))) {
                    try {
                        final JSONObject packetObj = parser.getJSONObject(inputString.substring(11, 19));
                        final String msgId = inputString.substring(packetObj.getInt("msgidstart"),
                                packetObj.getInt("msgidend"));
                        final JSONObject parseMessageId = packetObj.getJSONObject("data");
                        if (parseMessageId.has(msgId) && !"100".equals(msgId)) {
                            final JSONObject infoSeq = parseMessageId.getJSONObject(msgId);
                            final String vin = inputString.substring(infoSeq.getInt("vinstart"),
                                    infoSeq.getInt("vinend"));
                            final String statusason = inputString.substring(infoSeq.getInt("statusasonstart"),
                                    infoSeq.getInt("statusasonend"));
                            String hrmValues = inputString.substring(infoSeq.getInt("hmrstart"), infoSeq.getInt("hmrend"));
                            Double  hmr=null;
                            if(!hrmValues.isEmpty())
                            {
                                hmr = Double.parseDouble(hrmValues);
                            }else {
                                logger.info("Empty HMR "+vin);
                            }
                            final String location = inputString.substring(infoSeq.getInt("locationstart"),
                                    infoSeq.getInt("locationend"));
                            String batterValues = inputString.substring(infoSeq.getInt("batteryvoltstart"), infoSeq.getInt("batteryvoltend"));
                            Double batteryVoltage =null;
                            if(!batterValues.isEmpty())
                            {
                                batteryVoltage = Double.parseDouble(batterValues);
                            }else {
                                logger.info("Empty Battery "+vin);
                            }
                            Timestamp statusAsOnTime = getStatusAsOnTime(statusason);

                            Double fuelLevelPerc = 0.0;
                            Double latitude = null;
                            Double longitude = null;
                            try {
                                // below change is to parse location whenever N and E exist in the location
                                // string

                                if (location.contains("N") && location.contains("E")) {
                                    final Double lat = Double.parseDouble(location.split("N")[0]) / 100;
                                    final Double fractionLat = ((lat - lat.intValue()) / 60) * 100;
                                    latitude = lat.intValue() + fractionLat;
                                    final Double lon = Double.parseDouble(location.split("N")[1].split("E")[0]) / 100;
                                    final Double fractionLon = ((lon - lon.intValue()) / 60) * 100;
                                    longitude = lon.intValue() + fractionLon;


                                } else {
                                    //logger.info("Invalid location string: {}", packet);
                                    MachineLocation machineLocation = testMachineDAO.getExistingMachineLocation(vin);
                                    latitude = machineLocation.getLatitude();
                                    longitude = machineLocation.getLongitude();
                                }
                                vinListforGeo.add(vin);
                                machinelocationList.add(new MachineLocation(vin, statusAsOnTime, latitude, longitude));
                            } catch (Exception ex) {
                                logger.error("getting exception while parsing loaction {} ", ex.getMessage());
                            }

                            vinList.add(vin);
                            // Update machine usage data in machine table
                            if ((infoSeq.getInt("fuelpercend") - infoSeq.getInt("fuelpercstart")) > 0
                                    && Double.parseDouble(inputString.substring(infoSeq.getInt("fuelpercstart"),
                                    infoSeq.getInt("fuelpercend"))) != 110) {
                                fuelLevelPerc = Double.parseDouble(inputString
                                        .substring(infoSeq.getInt("fuelpercstart"), infoSeq.getInt("fuelpercend")));
//								logger.info("Fuel Level "+vin+"-"+statusAsOnTime+"-"+(infoSeq.getInt("fuelpercend") - infoSeq.getInt("fuelpercstart"))+"-"+fuelLevelPerc);
                                machineDataList.add(new MachineData(vin, hmr, statusAsOnTime, latitude, longitude,
                                        batteryVoltage, fuelLevelPerc));

                                if(statusAsOnTime.after(lastDate))
                                {

                                    if (config.getIgnitionmsgid().contains(Integer.parseInt(msgId))) {
                                        if(infoSeq.has("ignitionstatus"))
                                        {
                                            final Boolean ignitionStatus = "1"
                                                    .equals((inputString.subSequence(infoSeq.getInt("ignitionstatus"),
                                                            infoSeq.getInt("ignitionstatus") + 1).toString().trim())) ? true
                                                    : false;
//											logger.info("Ignition Status: "+vin+"-"+statusAsOnTime+"-"+ignitionStatus+"-"+fuelLevelPerc);

                                            if(ignitionStatus.equals(true))
                                            {
                                                machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                                                testMachineDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                            }else {
                                                try
                                                {
                                                    logger.info("Ignition Status is False : "+vin+"-"+statusAsOnTime+"-"+fuelLevelPerc);

                                                    Double fuelLevel = machineFuelHistoryDataRepo.getFuelHistoryRecord(vin);
                                                    if(fuelLevel!=null) {
                                                        machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevel));
                                                        testMachineDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                                    }else {
                                                        logger.error("No Existing Records "+vin);
                                                    }

                                                }catch(Exception e)
                                                {
                                                    logger.info("Exception in get fuel data "+e.getMessage()+"-"+vin);
                                                    e.printStackTrace();
                                                }

                                            }
                                        }else {
                                            logger.info("Ignition Not Integrated: "+vin);
                                            machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                                            testMachineDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                        }
                                    }else {
                                        machineFuelConsumptionList.add(new MachineFuelConsumption(vin, statusAsOnTime, fuelLevelPerc));
                                        testMachineDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
                                        logger.info("Ignition Msg Id Not Configured :"+msgId+"-"+vin);
                                    }
                                }

                            } else {

                                if(hmr!=null && batteryVoltage!=null) {
                                    machineDataWithoutFuel.add(new MachineData(vin, hmr, statusAsOnTime, latitude,
                                            longitude, batteryVoltage, fuelLevelPerc));
                                }else {
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
                                                    infoSeq.getInt("alertstatus") + 1).toString().trim())) ? true
                                            : false;
                                    if ("009".equals(alertId)) {


                                        if(statusAsOnTime.after(lastDate))
                                        {
                                            machineEngineStatusList
                                                    .add(new MachineEngineStatus(vin, statusAsOnTime, enginestatus));
                                        }else {
                                            logger.info("machineEngineStatusList : statusAsOnTime is greaterthan 15 Days "+vin+"-"+statusAsOnTime+"-"+lastDate);
                                        }

                                    }

                                }
                            }
                            catch(Exception e)
                            {
                                logger.error("Exeception in engine status block -"+packet+"-"+e.getMessage());
                            }
							*/
/*}else {
								logger.info("Msg Event Id Not Configured :"+msgId+"-"+vin);
							}*//*

                        }else {
                            logger.info("Msg id - "+msgId+" not integrated for the firmware - "+firware);
                        }
                    } catch (final JSONException e) {
                        logger.error("Failed to extract data from  input string " + packet);
                        e.printStackTrace();
                    }
                } else {
                    logger.info("Missing Firmware Packet : "+packet);
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat updatedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = utilities.getDate(df.format(date));
                    FirmwareData data = new FirmwareData();
                    data.setFirmware(firware);
                    Long count = firmwarePacketRepo.countByFirmwareAndDay(firware, date);
                    if (count != 0) {
                        data = firmwarePacketRepo.findByFirmware(firware, date);
                        data.setCount(data.getCount() + 1);
                        data.setLast_updated_at(utilities.getDateTime(updatedf.format(new Date())));

                    } else {
                        data.setCount(1);
                        data.setLast_updated_at(utilities.getDateTime(updatedf.format(new Date())));
                        data.setDay(date);

                    }
                    firmwarePacketRepo.save(data);
                }
            } catch (final Exception ex) {
                logger.error("Failed to parse packet " + packet);
                ex.printStackTrace();
            }
        }
        try {
            logger.info("Executing batches...");

            logger.info("FeedProcessor: adding machine feed data");
            testMachineDAO.updateBatchMachineFeedData(machineDataWithoutFuel);
            logger.info("FeedProcessor: fininshed adding machine feed data adding machine feed data with fuel");
            testMachineDAO.updateBatchMachineFeedDataHavingFuel(machineDataList);
            logger.info("FeedProcessor: fininshed adding machine feed data with fuel adding machine location data");
            testMachineDAO.updateStatusOnTime(machineStatusList);
            logger.info("FeedProcessor: fininshed adding machine feed data with hmr and battery ");
            testMachineDAO.updateBatchMachinLocation(machinelocationList);
            logger.info("FeedProcessor: fininshed adding machine location data adding machine engine status data");
            testMachineDAO.addBatchMachineWithEngineStatus(machineEngineStatusList);
            logger.info(
                    "FeedProcessor: fininshed adding machine engine status data adding machine fuel consumption data");
//			testMachineDAO.addBatchMachineWithFuelConsume(machineFuelConsumptionList);
            logger.info("FeedProcessor: fininshed adding machine fuel consumpotion data");

			*/
/*if(machineLocationHistory!=null && machineLocationHistory.size()>0)
			{
				logger.info("Location Details Size "+machineLocationHistory.size());
				testMachineDAO.addLocationHistory(machineLocationHistory);
				machineLocationHistory.clear();
			}*//*

            machineDataList.clear();
            machineDataWithoutFuel.clear();
            machinelocationList.clear();
            machineEngineStatusList.clear();
            machineFuelConsumptionList.clear();
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            logger.info("Time for 1000 packets  "+elapsedTime);
        } catch (final Exception ex) {
            logger.error("Failed to execute batch");
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

        // Format is "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week]
        // [Year]"
        @Scheduled(cron = "0 0 0/1 * * ?")
        public void publish() throws ParseException {

        // take count value and set it to 0
        long hourlyCount = packetCount;
        // Reset Count
        packetCount = 0;

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
        logger.info("Hourly count value is " + hourlyCount);
        feedRepo.save(stats);
        if (hourlyCount == 0) {
            logger.info("Start Feedparser Mail");
            emailService.sendFeedParserStatusMail("Feedparser",dateformat.format(date),0);
        }
    }

    }

}
*/
