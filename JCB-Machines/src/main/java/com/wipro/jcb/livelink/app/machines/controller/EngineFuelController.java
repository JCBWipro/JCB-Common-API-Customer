package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.MsgResponseTemplate;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.exception.*;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.Date;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/16/2024
 * project: JCB-Common-API-Customer
 */
@RestController
@PropertySource("application.properties")
@Tag(name = "Customer", description = "Customer Machine API")
@RequestMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EngineFuelController {
    private static final Logger logger = LoggerFactory.getLogger(EngineFuelController.class);
    @Autowired
    Utilities utilities;
    @Autowired
    MachineResponseService machineResponseService;


    /*
     * Api to get Engine or Fuel utilization for one day data
     */
    @CrossOrigin
    @Operation(description = "Get Machine Engine and Fuel Utilization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Engine and Fuel Data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Request failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})

    @GetMapping("/getEngineFuelDetailData/{username}")
    public ResponseEntity<?> getEngineFuelDetailData(@PathVariable("username") String userName,
                                                     @RequestParam("vin") String vin,
                                                     @RequestParam(value = "startDate", defaultValue = "optional") String startDate,
                                                     @RequestParam(value = "endDate", defaultValue = "optional") String endDate, @RequestParam(value = "type", defaultValue = "Engine") String type) {
        try {
            if (userName != null) {
                if (vin != null) {
                    Machine machine = machineResponseService.getMachineDetails(vin, userName);
                    if (machine != null) {
                        logger.info("getEngineFuelDetailData: GET machine fuel and engine utilization: {}\nstartDate: {}\nendDate: {}", vin, startDate, endDate);
                        if (startDate.equalsIgnoreCase("optional")) {
                            startDate = utilities.getStartDate(6);
                            endDate = utilities.getStartDate(0);
                        }
                        if (utilities.getDate(endDate).before(new Date()) && utilities.getDate(startDate).before(new Date())) {

                            if (utilities.getDate(startDate).before(utilities.getDate(endDate)) || utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
                                return new ResponseEntity<>(machineResponseService
                                        .getEngineFuelDetailData(vin, utilities.getDate(startDate), utilities.getDate(endDate), type),
                                        HttpStatus.OK);
                            } else {
                                return ResponseEntity.badRequest().body(new MsgResponseTemplate("Fromdate should be less than todate", false));
                            }

                        } else {
                            return ResponseEntity.badRequest().body(new MsgResponseTemplate("Date should be less than or equal to current date", false));
                        }

                    } else {
                        return ResponseEntity.badRequest().body(new MsgResponseTemplate("Please select correct machine", false));
                    }

                } else {
                    return ResponseEntity.badRequest().body(new MsgResponseTemplate("Please select machine", false));
                }


            } else {
                logger.info("getEngineFuelDetailData: No valid session present");
                return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            logger.error(
                    "getEngineFuelDetailData: Issue faced while getting machine fuel and engine utilization data for vin ",
                    e);
            return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}