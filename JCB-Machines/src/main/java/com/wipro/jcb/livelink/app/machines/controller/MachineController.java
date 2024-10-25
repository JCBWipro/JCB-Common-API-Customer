package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.config.AppConfiguration;
import com.wipro.jcb.livelink.app.machines.constants.ConstantConfig;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.ApiOK;
import com.wipro.jcb.livelink.app.machines.dto.ResponseData;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.enums.FilterSearchType;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.request.GeofenceSetRequest;
import com.wipro.jcb.livelink.app.machines.request.TimefenceSetRequest;
import com.wipro.jcb.livelink.app.machines.service.response.*;
import com.wipro.jcb.livelink.app.machines.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.machines.service.MachineProfileService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 */

@Slf4j
@RestController
@PropertySource("application.properties")
@Tag(name = "Customer", description = "Customer Machine API")
@RequestMapping(value = "/user/machines", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MachineController {

    @Autowired
    MachineResponseService machineResponseService;

    @Autowired
    MachineProfileService machineProfileService;


    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    MachineService machineService;

    @Autowired
    MachineRepository machineRepository;

    @Autowired
    Utilities utilities;

    @Autowired
    AdvanceReportService advanceReportService;

    @GetMapping(value = "/appconfig")
    @Operation(summary = "Get app configuration", description = "App configuration specific to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Config data successfully sent", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppConfiguration.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Config request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })

    public @ResponseBody ResponseEntity<?> getAppConfig() {
        try {
            log.info("getAppConfig: Received request for app configuration.");
            AppConfiguration config = appConfiguration;
            log.debug("getAppConfig: Retrieved app configuration: {}", config);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } catch (Exception e) {
            log.error("getAppConfig: Error retrieving app configuration.", e);
            return new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve app configuration",
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/machinesV2")
    @Operation(summary = "List all machines for the current user", description = "Retrieves a paginated list of machines associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine List", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> getMachinesVTwo(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Page number (zero-based)") @RequestParam(value = "pageNumber", defaultValue = "0") String pageNumber,
            @Parameter(description = "Page size") @RequestParam(value = "pageSize", defaultValue = "${controller.customer.machines.pageSize}") String pageSize,
            @Parameter(description = "Filter criteria (comma-separated values, e.g., '3DX,Super,ecoXcellence')") @RequestParam(value = "filter", defaultValue = "optional") String filter,
            @Parameter(description = "Search term") @RequestParam(value = "search", defaultValue = "optional") String search,
            @Parameter(description = "Skip fetching reports (true/false)") @RequestParam(value = "skipReports", required = false) Boolean skipReports) {

        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("Session expired for user details: {}", userDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            log.info("machines: GET request from user {}", userName);
            MachineListResponseV2 response = machineResponseService.getMachineResponseListV2(userName, filter, search, skipReports, pageNumber, pageSize);
            return ResponseEntity.ok(response);

        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting machine list for Customer. Parameters: filter={}, search={}", filter, search, e);
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiError(e.getStatus(), e.getMessage(), e.getErrorCode(), e.getDetails()));

        } catch (final IllegalArgumentException e) {
            log.warn("Invalid input parameters: filter={}, search={}. Error: {}", filter, search, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "Invalid input parameters", "INVALID_INPUT", null));

        } catch (final Exception e) {
            log.error("Unexpected error fetching machine list: filter={}, search={}", filter, search, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

    @GetMapping(value = "/machinesdetailsV3")
    @Operation(summary = "Get machine details for the current user", description = "Retrieves detailed information about a specific machine identified by its VIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine Details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineResponseV3.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> getMachinesDetailsVThree(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Vehicle Identification Number (VIN) of the machine", required = true) @RequestParam("vin") String vin) {

        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();
        try {
            if (userName == null) {
                log.warn("getMachinesDetailsVThree: Username not found in user details.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user details"));
            }

            log.info("getMachinesDetailsVThree: GET request from user {} for VIN {}", userName, vin);

            MachineResponseV3 machineDetails = machineResponseService.getMachineDetailsListV3(userName, vin);
            return ResponseEntity.ok(machineDetails);

        } catch (ProcessCustomError e) {
            log.error("getMachinesDetailsVThree: ProcessCustomError occurred while fetching machine details for VIN {}: {}", vin, e.getMessage(), e);
            return ResponseEntity.status(e.getStatus()).body(new ApiError(e.getStatus(), e.getMessage()));

        } catch (Exception e) {
            log.error("getMachinesDetailsVThree: An unexpected error occurred while fetching machine details for VIN {}: {}", vin, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request"));
        }
    }

    @GetMapping(value = "/machinesV3", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "List all machines for the current user", description = "Retrieves a paginated list of machines associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine List", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponseV3.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> getMachinesVThree(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Page number (zero-based)") @RequestParam(value = "pageNumber", defaultValue = "0") String pageNumber,
            @Parameter(description = "Page size") @RequestParam(value = "pageSize", defaultValue = "${controller.customer.machines.pageSize}") String pageSize,
            @Parameter(description = "Filter criteria (comma-separated values, e.g., '3DX,Super,ecoXcellence')") @RequestParam(value = "filter", defaultValue = "optional") String filter,
            @Parameter(description = "Search term") @RequestParam(value = "search", defaultValue = "optional") String search,
            @Parameter(description = "Skip fetching reports (true/false)") @RequestParam(value = "skipReports", required = false) Boolean skipReports) {

        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("Session expired for user details:- {}", userDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            log.info("machinesV3: GET request from user {}", userName);
            MachineListResponseV3 response = machineResponseService.getMachineResponseListV3(
                    userName, filter, search, skipReports, pageNumber, pageSize);
            return ResponseEntity.ok(response);

        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting machine list for Customer. Parameters: filter={}, search={}", filter, search, e);
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiError(e.getStatus(), e.getMessage(), e.getErrorCode(), e.getDetails()));

        } catch (final IllegalArgumentException e) {
            log.warn("Invalid input parameters: filter: ={}, search: ={} Error: {}", filter, search, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "Invalid input parameters", "INVALID_INPUT", null));

        } catch (final Exception e) {
            log.error("Unexpected error fetching machine list: filter={}, search={}", filter, search, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

    /*
     * This End Point is used to get MachineProfile related details
     */
    @Operation(summary = "Get Machine profile", description = "Fetch Machine profile details by Vin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Machine profile"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")
    })
    @Transactional(readOnly = true)
    @GetMapping("/machineprofile")
    public ResponseEntity<?> getMachineProfile(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails, @RequestParam(required = false) String vin) {
        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();
        try {
            if (userName != null) {
                log.info("machineprofile:GET Request from user {}", userName);
                return new ResponseEntity<>(machineProfileService.getMachineProfile(userName, vin),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error("machineProfile:GET Request failed for machineprofile with fields for {}", vin);
            log.info("Exception occured for Machineprofile API :- {}-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            log.error("machineprofile:GET Request failed for machineprofile with fields for {}", vin);
            log.info("Exception occured for Machineprofile API :{}-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * This End Point is used to Update MachineProfile related details
     */
    @Operation(summary = "Update machine profile", description = "Update machine profile and fetch the Latest Machine Profile Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update machine profile"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")
    })
    @PutMapping(value = "/machineprofile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putMachineProfile(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                               @RequestPart(value = "operatorName", required = false) String operatorName, @RequestPart("vin") String vin,
                                               @RequestPart(value = "tag", required = false) String tag,
                                               @RequestPart(value = "hours", required = false) String hours,
                                               @RequestPart("jcbCertified") String jcbCertified,
                                               @RequestPart(value = "phoneNumber", required = false) String phoneNumber,
                                               @RequestPart(value = "workEnd", required = false) String workEnd,
                                               @RequestPart(value = "workStart", required = false) String workStart,
                                               @RequestPart(value = "site", required = false) String site,
                                               @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("machineprofile:GET request from user {}", userName);
                machineProfileService.putMachineProfile(userName, vin, operatorName != null ? operatorName : "",
                        phoneNumber != null ? phoneNumber : "", hours != null ? hours : "",
                        workStart != null ? workStart : "", workEnd != null ? workEnd : "", jcbCertified,
                        tag != null ? tag : "", site != null ? site : "", image);
                log.info("machineprofile:GET end of request from user {}", userName);
                return new ResponseEntity<>(machineProfileService.getMachineProfile(userName, vin),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error("machineProfile:GET request failed for Vin {} ", vin);
            return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            log.error("machineProfile:GET request failed for vin {} ", vin);
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * This End Point is to Set Machine Geofence related details
     */
    @Operation(summary = "Set Machine Geofence", description = "Setting GeoFencing Parameter for machines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting GeoFencing Parameter for machines"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")})
    @PostMapping("/setmachinegeofenceparam")
    public ResponseEntity<?> setGeoFencingForMachine(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                     @RequestBody GeofenceSetRequest geofenceSetRequest) {
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("setMachineGeofenceParam: POST Request for machine {} user {} ", geofenceSetRequest, userName);
                if (null != geofenceSetRequest.getVin() && null != geofenceSetRequest.getCenterLatitude()
                        && null != geofenceSetRequest.getCenterLongitude() && null != geofenceSetRequest.getRadis()) {
                    machineService.setMachineGeoFenceParam(geofenceSetRequest.getVin(), geofenceSetRequest.getCenterLatitude(),
                            geofenceSetRequest.getCenterLongitude(), geofenceSetRequest.getRadis());
                    log.info("Geofencing parameters CenterLatitude:{} "
                                    + "CenterLongitude:{} and Radius:{} updated Successfully",
                            geofenceSetRequest.getCenterLatitude(), geofenceSetRequest.getCenterLongitude(),
                            geofenceSetRequest.getRadis());
                    return new ResponseEntity<>(
                            new ResponseData("Success", "Geofencing parameters CenterLatitude, CenterLongitude and Radius Updated Successfully"), HttpStatus.OK);
                } else {
                    throw new ProcessCustomError("Please provide valid value for geofence",
                            HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("setGeoFencingForMachine: Issue faced while setting Geofence parameters for machine ", e);
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * This End Point is to Set Machine TimeFence related details
     */
    @Operation(summary = "Set Machine TimeFence", description = "Setting TimeFencing Parameter for machines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting Timefencing Parameter for machines"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")})
    @PostMapping("/setmachinetimefenceparam")
    public ResponseEntity<?> setTimeFencingForMachine(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                      @RequestBody TimefenceSetRequest timefenceSetRequest) {
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("setMachineTimeFenceParam: POST  params for machine is  {} user {}", timefenceSetRequest, userName);
                if (null != timefenceSetRequest.getVin() && null != timefenceSetRequest.getStartTime()
                        && null != timefenceSetRequest.getEndTime()) {
                    machineService.setMachineTimeFence(timefenceSetRequest.getVin(), timefenceSetRequest.getStartTime(),
                            timefenceSetRequest.getEndTime());
                    log.info("TimeFence parameters StartTime:{} and EndTime:{} Updated Successfully", timefenceSetRequest.getStartTime(), timefenceSetRequest.getEndTime());
                    return new ResponseEntity<>(
                            new ResponseData("Success", "TimeFence parameters StartTime and EndTime Updated Successfully"), HttpStatus.OK);
                } else {
                    throw new ProcessCustomError("Please provide valid value for timeFence",
                            HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("setTimeFencingForMachine: Issue faced while setting TimeFence parameters for machine ", e);
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * This End Point is to Fetch Machine Location related details
     */
    @Operation(summary = "Get Machine Location", description = "Get Machine Location related details")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get Machine Location related details"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")})
    @GetMapping("/machinelocation")
    public ResponseEntity<?> getMachineLocationDetail(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                      @RequestParam(value = "vin", required = false) String vin, @RequestParam("pageNumber") String pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = "${machines.location.pageSize}") String pageSize) {
        String userName = null;
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            userName = userResponse.getUserName();
            log.info("Machine Location History : Get request for machine {} User {} ", vin, userName);
            if (userName != null) {
                if (vin != null) {
                    Machine machine = machineRepository.findByVinAndUserName(vin, userName);
                    if (machine != null) {
                        return new ResponseEntity<>(
                                machineService.getMachineLocationDetail(vin, pageNumber, pageSize), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                "Please select correct machine", "Please select correct machine", null),
                                HttpStatus.EXPECTATION_FAILED);
                    }
                } else {
                    return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                            "Please select machine", "Please select machine", null), HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("Issue faced while getting machineLocation request {}", e.getMessage());
            log.info("Exception occurred for Machine Location API :{}-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * This End Point is to Fetch Machine Location History details
     */
    @Operation(summary = "Machine Location History Details", description = "Machine Location History Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Machine Location History Information"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")})
    @GetMapping("/machinelocationhistory")
    public ResponseEntity<?> getMachineLocationHistory(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                       @RequestParam(value = "vin", required = false) String vin,
                                                       @RequestParam(value = "date", required = false) String date) {
        String userName = null;
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            userName = userResponse.getUserName();
            log.info("Machine Location History Details: Get request for machine {} User {} StartDate {}", vin, userName,
                    date);
            if (userName != null) {
                if (vin != null) {
                    Machine machine = machineRepository.findByVinAndUserName(vin, userName);
                    if (machine != null) {
                        if (date != null && !date.isEmpty()) {
                            if (utilities.getDate(date).before(new Date())) {
                                return new ResponseEntity<>(new MachineLocationHistory(vin,
                                        machineService.getMachineLocationHistory(vin, date)), HttpStatus.OK);
                            } else {
                                return new ResponseEntity<>(
                                        new ApiError(HttpStatus.EXPECTATION_FAILED,
                                                "date should be less than current date",
                                                "date should be less than current date", null),
                                        HttpStatus.EXPECTATION_FAILED);
                            }
                        } else {
                            return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                    "Please select date", "Please select date", null), HttpStatus.EXPECTATION_FAILED);
                        }
                    } else {
                        return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                "Please select correct machine", "Please select correct machine", null),
                                HttpStatus.EXPECTATION_FAILED);
                    }
                } else {
                    return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                            "Please select machine", "Please select machine", null), HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("Issue faced while getting listofdownmachines request- {}", e.getMessage());
            log.info("Exception occurred for Machine Location History API :{}-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * This End Point is to Get Machine Utilization Report Details
     */
    @Operation(summary = "Machine Utilization Report", description = "Machine Utilization Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine Utilization Information"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")})
    @GetMapping("/machineutilizationreport")
    public ResponseEntity<?> getMachineUtilizationReport(
            @RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
            @RequestParam(value = "vin", required = false) String vin,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        String userName = null;
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            userName = userResponse.getUserName();
            log.info("Machine Utilization Request Request: Get request for machine {} User {} StartDate {}  EndDate {} ",
                    vin, userName, startDate, endDate);

            if (userName != null) {
                if (vin != null) {
                    Machine machine = machineRepository.findByVinAndUserName(vin, userName);
                    if (machine != null) {
                        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                            if (utilities.getDate(startDate).before(new Date())
                                    && utilities.getDate(endDate).before(new Date())) {

                                if (utilities.getDate(startDate).before(utilities.getDate(endDate))
                                        || utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
                                    return new ResponseEntity<>(
                                            advanceReportService.getMachineUtilization(vin, startDate, endDate),
                                            HttpStatus.OK);
                                } else {
                                    return new ResponseEntity<>(
                                            new ApiError(HttpStatus.EXPECTATION_FAILED,
                                                    "FromDate should be less than toDate ",
                                                    "FromDate should be less than toDate", null),
                                            HttpStatus.EXPECTATION_FAILED);
                                }
                            } else {
                                return new ResponseEntity<>(
                                        new ApiError(HttpStatus.EXPECTATION_FAILED,
                                                "FromDate,toDate should be less than currentDate",
                                                "FromDate,toDate should be less than currentDate", null),
                                        HttpStatus.EXPECTATION_FAILED);
                            }
                        } else {
                            return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                    "Please select fromDate and toDate ", "Please select fromDate and toDate", null),
                                    HttpStatus.EXPECTATION_FAILED);
                        }
                    } else {
                        return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                "Please select correct machine", "Please select correct machine", null),
                                HttpStatus.EXPECTATION_FAILED);
                    }
                } else {
                    return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                            "Please select machine", "Please select machine", null), HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("Issue faced while getting listofdownmachines request {}", e.getMessage());
            log.info("Exception occurred for Machine Utilization Report API :{}-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/machineserviceinfo")
    @Operation(summary = "Get machine service information", description = "Retrieves service information for a specific machine identified by its VIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine Service Information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineServiceInfo.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> getMachineServiceInfo(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Vehicle Identification Number (VIN) of the machine", required = true) @RequestParam("vin") String vin) {

        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("Session expired for user details : {}", userDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            log.info("machineServiceInfo: GET request from user {} for VIN {}", userName, vin);
            MachineServiceInfo response = machineService.getMachineServiceInfo(vin);
            return ResponseEntity.ok(response);

        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting machine service info for VIN {}. ", vin, e);
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiError(e.getStatus(), e.getMessage(), e.getErrorCode(), e.getDetails()));

        } catch (final IllegalArgumentException e) {
            log.warn("Invalid input parameters: VIN: {} Error: {}", vin, e.getMessage());
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "Invalid input parameters", "INVALID_INPUT", null));

        } catch (final Exception e) {
            log.error("Unexpected error fetching machine service info for VIN {}.", vin, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

    /*
     * This End Point is to Get Fuel Utilization Report Details
     */
    @CrossOrigin
    @Operation(summary = "Fuel Utilization Report", description = "Fuel Utilization Information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Fuel Utilization Information"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")})
    @Transactional(readOnly = true)
    @GetMapping("/fuelutilizationreport")
    public ResponseEntity<?> getFuelUtilizationReport(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                      @RequestParam(value = "vin", required = false) String vin,
                                                      @RequestParam(value = "startDate", required = false) String startDate,
                                                      @RequestParam(value = "endDate", required = false) String endDate) {
        String userName = null;
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            userName = userResponse.getUserName();
            log.info("Fuel Utilization Request: Get request for machine {} User {} StartDate {}  EndDate {} ", vin,
                    userName, startDate, endDate);
            if (userName != null) {
                if (vin != null) {

                    Machine machine = machineRepository.findByVinAndUserName(vin, userName);
                    if (machine != null) {
                        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                            if (utilities.getDate(startDate).before(new Date())
                                    && utilities.getDate(endDate).before(new Date())) {

                                if (utilities.getDate(startDate).before(utilities.getDate(endDate))
                                        || utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
                                    return new ResponseEntity<>(
                                            advanceReportService.getFuelUtilization(vin, startDate, endDate),
                                            HttpStatus.OK);
                                } else {
                                    return new ResponseEntity<>(
                                            new ApiError(HttpStatus.EXPECTATION_FAILED,
                                                    "Fromdate should be lessthan todate ",
                                                    "Fromdate should be lessthan todate", null),
                                            HttpStatus.EXPECTATION_FAILED);
                                }
                            } else {
                                return new ResponseEntity<>(
                                        new ApiError(HttpStatus.EXPECTATION_FAILED,
                                                "Fromdate,todate should be lessthan current date",
                                                "Fromdate,todate should be lessthan current date", null),
                                        HttpStatus.EXPECTATION_FAILED);
                            }
                        } else {
                            return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                    "Please select fromdate and todate ", "Please select fromdate and todate", null),
                                    HttpStatus.EXPECTATION_FAILED);
                        }
                    } else {
                        return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                                "Please select correct machine", "Please select correct machine", null),
                                HttpStatus.EXPECTATION_FAILED);
                    }
                } else {
                    return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                            "Please select machine", "Please select machine", null), HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("Issue faced while getting listofdownmachines request {}", e.getMessage());
            log.info("Exception occured for Fuel Utilization Report API :{}-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/locationV3", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get Machine Location Data", description = "Get Machine Location Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine Location", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiOK.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))})
    public ResponseEntity<?> getMachineLocationData(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Latitude of the location") @RequestParam("latitude") String latitude,
            @Parameter(description = "Longitude of the location") @RequestParam("longitude") String longitude) {

        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("getMachineLocationData: Username not found in user details.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user details"));
            }

            log.info("getMachineLocationData: GET request from user {} for latitude {}, longitude {}", userName, latitude, longitude);

            if (latitude == null || latitude.isEmpty() || longitude == null || longitude.isEmpty()) {
                log.warn("getMachineLocationData: Latitude or longitude is missing.");
                return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "Latitude and longitude are required."));
            }

            AddressResponse locationData = utilities.getLocationDetails(latitude, longitude);
            return ResponseEntity.ok(locationData);

        } catch (ProcessCustomError e) {
            log.error("getMachineLocationData: ProcessCustomError occurred while fetching location data: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getStatus()).body(new ApiError(e.getStatus(), e.getMessage()));

        } catch (Exception e) {
            log.error("getMachineLocationData: An unexpected error occurred while fetching location data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request"));
        }
    }

    @GetMapping(value = "/autosuggestion")
    @Operation(summary = "Auto suggestion based on type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of suggested words based on the type eg alerts of machine owned by user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))})
    public ResponseEntity<?> autoSuggestion(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Word for suggestion") @RequestParam String word,
            @Parameter(description = "Filter search type") @RequestParam FilterSearchType type) {

        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("autoSuggestion: Username not found in user details.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user details"));
            }

            log.info("autoSuggestion: GET Request for filter type {} user {}", type, userName);
            List<String> suggestions = utilities.getSuggestions(word, userName, type);
            return ResponseEntity.ok(suggestions);

        } catch (Exception e) {
            log.error("autoSuggestion: An unexpected error occurred while fetching suggestions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request"));
        }
    }

    @GetMapping(value = "/filters", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Filters based on the type eg alerts of the machine owned by user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFilter.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> filters(
            @Parameter(description = "User details from the token", required = true) @RequestHeader("LoggedInUserRole") String userDetails,
            @Parameter(description = "Filter search type") @RequestParam FilterSearchType type) {

        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("filters: Username not found in user details.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user details"));
            }

            log.info("filters: GET Request for filter type {} user {}", type, userName);
            ResponseFilter responseFilter = new ResponseFilter(utilities.getFilters(userName, type));
            return ResponseEntity.ok(responseFilter);

        } catch (final Exception e) {
            log.error("filters: GET request for filter has been failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

    @GetMapping(value = "/generatelink", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generate live location link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Live Location Link", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT, readOnly = false)
    public ResponseEntity<?> generateLiveLocationLink(@RequestHeader("LoggedInUserRole") String userDetails,
                                                                     @RequestParam(value = "vin") String vin,
                                                                     @RequestParam(value = "slot") String slot) {
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("Generate link: GET request from user {}", userName);
                if (!vin.isEmpty() && !slot.isEmpty()) {
                    Machine machine = machineResponseService.getMachineDetails(vin, userName);
                    if (machine != null) {
                        LiveLocationData liveLocationData = machineResponseService.generateLiveLocationLink(vin, slot, userName);
                        return new ResponseEntity<>(liveLocationData, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Please select correct machine", "Please select correct machine", null), HttpStatus.EXPECTATION_FAILED);
                    }
                } else {
                    return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Please give vin and slot", "Please give vin and slot", null), HttpStatus.EXPECTATION_FAILED);
                }

            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error("Issue faced while generating live location {}", e.getMessage());
            return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            log.error("Issue faced while generating the live location{}", e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}