package com.wipro.jcb.livelink.app.dataprocess.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
public class MachineLocationRowMapper implements RowMapper<MachineLocation> {

    @Override
    public MachineLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
        MachineLocation machineLocation = new MachineLocation();
        machineLocation.setVin(rs.getString("vin"));
        machineLocation.setLatitude(rs.getDouble("latitude"));
        machineLocation.setLongitude(rs.getDouble("longitude"));
        return machineLocation;
    }
}
