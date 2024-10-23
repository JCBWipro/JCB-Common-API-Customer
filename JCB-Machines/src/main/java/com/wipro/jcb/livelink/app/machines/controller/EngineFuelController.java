package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.FeedbackRequest;
import com.wipro.jcb.livelink.app.machines.dto.ResponseData;
import com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.entity.UsersFeedbackData;
import com.wipro.jcb.livelink.app.machines.exception.*;
import com.wipro.jcb.livelink.app.machines.repo.UserRepository;
import com.wipro.jcb.livelink.app.machines.repo.UsersFeedbackDataRepo;
import com.wipro.jcb.livelink.app.machines.service.EmailService;
import com.wipro.jcb.livelink.app.machines.service.LoadHistoricalDataService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.EngineFuelHistoryUtilizationDataV2;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/16/2024
 */
@Slf4j
@RestController
@PropertySource("application.properties")
@Tag(name = "Customer", description = "Customer Machine API")
@RequestMapping(value = "/user/machines", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EngineFuelController {
    private static final Logger logger = LoggerFactory.getLogger(EngineFuelController.class);
    @Autowired
    Utilities utilities;
    @Autowired
    MachineResponseService machineResponseService;
    @Autowired
    LoadHistoricalDataService loadHistoricalDataService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UsersFeedbackDataRepo usersFeedbackDataRepo;
    @Autowired
    EmailService emailService;


    /*
     * Api to get Engine or Fuel utilization for one day data
     */
    @CrossOrigin
    @Operation(summary = "Get Machine Engine and Fuel Utilization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Engine and Fuel Data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Request failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})

    @GetMapping("/getEngineFuelDetailData")
    public ResponseEntity<?> getEngineFuelDetailData(
            @RequestHeader("LoggedInUserRole") String userDetails,
            @RequestParam("vin") String vin,
            @RequestParam(value = "startDate", defaultValue = "optional") String startDate,
            @RequestParam(value = "endDate", defaultValue = "optional") String endDate,
            @RequestParam(value = "type", defaultValue = "Engine") String type) {

        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();
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
                                return new ResponseEntity<EngineFuelHistoryUtilizationDataV2>(machineResponseService
                                        .getEngineFuelDetailData(vin, utilities.getDate(startDate), utilities.getDate(endDate), type),
                                        HttpStatus.OK);
                            } else {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(HttpStatus.BAD_REQUEST, "Fromdate should be less than todate"));
                            }

                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(HttpStatus.BAD_REQUEST, "Date should be less than or equal to current date"));
                        }

                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(HttpStatus.BAD_REQUEST, "Please select correct machine"));
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(HttpStatus.BAD_REQUEST, "Please select machine"));

                }

            } else {
                logger.info("getEngineFuelDetailData: No valid session present");
                return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            logger.error(
                    "getEngineFuelDetailData: Issue faced while getting machine fuel and engine utilization data for vin");
            return ResponseEntity.status(e.getStatus()).body(new ApiError(e.getStatus(), e.getMessage()));
        } catch (final Exception e) {
            logger.error(
                    "getEngineFuelDetailData: Issue faced while getting machine fuel and engine utilization data for vin ",
                    e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request"));
        }

    }

    //Service Json Call Request
    @Operation(summary = "Service History Report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "process execution",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Request failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping("/servicecalljson")
    public ResponseEntity<?> getServiceCallJson(
            @RequestHeader("LoggedInUserRole") String userDetails,
            @RequestParam(value = "minusDays", defaultValue = "optional") Integer minusDays
    ) {
        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();
        Map<String, Object> response = null;
        try {
            log.info("Calling getServiceCallJson API");

            if (userName != null) {
                List<ServiceCallJsonData> request = loadHistoricalDataService.fetchServiceCallData();
                response = new HashMap<>();
                response.put("Days", minusDays);
                response.put("servicecallrequest", request);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "No valid session present","Session expired", null), HttpStatus.EXPECTATION_FAILED);

            }

        } catch (final Exception e) {
            log.error("Issue faced while processServiceHistoryData");
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /*
     * Api to get Engine and Fuel utilization day data based on standard/premium user.standard user only can view current day data.premiun user can view last 15 days data.
     */
    @Operation(summary = "Get Machine Engine and Fuel Utilization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Engine and Fuel Data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Request failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping( "/getEngineFuelDataV3")
    public ResponseEntity<?> getEngineFuelDataV3(
            @RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
            @RequestParam("vin") String vin,
            @RequestParam(value = "startDate", defaultValue = "optional") String startDate,
            @RequestParam(value = "endDate", defaultValue = "optional") String endDate) {

        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();
        try {
            if (userName != null) {
                if(vin!=null) {
                    Machine machine = machineResponseService.getMachineDetails(vin, userName);
                    if(machine!=null) {
                        logger.info("getEngineFuelDataV3: GET machine fuel and engine utilization: " + vin + "\nstartDate: "
                                + startDate + "\nendDate: " + endDate);
                        if (startDate.equalsIgnoreCase("optional")) {
                            startDate = utilities.getStartDate(6);
                            endDate = utilities.getStartDate(0);
                        }
                        if(utilities.getDate(endDate).before(new Date()) && utilities.getDate(startDate).before(new Date())) {
                            if(utilities.getDate(startDate).before(utilities.getDate(endDate)) || utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
                                return new ResponseEntity<EngineFuelHistoryUtilizationDataV2>(machineResponseService
                                        .getMachineEngineFuelDataV3(vin, utilities.getDate(startDate), utilities.getDate(endDate)),
                                        HttpStatus.OK);
                            }else {
                                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Fromdate should be lessthan todate ", "Fromdate should be lessthan todate", null), HttpStatus.EXPECTATION_FAILED);
                            }

                        }else {
                            return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Date should be lessthan or equal to current date", "Date should be lessthan or equal to current date", null), HttpStatus.EXPECTATION_FAILED);
                        }

                    }else {
                        return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Please select correct machine", "Please select correct machine", null), HttpStatus.EXPECTATION_FAILED);
                    }

                }else {
                    return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Please select machine", "Please select machine", null), HttpStatus.EXPECTATION_FAILED);
                }


            } else {
                log.info("getEngineFuelDataV3: No valid session present");
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error(
                    "getEngineFuelDataV3: Issue faced while getting machine fuel and engine utilization data for vin");
            log.info("Exception occured for EngineFuelDataV3 API :{} Param {} Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            log.error(
                    "getEngineFuelDataV3: Issue faced while getting machine fuel and engine utilization data for vin ",
                    e);
            log.info("Exception occured for EngineFuelDataV3 API :{} Param {} Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * This method processes the feedback provided by the user, saves it to the database, and sends a feedback email.
     */
    @Operation(summary = "user Feedback information", description = "user Feedback information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user Feedback Information",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Request failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})

    @PostMapping(value = "/feedback", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> userFeedback(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                          @RequestBody FeedbackRequest feedbackRequest) {
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                final User user = userRepository.findByUserName(userName);
                logger.info("feedback:POST Request from user {} email {} message {} ", userName, user.getEmail(),
                        feedbackRequest.getUserFeedback());
                usersFeedbackDataRepo.save(new UsersFeedbackData(userName, utilities.getStartDateTimeInDateFormat(0),
                        feedbackRequest.getUserFeedback()));
                emailService.sendFeedbackEmail(feedbackRequest.getUserFeedback(), userName);
                return new ResponseEntity<>(
                        new ResponseData("SUCCESS", "User feedback is processed successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            logger.error("Processing of user Feedback is failed ", e);
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
