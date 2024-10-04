package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.config.AppConfiguration;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.ResponseData;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.request.GeofenceSetRequest;
import com.wipro.jcb.livelink.app.machines.request.TimefenceSetRequest;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponse;
import com.wipro.jcb.livelink.app.machines.service.MachineProfileService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponseV2;
import com.wipro.jcb.livelink.app.machines.service.response.MachineResponseV3;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;

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
 * project: JCB-Common-API-Customer
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
	private MachineService machineService;

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
    
    /*
     * This End Point is used to get MachineProfile related details
     */
    @CrossOrigin
    @Operation(summary = "Get Machine profile", description = "Fetch Machine profile details by Vin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Machine profile"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")
    })
	@Transactional(readOnly = true)
    @GetMapping("/machineprofile")
	public ResponseEntity<?> getMachineProfile(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails, @RequestParam(required=false) String vin) {
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
			log.error("machineprofile:GET Request failed for machineprofile with fields for " + vin);
			log.info("Exception occured for Machineprofile API :"+userName+"-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error("machineprofile:GET Request failed for machineprofile with fields for " + vin);
			log.info("Exception occured for Machineprofile API :"+userName+"-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
    
    /*
     * This End Point is used to Update MachineProfile related details
     */
    @CrossOrigin
    @Operation(summary = "Update machine profile", description = "Update machine profile and fetch the Latest Machine Profile Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update machine profile"),
            @ApiResponse(responseCode = "401", description = "Auth Failed"),
            @ApiResponse(responseCode = "500", description = "Request failed")
    })
	@PutMapping(value="/machineprofile", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
			log.error("machineprofile:GET request failed for vin {} ", vin);
			return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error("machineprofile:GET request failed for vin {} ", vin);
			return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/*
     * This End Point is to Set Machine Geofence related details
     */
	@CrossOrigin
	@Operation(summary = "Set Machine Geofence", description = "Setting GeoFencing Parameter for machines")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Setting GeoFencing Parameter for machines"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@PostMapping("/setmachinegeofenceparam")
	public ResponseEntity<?> setGeoFencingForMachine(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
			@RequestBody GeofenceSetRequest geofenceSetRequest) {
		try {
			GeofenceSetRequest gfSetRequest = geofenceSetRequest;
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			String userName = userResponse.getUserName();
			if (userName != null) {
				log.info("setmachinegeofenceparam: POST Request for machine {} user {} ", gfSetRequest, userName);
				if (null != gfSetRequest.getVin() && null != gfSetRequest.getCenterLatitude()
						&& null != gfSetRequest.getCenterLongitude() && null != gfSetRequest.getRadis()) {
					machineService.setMachineGeoFenceParam(gfSetRequest.getVin(), gfSetRequest.getCenterLatitude(),
							gfSetRequest.getCenterLongitude(), gfSetRequest.getRadis());
					log.info("Geofencing parameters updated Successfully");
					return new ResponseEntity<ResponseData>(
							new ResponseData("Success", "Geofencing parameters updated Successfully"), HttpStatus.OK);
				} else {
					throw new ProcessCustomError("Please provide valid value for geofence",
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("setGeoFencingForMachine: Issue faced while setting Geofence parameters for machine ", e);
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This End Point is to Set Machine Timefence related details
	 */
	@CrossOrigin
	@Operation(summary = "Set Machine Timefence", description = "Setting Timefencing Parameter for machines")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Setting Timefencing Parameter for machines"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@PostMapping("/setmachinetimefenceparam")
	public ResponseEntity<?> setTimeFencingForMachine(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
			@RequestBody TimefenceSetRequest timefenceSetRequest) {
		try {
			TimefenceSetRequest tfSetRequest = timefenceSetRequest;
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			String userName = userResponse.getUserName();
			if (userName != null) {
				log.info("setmachinetimefenceparam: POST  params for machine is  {} user {}", tfSetRequest, userName);
				if (null != tfSetRequest.getVin() && null != tfSetRequest.getStartTime()
						&& null != tfSetRequest.getEndTime()) {
					machineService.setMachineTimeFence(tfSetRequest.getVin(), tfSetRequest.getStartTime(),
							tfSetRequest.getEndTime());
					log.info("Timefence parameters updated Successfully");
					return new ResponseEntity<ResponseData>(
							new ResponseData("Success", "Timefence parameters updated Successfully"), HttpStatus.OK);
				} else {
					throw new ProcessCustomError("Please provide valid value for timefence",
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("setTimeFencingForMachine: Issue faced while setting TimeFence parameters for machine ", e);
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}