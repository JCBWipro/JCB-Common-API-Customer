package com.wipro.jcb.livelink.app.dataprocess.dataparserDAO;

import com.wipro.jcb.livelink.app.dataprocess.constants.Constant;
import com.wipro.jcb.livelink.app.dataprocess.constants.EventLevel;
import com.wipro.jcb.livelink.app.dataprocess.constants.EventName;
import com.wipro.jcb.livelink.app.dataprocess.constants.EventType;
import com.wipro.jcb.livelink.app.dataprocess.dto.AlertData;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineData;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineEngineStatus;
import com.wipro.jcb.livelink.app.dataprocess.dto.MachineFuelConsumption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Transactional
@Repository
public class MachineDAO {
    private final Logger logger = LoggerFactory.getLogger(MachineDAO.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String MACHINE_WITHOUT_FUEL_LOCATION = "UPDATE machine SET  status_as_on_time = ?,total_machine_hours=?,battery_voltage=? WHERE vin = ? AND status_as_on_time < ?";
    private static final String MACHINE_WITH_FUEL_WITHOUT_LOCATION = "UPDATE machine SET  status_as_on_time = ?,total_machine_hours=?,battery_voltage=?,fuel_level=? WHERE vin = ? AND status_as_on_time < ?";
    private static final String MACHINE_WITHOUT_FUEL = "UPDATE machine SET  status_as_on_time = ?,longitude=?,latitude=?,total_machine_hours=?,battery_voltage=? WHERE vin = ? AND status_as_on_time < ?";
    private static final String MACHINE_WITH_FUEL = "UPDATE machine SET  status_as_on_time = ?,longitude=?,latitude=?,total_machine_hours=?,battery_voltage=?,fuel_level=? WHERE vin = ? AND status_as_on_time < ?";
    private static final String MACHINE_FUEL_HISTORY = "INSERT INTO  machinefuelhistorydata (vin, vin_id,date_time,fuel_level) VALUES (?, ?,?,?) on conflict do nothing";
    private static final String MACHINE_ENGINE_STATUS = "INSERT INTO  machineenginestatushistorydata (vin, vin_id,date_time,is_engine_on) VALUES (?, ?,?,?) on conflict do nothing";
    private static final String TIME_FENCE_MACHINES = "SELECT machine.vin,machine.status_as_on_time ,machin_user.user_id FROM machine join machin_user ON machine.vin = machin_user.vin WHERE machine.vin IN (:ids) AND machine.status_as_on_time NOT between (:today)::date+start_time::time and (:today)::date+end_time::time AND (start_time LIKE '%AM' OR start_time LIKE '%PM') AND (end_time LIKE '%AM' OR end_time LIKE '%PM')";
    private static final String ADD_ALERT = "INSERT INTO alert (alert_id,event_description,event_generated_time,event_level,event_name,event_type ,is_open,read_flag,vin,is_generated)VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String IS_ALERT_ID_EXISTS = "SELECT Count(vin) FROM alert WHERE alert_id = ?";
    private static final String GEO_FENCE_MACHINES = "SELECT machin_user.user_id,machine.vin,machine.radius ,round(6371 * acos( cos( radians(machine.center_lat) ) * cos( radians( machine.latitude ) ) * cos( radians( machine.longitude ) - radians(machine.center_long) ) + sin( radians(machine.center_lat) ) * sin( radians( machine.latitude ) )) ) as distance FROM machine join machin_user ON machine.vin = machin_user.vin WHERE machine.vin IN (:ids) and machine.center_lat > 0.0 and machine.center_long > 0.0 and machine.radius > 0.0";
    private static final String DELETE_GEO_FENCE_ALERT = "DELETE FROM alert WHERE vin IN (:ids) AND event_name =(:eventName)";
    private static final String IS_ALERT_EXISTS = "SELECT distinct(vin) FROM alert WHERE vin IN (:ids) AND event_name = (:eventName) AND is_open = (:isOpen)";

    public void updateBatchMachineWithoutFuel(List<MachineData> packets) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_WITHOUT_FUEL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getLongitude());
                    ps.setDouble(3, packet.getLatitude());
                    ps.setDouble(4, packet.getHmr());
                    ps.setDouble(5, packet.getBatteryVoltage());
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

    public void updateBatchMachineWithFuel(List<MachineData> packets) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_WITH_FUEL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getLongitude());
                    ps.setDouble(3, packet.getLatitude());
                    ps.setDouble(4, packet.getHmr());
                    ps.setDouble(5, packet.getBatteryVoltage());
                    ps.setDouble(6, packet.getFuelLevel());
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
    public void updateBatchMachineWithoutFuelLocation(List<MachineData> packets) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_WITHOUT_FUEL_LOCATION, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getHmr());
                    ps.setDouble(3, packet.getBatteryVoltage());
                    ps.setString(4, packet.getVin());
                    ps.setTimestamp(5, packet.getStausAsOn());
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

    public void updateBatchMachineWithFuelWithoutLocation(List<MachineData> packets) {
        try {
            jdbcTemplate.batchUpdate(MACHINE_WITH_FUEL_WITHOUT_LOCATION, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MachineData packet = packets.get(i);
                    ps.setTimestamp(1, packet.getStausAsOn());
                    ps.setDouble(2, packet.getHmr());
                    ps.setDouble(3, packet.getBatteryVoltage());
                    ps.setDouble(4, packet.getFuelLevel());
                    ps.setString(5, packet.getVin());
                    ps.setTimestamp(6, packet.getStausAsOn());
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
