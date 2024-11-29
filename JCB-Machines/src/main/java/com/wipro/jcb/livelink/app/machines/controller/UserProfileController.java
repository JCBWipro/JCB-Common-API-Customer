package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.constants.ConstantConfig;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.ResponseData;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.request.GeofenceRequest;
import com.wipro.jcb.livelink.app.machines.request.TimefenceRequest;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.UserService;
import com.wipro.jcb.livelink.app.machines.service.response.GeofenceLandmarkResponse;
import com.wipro.jcb.livelink.app.machines.service.response.MachineDownQuestionResponse;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponse;
import com.wipro.jcb.livelink.app.machines.service.response.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/30/2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@RestController
@PropertySource("application.properties")
@Tag(name = "User", description = "User Authentication API")
@RequestMapping("/user/machines")
public class UserProfileController {
	
    @Autowired
    UserService userService;
    
    @Autowired
    MachineService machineService;
    
    @Autowired
    MachineResponseService machineResponseService;

    @Operation(description = "get user profile", summary = " user profile details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile fetching",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "User profile fetch failed",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping("/userprofile")
    public ResponseEntity<?> getUserProfile(
            @RequestHeader("LoggedInUserRole") String userDetails,
            @RequestParam(value = "v", defaultValue = "optional") String version) {
        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();

        try {
            long start = System.currentTimeMillis();

            log.info("userprofile:GET  Getting API request from user {}, version {} ", userName, version);
            if (userName != null) {
                log.info("the value of userName:{}", userName);
                final UserProfile userProfile = userService.getUserProfile(userName, version);
                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                log.info("Userprofile API Duration :{}-{}", elapsedTime, userName);
                return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);
            } else {
                log.info("Session Expired For User Profile");
                return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error("Exception in userprofile ");
            log.error("Error observed while fetching user profile " + e.getMessage());
            log.info("Exception occurred for profile API {} Error -{}", userName, e.getMessage());
            return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            log.error("getUserProfile:  Get request failed");
            log.info("Exception occurred for profile API :{} Error -{}", userName, e.getMessage());
            return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR, e.getMessage(),
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // UserProfile Mapping API
    @Operation(description = "User Mapping Request ", summary = "User Mapping Request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Config data successfully sent",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MachineDownQuestionResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized user",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Config request failed",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})

    @GetMapping("/userMapping")
    public @ResponseBody ResponseEntity<?> userMapping(
            @RequestParam("userName") String userName) {
        try {
            long start = System.currentTimeMillis();
            log.info("userMapping:GET  Getting API request from user {}", userName);
            if (userName != null) {
                final Boolean response = userService.getuserMapping(userName);
                log.info("userMapping:GET  end API Response {}", response);
                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                log.info("Vin Mapping Duration :{}", elapsedTime);
                return new ResponseEntity<UserProfile>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }


        } catch (final Exception e) {
            log.error("userMapping:Error observed while retrieving app config {}", e.getMessage());
            return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR, e.getMessage(),
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /*
	 * API to Set GeoFencing Parameter for machines
	 */
	@CrossOrigin
	@Operation(summary = "Setting GeoFencing Parameter for machines")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Geofencing information is updated for machinegeofence", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@PostMapping("/setgeofence")
	public ResponseEntity<?> setGeoFence(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestBody GeofenceRequest geofenceParam) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			String userName = userResponse.getUserName();
			if (userName != null) {
				log.info("setmachinegeofenceparam: POST Request for machine {} user {} ", geofenceParam.getVin(),
						userName);
				String validation = checkSetGeofenceValidation(geofenceParam);
				if (validation == null) {
					String message = machineService.setGeoFenceParam(geofenceParam, userName,
							geofenceParam.getMachineType(), "optional");
					if (message.equals(MessagesList.SUCCESS)) {
						return new ResponseEntity<ResponseData>(
								new ResponseData("Success", "Geofence Created Successfully"), HttpStatus.OK);
					} else if (message.equals(MessagesList.UPDATESUCCESS)) {
						return new ResponseEntity<ResponseData>(
								new ResponseData("Success", "Geofence Updated Successfully"), HttpStatus.OK);
					} else {
						return new ResponseEntity<ApiError>(
								new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed", message, null),
								HttpStatus.EXPECTATION_FAILED);
					}
				} else {
					return new ResponseEntity<ApiError>(
							new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed", validation, null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("setGeoFencing: Issue faced while setting geofence parameters for machine ", e);
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String checkSetGeofenceValidation(GeofenceRequest gfSetRequest) {
		String reason = null;
		if (gfSetRequest.getRadius() != null && gfSetRequest.getLatitude() != null
				&& gfSetRequest.getLongitude() != null && gfSetRequest.getIsArrival() != null
				&& gfSetRequest.getIsDepature() != null && gfSetRequest.getLandmarkName() != null) {

			if (gfSetRequest.getRadius() == null || gfSetRequest.getRadius().isNaN()) {
				reason = "Radius can't be null or empty";
			} else if ((gfSetRequest.getRadius()) < 0.5) {
				reason = "Please provide valid value greater than or equal to 0.5 for radius";
			}
			if (gfSetRequest.getLatitude() == null || gfSetRequest.getLatitude().isEmpty()) {
				reason = "Latitude can't be null or empty";
			}
			if (gfSetRequest.getLongitude() == null || gfSetRequest.getLongitude().isEmpty()) {
				reason = "Longitude can't be null or empty";
			}
			if (gfSetRequest.getIsArrival() == null || gfSetRequest.getIsArrival().isEmpty()) {
				reason = "IsArrival can't be null or empty";
			}
			if (gfSetRequest.getIsDepature() == null || gfSetRequest.getIsDepature().isEmpty()) {
				reason = "IsDepature can't be null or empty";
			}
			if (gfSetRequest.getLandmarkName() == null || gfSetRequest.getLandmarkName().isEmpty()) {
				reason = "LandmarkName can't be null or empty";
			}
		} else {
			reason = "Please provide valid value for geofence";
		}
		return reason;
	}
	
	/*
	 * API to Get Geofence Details
	 */
	@CrossOrigin
	@Operation(summary = "Get Geofence Details for machines")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Geofencing information is updated for machinegeofence", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@Transactional(readOnly = false)
	@GetMapping("/getgeofence")
	public ResponseEntity<?> getGeofenceDetils(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestParam("vin") String vin) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			String userName = userResponse.getUserName();
			log.info("getGeofenceDetils() Method Started " + vin + "-" + userName);
			if (userName != null) {
				return new ResponseEntity<GeofenceRequest>(machineService.getGeofenceDetails(vin, userName, "optional"),
						HttpStatus.OK);
			} else {
				log.info("Get Geofence : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}

		} catch (final Exception e) {
			log.error("Issue faced while get geofence");
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * API to Delete GeoFence Details
	 */
	@CrossOrigin
	@Operation(summary = "Delete GeoFence Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Geofence Deleted Successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@PostMapping("/deletegeofence")
	public ResponseEntity<?> deleteGeofence(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestBody GeofenceRequest geofenceParam) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			log.info("Delete Geofence Method Started for userName:{}", userName);
			if (userName != null) {
				if (geofenceParam.getLandmarkId() != null) {
					String response = machineService.deleteGeofenceDetails(geofenceParam, userName, "optional");
					if (response.equals(MessagesList.SUCCESS)) {
						return new ResponseEntity<ResponseData>(
								new ResponseData("Success", "Geofence Deleted Successfully"), HttpStatus.OK);
					} else {
						return new ResponseEntity<ApiError>(
								new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed", response, null),
								HttpStatus.EXPECTATION_FAILED);
					}
				} else {
					log.info("Validation Issue");
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"Landmark Id and VIN can't be null or empty", "Session expired", null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("Delete Geofence : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}

		} catch (final Exception e) {
			log.error("Issue faced while delete geofence");
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * API to Get Landmark Details
	 */
	@CrossOrigin
	@Operation(summary = "Get Landmark Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get Landmark Details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT, readOnly = false)
	@GetMapping("/getlandmarks")
	public ResponseEntity<?> getLandmarks(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestParam(value = "landmarkName", defaultValue = "optional") String landmarkName,
			@RequestParam("vin") String vin) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			log.info("Get Landmark Method Started " + userName);
			if (userName != null) {
				if (vin != null && !vin.isEmpty()) {
					return new ResponseEntity<GeofenceLandmarkResponse>(
							machineService.getLandmarkDetails(userName, landmarkName, "optional", vin), HttpStatus.OK);
				} else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"Please select vin number", "Please select vin number", null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("Get Timefence : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("Issue faced while get landmarks");
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * API to Set TimeFence Parameter for machines
	 */
	@CrossOrigin
	@Operation(summary = "Set Timefence Parameter for machines")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Set Timefence Parameter for machines", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@PostMapping("/settimefence")
	public ResponseEntity<?> setTimeFence(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestBody TimefenceRequest timefenceParam) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			if (userName != null) {
				log.info("setmachinetimefenceparam: POST Request for machine {} user {} ", timefenceParam.getVin(),
						userName);
				System.out.println("gfSetRequest" + timefenceParam);
				String machineType = machineResponseService.getMachinetype(timefenceParam.getVin());
				if (!machineType.equals(MessagesList.NO_FIRMWARE)) {
					String validation = checkTimefenceValidation(timefenceParam, machineType);
					log.info("validation " + validation);
					if (validation == null) {
						String message = machineService.setTimeFenceParam(timefenceParam, userName, machineType,
								"optional");
						if (message.equals(MessagesList.SUCCESS)) {
							return new ResponseEntity<ResponseData>(
									new ResponseData("Success", "Timefence Updated Successfully"), HttpStatus.OK);
						} else {
							return new ResponseEntity<ApiError>(
									new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed", message, null),
									HttpStatus.EXPECTATION_FAILED);
						}
					} else {
						return new ResponseEntity<ApiError>(
								new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed", validation, null),
								HttpStatus.EXPECTATION_FAILED);
					}
				} else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed",
							"No Firmware version for selected vin", null), HttpStatus.EXPECTATION_FAILED);
				}

			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("setTimeFencing: Issue faced while setting timefence parameters for machine ", e);
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String checkTimefenceValidation(TimefenceRequest timefenceParam, String machineType) {
		String reason = null;

		if (timefenceParam.getVin() == null || timefenceParam.getVin().equals("")) {
			reason = "VIN cannot be null or empty";
		}
		if (machineType.equals(MessagesList.LL4)) {

			if (timefenceParam.getMobileNumber() == null || timefenceParam.getMobileNumber().equals("")) {
				reason = "Mobile number can't be null or empty";
			} else {
				Pattern p = Pattern.compile("^\\d{10}$");
				Matcher m = p.matcher(timefenceParam.getMobileNumber());
				if (!m.matches()) {
					reason = "Please provide valid value for mobilnumber";
				}
			}

			if (timefenceParam.getNotificationPattern() == null || timefenceParam.getNotificationPattern().equals("")) {
				reason = "Notification can't be null or empty";
			}
			if (timefenceParam.getNotificationPattern().equalsIgnoreCase("OneTime")) {
				if (timefenceParam.getNotificationDate() == null || timefenceParam.getNotificationDate().equals(""))
					reason = "Notification date can't be null or empty";

			} else {
				if (timefenceParam.getRecurrence() == null || timefenceParam.getRecurrence().equals("")) {
					reason = "Recurrence pattern can't be null or empty";
				}
				if (timefenceParam.getRecurrenceStartDate() == null
						|| timefenceParam.getRecurrenceStartDate().equals("")) {
					reason = "Recurrence start date can't be null or empty";
				}
			}

			if (timefenceParam.getNotificationDetails() == null) {
				reason = "Please select notification details";
			}
		} else {

			if (timefenceParam.getOperatingStartTime() == null && timefenceParam.getOperatingStartTime().equals("")) {
				reason = "Operation start time can't be null or empty";
			}
			if (timefenceParam.getOperatingEndTime() == null && timefenceParam.getOperatingEndTime().equals("")) {
				reason = "Operation end time can't be null or empty";
			}
		}

		return reason;

	}
	
	/*
	 * API to Get TimeFence Details
	 */
	@CrossOrigin
	@Operation(summary = "Get Timefence Details")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Get Timefence Details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@Transactional(readOnly = false)
	@GetMapping("/gettimefence")
	public ResponseEntity<?> getTimefenceDetils(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestParam("vin") String vin) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			log.info("Get Timefence Method Started" + vin + "-" + userName);
			if (userName != null) {
				return new ResponseEntity<TimefenceRequest>(
						machineService.getTimefenceDetails(vin, userName, "optional"), HttpStatus.OK);
			} else {
				log.info("Get Timefence : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}

		} catch (final Exception e) {
			log.error("Issue faced while get timefence");
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * API to Delete TimeFence Details
	 */
	@CrossOrigin
	@Operation(summary = "Delete Timefence Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Timefence Details Deleted Successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@PostMapping("/deletetimefence")
	public ResponseEntity<?> deletetimefence(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestBody GeofenceRequest geofenceParam) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			log.info("Delete Geofence Method Started " + userName);
			if (userName != null) {
				if (geofenceParam.getVin() != null) {
					String response = machineService.deleteTimefenceDetails(geofenceParam.getVin(), userName, "optional");
					if (response.equals(MessagesList.SUCCESS)) {
						return new ResponseEntity<ResponseData>(
								new ResponseData("Success", "Timefence Deleted Successfully"), HttpStatus.OK);
					} else {
						return new ResponseEntity<ApiError>(
								new ApiError(HttpStatus.EXPECTATION_FAILED, "Failed", response, null),
								HttpStatus.EXPECTATION_FAILED);
					}
				} else {
					log.info("Validation Issue");
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"VIN can't be null or empty", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("Delete Geofence : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}

		} catch (final Exception e) {
			log.error("Issue faced while delete geofence");
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
