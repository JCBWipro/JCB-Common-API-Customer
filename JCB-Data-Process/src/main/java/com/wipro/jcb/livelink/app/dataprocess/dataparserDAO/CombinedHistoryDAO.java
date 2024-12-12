package com.wipro.jcb.livelink.app.dataprocess.dataparserDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
/*
public class CombinedHistoryDAO {

    {
        private final Logger logger = LoggerFactory.getLogger(CombinedHistoryDAO.class);
        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Autowired
        private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        private static final String MACHINE_FUEL_HISTORY = "INSERT INTO  machinefuelhistorydata (vin, vin_id,date_time,fuel_level, created_by) VALUES (?, ?,?,?,?) on conflict do nothing";
        private static final String MACHINE_ENGINE_STATUS = "INSERT INTO  machineenginestatushistorydata (vin, vin_id,date_time,is_engine_on) VALUES (?, ?,?,?) on conflict do nothing";
        private static final String ADD_ALERT = "INSERT INTO alert (alert_id,event_description,event_generated_time,event_level,event_name,event_type ,is_open,read_flag,vin,is_generated)VALUES (?,?,?,?,?,?,?,?,?,?)";
        private static final String IS_ALERT_ID_EXISTS = "SELECT Count(vin) FROM alert WHERE alert_id = ?";
        private static final String DELETE_GEO_FENCE_ALERT = "DELETE FROM alert WHERE vin IN (:ids) AND event_name =(:eventName)";
        private static final String IS_ALERT_EXISTS = "SELECT distinct(vin) FROM alert WHERE vin IN (:ids) AND event_name = (:eventName) AND is_open = (:isOpen)";

        //New logic Queries
        private static final String MACHINE_LOCATION = "UPDATE machine_feedparser_location_data SET  status_as_on_time = ?,longitude=?,latitude=?,last_modified_date =?  WHERE vin = ? AND status_as_on_time < ? AND longitude != ? AND longitude != ?";
        private static final String MACHINE_INFORMATION = "UPDATE machine_feedparser_data SET  status_as_on_time = ?,total_machine_hours=?,battery_voltage=?,last_modified_date =?,hmr_packet_time=? WHERE vin = ? AND hmr_packet_time < ?";
        private static final String MACHINE_HAVING_FUEL = "UPDATE machine_feedparser_data SET  status_as_on_time = ?,total_machine_hours=?,battery_voltage=?,fuel_level=?,last_modified_date =?,fuel_packet_time=? WHERE vin = ? AND fuel_packet_time < ?";
        private static final String TIME_FENCE_MACHINES = "SELECT machine.vin,machine_feedparser_data.status_as_on_time ,machin_user.user_id FROM machine join machin_user ON machine.vin = machin_user.vin join machine_feedparser_data ON machine.vin = machine_feedparser_data.vin WHERE machine.vin IN (:ids) AND machine_feedparser_data.status_as_on_time NOT between (:today)::date+start_time::time and (:today)::date+end_time::time AND start_time IS NOT null AND end_time IS NOT null";
        private static final String GEO_FENCE_MACHINES = "SELECT machin_user.user_id,machine.vin,machine.radius ,round(6371 * acos( cos( radians(machine.center_lat) ) * cos( radians( machine_feedparser_location_data.latitude ) ) * cos( radians( machine_feedparser_location_data.longitude ) - radians(machine.center_long) ) + sin( radians(machine.center_lat) ) * sin( radians( machine_feedparser_location_data.latitude ) )) ) as distance FROM machine join machin_user ON machine.vin = machin_user.vin join machine_feedparser_location_data on ON machine.vin = machine_feedparser_location_data.vin WHERE machine.vin IN (:ids) and machine.center_lat > 0.0 and machine.center_long > 0.0 and machine.radius > 0.0";

        private static final String MACHINE_LOCATION_DATA = "SELECT vin, latitude, longitude from machine_feedparser_location_data where vin=?";
        private static final String MACHINE_LOCATION_HISTORY = "insert into machinelocationhistorydata (vin,vin_id,date_time,latitude,longitude) values (?,?,?,?,?) on conflict do nothing";
        private static final String MACHINE_LOCATION_HISTORY_UPDATE = "update machinelocationhistorydata set date_time =? where vin =? and latitude=? and longitude=? and date_time=? and created_at=?";
        private static final String MACHINE_WITHOUT_HMR = "UPDATE machine_feedparser_data SET  status_as_on_time = ?,last_modified_date =?,fuel_packet_time=? WHERE vin = ? AND fuel_packet_time < ?";

        public void addBatchMachineWithFuelConsume(List<MachineFuelConsumption> packets) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_FUEL_HISTORY, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineFuelConsumption packet = packets.get(i);
                    ps.setString(1, packet.getVin());
                    ps.setString(2, packet.getVin());
                    ps.setTimestamp(3, packet.getStausAsOn());
                    ps.setDouble(4, packet.getFuelLevelPerc());
                    ps.setString(5, "f");
                }

                @Override
                public int getBatchSize() {
                    return packets.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to add fuel consumption with message ", ex);
        }
    }

        public void addBatchMachineWithEngineStatus(List<MachineEngineStatus> packets) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_ENGINE_STATUS, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineEngineStatus packet = packets.get(i);
                    ps.setString(1, packet.getVin());
                    ps.setString(2, packet.getVin());
                    ps.setTimestamp(3, packet.getStausAsOn());
                    ps.setBoolean(4, packet.getEnginestatus());
                }

                @Override
                public int getBatchSize() {
                    return packets.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to add engine status with message ", ex);
        }
    }

        public List<Map<String, Object>> getMachineForTimefenceAlert(Set<String> vinList) {
        List<Map<String, Object>> machineInfo = new LinkedList<>();
        try {
            Date d1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(d1);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue(Constant.ID, vinList);
            parameters.addValue(Constant.TODAY, today);
            machineInfo = namedParameterJdbcTemplate.queryForList(TIME_FENCE_MACHINES, parameters);
            logger.debug("query  executed getMachineForTimefenceAlert records count is {} ", machineInfo.size());
        } catch (Exception ex) {
            logger.error("Failed to get getMachineForTimefenceAlert with message ", ex);
        }
        return machineInfo;
    }

        public List<Map<String, Object>> getMachineForGeofenceAlert(Set<String> vinList) {
        List<Map<String, Object>> machineInfo = new LinkedList<>();
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue(Constant.ID, vinList);
            machineInfo = namedParameterJdbcTemplate.queryForList(GEO_FENCE_MACHINES, parameters);
            logger.debug("query executed getMachineForGeofenceAlert records count is {}", machineInfo.size());
        } catch (Exception ex) {
            logger.error("Failed to get getMachineForGeofenceAlert with message", ex);
        }
        return machineInfo;
    }

        public void addTimefenceAlert(List<AlertData> alertData) {
        try {
            jdbcTemplate.batchUpdate(ADD_ALERT, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    AlertData alert = alertData.get(i);
                    ps.setString(1, alert.getAlertId());
                    ps.setString(2, Constant.TIME_FENCE_EVENT_DESCRIPTION);
                    ps.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
                    ps.setString(4, EventLevel.YELLOW.toString());
                    ps.setString(5, EventName.TIME_FENCE_ALERT.getName());
                    ps.setString(6, EventType.UTILIZATION.getName());
                    ps.setBoolean(7, true);
                    ps.setBoolean(8, true);
                    ps.setString(9, alert.getVin());
                    ps.setBoolean(10, true);
                }

                @Override
                public int getBatchSize() {
                    return alertData.size();
                }
            });
        } catch (final Exception ex) {
            logger.warn("Failed to update Timefence Alert with message ", ex);
        }
    }

        public void addGeofenceAlert(List<AlertData> alertData) {
        try {
            jdbcTemplate.batchUpdate(ADD_ALERT, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    AlertData alert = alertData.get(i);
                    ps.setString(1, alert.getAlertId());
                    ps.setString(2, Constant.GEO_FENCE_EVENT_DESCRIPTION);
                    ps.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
                    ps.setString(4, EventLevel.YELLOW.toString());
                    ps.setString(5, EventName.GEO_FENCE_ALERT.getName());
                    ps.setString(6, EventType.LANDMARK.getName());
                    ps.setBoolean(7, true);
                    ps.setBoolean(8, true);
                    ps.setString(9, alert.getVin());
                    ps.setBoolean(10, true);
                }

                @Override
                public int getBatchSize() {
                    return alertData.size();
                }
            });
        } catch (final Exception ex) {
            logger.warn("Failed to update GeofenceAlert with message ", ex);
        }
    }

        public int findByAlertById(String id) {
        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject(IS_ALERT_ID_EXISTS, new Object[] { id }, Integer.class);
        } catch (Exception ex) {
            logger.error("Failed to get alert id in findByAlertById with message", ex);
        }
        return count.intValue();
    }

        public int removeAlertForGeofence(Set<String> vinList) {
        int rows = 0;
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue(Constant.ID, vinList);
            parameters.addValue("eventName", EventName.GEO_FENCE_ALERT.getName());
            rows = namedParameterJdbcTemplate.update(DELETE_GEO_FENCE_ALERT, parameters);
        } catch (Exception ex) {
            logger.error("Failed during removeAlertForGeofence excution with message ", ex);
        }
        return rows;
    }
        public List<Map<String, Object>> findAlertByVin(Set<String> vinList, String eventName) {
        List<Map<String, Object>> alertVinList = new LinkedList<>();
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue(Constant.ID, vinList);
            parameters.addValue("eventName",eventName);
            parameters.addValue("isOpen",true);
            alertVinList = namedParameterJdbcTemplate.queryForList(IS_ALERT_EXISTS, parameters);
        } catch (Exception ex) {
            logger.error("Failed to get getMachineForGeofenceAlert with message", ex);
        }
        return alertVinList;

    }

        //************************Location queries
        public void updateBatchMachinLocation(List<MachineLocation> packets) {

        Timestamp lastModifiedDate = getLastModifiedDate();
        try {


            jdbcTemplate.batchUpdate(MACHINE_LOCATION, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {

                    MachineLocation packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getLongitude());
                    ps.setDouble(3, packet.getLatitude());
                    ps.setTimestamp(4, lastModifiedDate);
                    ps.setString(5, packet.getVin());
                    ps.setTimestamp(6, packet.getStausAsOn());
                    ps.setDouble(7, packet.getLongitude());
                    ps.setDouble(8, packet.getLatitude());

                }

                @Override
                public int getBatchSize() {
                    return packets.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to update updateBatchMachineWithoutFuel with message ", ex);
        }
    }
        public void updateBatchMachineFeedData(List<MachineData> packets) {
        Timestamp lastModifiedDate = getLastModifiedDate();

        try {
            jdbcTemplate.batchUpdate(MACHINE_INFORMATION, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getHmr());
                    ps.setDouble(3, packet.getBatteryVoltage());
                    ps.setTimestamp(4, lastModifiedDate);
                    ps.setTimestamp(5, packet.getStausAsOn());
                    ps.setString(6, packet.getVin());
                    ps.setTimestamp(7, packet.getStausAsOn());
                }

                @Override
                public int getBatchSize() {
                    return packets.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to update updateBatchMachineWithoutFuel with message ", ex);
        }
    }

        public void updateBatchMachineFeedDataHavingFuel(List<MachineData> packets) {
        Timestamp lastModifiedDate = getLastModifiedDate();
        try {
            jdbcTemplate.batchUpdate(MACHINE_HAVING_FUEL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getHmr());
                    ps.setDouble(3, packet.getBatteryVoltage());
                    ps.setDouble(4, packet.getFuelLevel());
                    ps.setTimestamp(5, lastModifiedDate);
                    ps.setTimestamp(6, packet.getStausAsOn());
                    ps.setString(7, packet.getVin());
                    ps.setTimestamp(8, packet.getStausAsOn());
                }

                @Override
                public int getBatchSize() {
                    return packets.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to update updateBatchMachineWithFuel with message ", ex);
        }
    }

        public Timestamp getLastModifiedDate() {
        Date d1 = new Date();
        Timestamp statusAsOnTime = null;
        try {
            statusAsOnTime = new java.sql.Timestamp(d1.getTime() + 19800000L);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return statusAsOnTime;

    }

        public MachineLocation getExistingMachineLocation(final String vin) {
        return jdbcTemplate.queryForObject(MACHINE_LOCATION_DATA, new Object[] {vin}, new MachineLocationRowMapper());
    }

        public void addBatchMachineLocationHistory(List<MachineLocationHistory> machineLocationHistory) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_LOCATION_HISTORY, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineLocationHistory packet = machineLocationHistory.get(i);
                    ps.setString(1, packet.getVin());
                    ps.setString(2, packet.getVin());
                    ps.setTimestamp(3, packet.getStausAsOn());
                    ps.setDouble(4, packet.getLatitude());
                    ps.setDouble(5, packet.getLongitude());

                }

                private java.sql.Date getCreatedDate(Date createdAt) {
                    // TODO Auto-generated method stub
                    return new java.sql.Date(createdAt.getTime());
                }

                @Override
                public int getBatchSize() {
                    return machineLocationHistory.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to add fuel machine location history with message ", ex);
        }
    }

        public void updateBatchmachineLocationHistory(List<MachineLocationHistoryUpdateData> updateMachineLocationHistory) {

        try {


            jdbcTemplate.batchUpdate(MACHINE_LOCATION_HISTORY_UPDATE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineLocationHistoryUpdateData packet = updateMachineLocationHistory.get(i);


                    ps.setTimestamp(1, packet.getNewStausAsOn());
                    ps.setString(2, packet.getVin());
                    ps.setDouble(3, packet.getLatitude());
                    ps.setDouble(4, packet.getLongitude());
                    ps.setTimestamp(5, packet.getOldStausAsOn());
                    ps.setDate(6, getCreatedDate(packet.getCreatedAt()));


                }

                private java.sql.Date getCreatedDate(Date createdAt) {
                    // TODO Auto-generated method stub
                    return new java.sql.Date(createdAt.getTime());
                }

                @Override
                public int getBatchSize() {
                    return updateMachineLocationHistory.size();
                }
            });
        }
        catch(Exception e)
        {
            logger.error("Failed to add fuel machine location history with message ", e.getMessage());
            e.printStackTrace();

        }
    }

        public void addLocationHistory(List<MachineLocationHistory> machineLocationHistory) {
        try {

            jdbcTemplate.batchUpdate(MACHINE_LOCATION_HISTORY,new BatchPreparedStatementSetter(){

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineLocationHistory locationDetails = machineLocationHistory.get(i);
                    ps.setString(1, locationDetails.getVin());
                    ps.setString(2, locationDetails.getVin());
                    ps.setTimestamp(3, locationDetails.getStausAsOn());
                    ps.setDouble(4, locationDetails.getLatitude());
                    ps.setDouble(5, locationDetails.getLongitude());

                }

                @Override
                public int getBatchSize() {
                    // TODO Auto-generated method stub
                    return machineLocationHistory.size();
                }

            });

        }catch(Exception e)
        {
            logger.error("Exception in save "+e.getMessage());
            e.printStackTrace();
        }

    }

        public void updateStatusOnTime(List<MachineData> packets) {
        Timestamp lastModifiedDate = getLastModifiedDate();
        try {
            jdbcTemplate.batchUpdate(MACHINE_WITHOUT_HMR, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setTimestamp(2, lastModifiedDate);
                    ps.setTimestamp(3, packet.getStausAsOn());
                    ps.setString(4, packet.getVin());
                    ps.setTimestamp(5, packet.getStausAsOn());
                }

                @Override
                public int getBatchSize() {
                    return packets.size();
                }
            });
        } catch (final Exception ex) {
            logger.error("Failed to update updateBatchMachineWithFuel with message ", ex);
        }
    }



    }
}
*/
