package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.ResponseData;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.request.GeofenceRequest;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.UserService;
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
}
