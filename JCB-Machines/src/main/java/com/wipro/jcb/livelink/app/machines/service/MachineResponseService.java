package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.response.EngineFuelHistoryUtilizationDataV2;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponseV2;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponseV3;
import com.wipro.jcb.livelink.app.machines.service.response.MachineResponseV3;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public interface MachineResponseService {

    /**
     * Retrieves a paginated list of machines for a user.
     *
     * @param userName    The username of the user.
     * @param filter      Filter criteria for machines.
     * @param search      Search term for machines.
     * @param skipReports Whether to skip fetching reports.
     * @param pageNumber  The page number.
     * @param pageSize    The page size
     * @return A {@link MachineListResponseV2} containing the list of machines.
     * @throws ProcessCustomError If an error occurs during processing.
     */
    MachineListResponseV2 getMachineResponseListV2(String userName, String filter, String search, Boolean skipReports, String pageNumber,
                                                   String pageSize) throws ProcessCustomError;

    /**
     * Retrieves a paginated list of machines for a user. This method likely represents a newer version of the API.
     *
     * @param userName    The username of the user.
     * @param filter      Filter criteria for machines.
     * @param search      Search term for machines.
     * @param skipReports Whether to skip fetching reports.
     * @param pageNumber  The page number.
     * @param pageSize    The page size.
     * @return A {@link MachineListResponseV3} containing the list of machines.
     * @throws ProcessCustomError If an error occurs during processing.
     */
    MachineListResponseV3 getMachineResponseListV3(String userName, String filter, String search,
                                                   Boolean skipReports, String pageNumber, String pageSize) throws ProcessCustomError;

    /**
     * Retrieves detailed information about a specific machine.
     *
     * @param userName The username of the user.
     * @param vin      The VIN of the machine.
     * @return A {@link MachineResponseV3} containing the machine details.
     * @throws ProcessCustomError If an error occurs during processing.
     */
    MachineResponseV3 getMachineDetailsListV3(String userName, String vin) throws ProcessCustomError;

    Machine getMachineDetails(String vin, String userName);

    // public Machine getMachineDetails(String vin);
    EngineFuelHistoryUtilizationDataV2 getEngineFuelDetailData(String vin, Date startDate, Date endDate, String type) throws ProcessCustomError;

    /**
     * Retrieves MachineType based on Vin
     *
     * @param vin The VIN of the machine.
     */
    String getMachinetype(String vin);

}