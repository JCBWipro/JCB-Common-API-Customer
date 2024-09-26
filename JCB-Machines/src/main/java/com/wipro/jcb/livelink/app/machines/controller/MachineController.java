package com.wipro.jcb.livelink.app.machines.controller;

import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.jcb.livelink.app.machines.commonUtils.MachineUtils;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.config.AddAuthorization;
import com.wipro.jcb.livelink.app.machines.constants.ConstantConfig;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.MachineProfileService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponse;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponseV2;
import com.wipro.jcb.livelink.app.machines.service.response.MachineProfile;
import com.wipro.jcb.livelink.app.machines.service.response.MachineResponseV3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

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
    Utilities utilities;
    
    /*
     * This End Point is used for the testing Purpose
     */
    @GetMapping
    public String getString(@RequestHeader("LoggedInUserRole") String userDetails) {
    	UserDetails userResponse = MachineUtils.getUserDetails(userDetails);
        return "LoggedIn Role is:-" + userResponse.getRoleName() + " and UserName is:-" + userResponse.getUserName();
    }

    @CrossOrigin
    @Operation(summary = "List all machines for the current user", description = "Retrieves a paginated list of machines associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine List", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @AddAuthorization
    @Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT, readOnly = true)
    @GetMapping(value = "/machinesV2")
    public ResponseEntity<?> getMachinesVTwo(
            @Parameter(description = "Access token for authentication", required = true) @RequestHeader("accessToken") String token,
            @Parameter(description = "Page number (zero-based)") @RequestParam(value = "pageNumber", defaultValue = "0") String pageNumber,
            @Parameter(description = "Page size") @RequestParam(value = "pageSize", defaultValue = "${controller.customer.machines.pageSize}") String pageSize,
            @Parameter(description = "Filter criteria (comma-separated values, e.g., '3DX,Super,ecoXcellence')") @RequestParam(value = "filter", defaultValue = "optional") String filter,
            @Parameter(description = "Search term") @RequestParam(value = "search", defaultValue = "optional") String search,
            @Parameter(description = "Skip fetching reports (true/false)") @RequestParam(value = "skipReports", required = false) Boolean skipReports) {

        try {
            String userName = utilities.getUserNamebyAppToken(token);
            if (userName == null) {
                log.warn("Session expired for token: {}", token);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            log.info("machines: GET request from user {}", userName);
            MachineListResponseV2 response = machineResponseService.getMachineResponseListV2(userName, filter, search, skipReports, pageNumber, pageSize, token);
            return ResponseEntity.ok(response);

        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting machine list for Customer. Parameters: filter={}, search={}", filter, search, e);
            log.info("Exception occurred for MachinesV2 API.. Parameters: filter={}, search={}. Exception: {}", filter, search, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getApiMessages());
        } catch (final Exception e) {
            log.error("Issue faced while getting machine list for Customer. Parameters: filter={}, search={}", filter, search, e);
            log.info("Exception occurred for MachinesV2 API. Parameters: filter={}, search={}. Exception: {}", filter, search, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process request");
        }
    }

    @CrossOrigin
    @Operation(summary = "Get machine details for the current user", description = "Retrieves detailed information about a specific machine identified by its VIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Machine Details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MachineResponseV3.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @AddAuthorization
    @Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT, readOnly = true)
    @GetMapping(value = "/machinesdetailsV3")
    public ResponseEntity<MachineResponseV3> getMachinesDetailsVThree(
            @Parameter(description = "Access token for authentication", required = true) @RequestHeader("accessToken") String token,
            @Parameter(description = "Vehicle Identification Number (VIN) of the machine", required = true) @RequestParam("vin") String vin) {
        String userName = null;
        try {
            userName = utilities.getUserNamebyAppToken(token);
            if (userName != null) {
                log.info("Machines details V3: GET request from user {} vin {} ", userName, vin);
                return new ResponseEntity<>(machineResponseService.getMachineDetailsListV3(userName, vin), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting machine list for Customer and parameter will be ");
            log.info("Exception occurred for MachinesdetailsV3 API :-{}-Param-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (final Exception e) {
            log.error("Issue faced while getting machine list for Customer and input parameter will be ");
            log.info("Exception occurred for MachinesdetailsV3 API :{}-Param-{}Exception -{}", userName, vin, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @CrossOrigin
	@Transactional(readOnly = true)
    @GetMapping("/machineprofile")
	public ResponseEntity<?> getMachineProfile(@RequestHeader("LoggedInUserRole") String userDetails, @RequestParam(required=false) String vin) {
    	UserDetails userResponse = MachineUtils.getUserDetails(userDetails);
    	String userName = userResponse.getUserName();
    	try {
			if (userName != null) {
				log.info("machineprofile:GET Request from user {}", userName);
				return new ResponseEntity<MachineProfile>(machineProfileService.getMachineProfile(userName, vin),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error("machineprofile:GET Request failed for machineprofile with fields for " + vin);
			log.info("Exception occured for Machineprofile API :"+userName+"-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error("machineprofile:GET Request failed for machineprofile with fields for " + vin);
			log.info("Exception occured for Machineprofile API :"+userName+"-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
    
}