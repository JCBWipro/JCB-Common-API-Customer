package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.dto.MachineDetailResponse;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface MachineDetailResponseService {

    /**
     * Retrieves machine details.
     *
     * @param machine The machine entity.
     * @param machineFeedparserdata The machine feed parser data.
     * @return A MachineDetailResponse object containing the machine details.
     * @throws ProcessCustomError If an error occurs during processing.
     */
    MachineDetailResponse getMachineDetailResponseList(Machine machine, MachineFeedParserData machineFeedparserdata) throws ProcessCustomError;

    /**
     * Retrieves machine details with an option to skip reports.
     *
     * @param machine             The machine entity.
     * @param machineFeedparserdata The machine feed parser data.
     * @param skipReports         Flag indicating whether to skip fetching reports.
     * @return A MachineDetailResponse object containing the machine details.
     * @throws ProcessCustomError If an error occurs during processing.
     */
    MachineDetailResponse getMachineDetailResponseListV2(Machine machine, MachineFeedParserData machineFeedparserdata, Boolean skipReports) throws ProcessCustomError;

    /**
     * Retrieves machine details for version 3.
     *
     * @param machine The machine entity.
     * @return A MachineDetailResponse object containing the machine details.
     * @throws ProcessCustomError If an error occurs during processing.
     */
    MachineDetailResponse getMachineDetailResponseListV3(Machine machine) throws ProcessCustomError;
}
