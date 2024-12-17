package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.constants.ConstantConfig;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerDistribution;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerMachinesV3;
import com.wipro.jcb.livelink.app.machines.dto.DealerDashboard;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.DealerCustomerMachinesResponseService;
import com.wipro.jcb.livelink.app.machines.service.DealerCustomerResponseService;
import com.wipro.jcb.livelink.app.machines.service.DealerDashboardResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineListResponse;

import io.swagger.annotations.ApiParam;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 *  DealerController is a REST controller that handles requests related to dealer machines.
 *  It provides endpoints to manage and retrieve dealer customer information
 */
@Slf4j
@RestController
@PropertySource("application.properties")
@Tag(name = "Dealer", description = "Dealer Machine API")
@RequestMapping(value = "/user/dealer", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DealerController {
	
    @Autowired
    DealerCustomerResponseService dealerCustomerResponseService;
    
    @Autowired
    DealerDashboardResponseService dealerDashboardResponseService;
    
    @Autowired
    DealerCustomerMachinesResponseService dealerCustomerMachinesResponseService;

    /**
     * Handles GET requests to retrieve all customers for a dealer.
     */
    @GetMapping(value = "/dashboard/customers")
    @Operation(summary = "Dealers All Customers", description = "Dealers All Customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dealers All Customers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDistribution.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })

    public ResponseEntity<?> getDealerAllCustomers(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
                                                   @RequestParam("pageNumber") String pageNumber,
                                                   @RequestParam(value = "pageSize", defaultValue = "${controller.dealer.home.pageSize}") String pageSize,
                                                   @RequestParam(value = "search", defaultValue = "optional") String search) {
        try {
            UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("dashboardCustomers: GET  request for user{}", userName);
                return new ResponseEntity<>(dealerCustomerResponseService.getDealerCustomers(userName,
                        search, Integer.parseInt(pageNumber), Integer.parseInt(pageSize)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting data for dealer home page");
            return new ResponseEntity<>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /*
     * API to Get Dealer Dashboard Distributor Details
     */
    @CrossOrigin
    @Operation(summary = "Get Dealer Dashboard Distributor Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dealer Dashboard Distributor Details",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Auth Failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "500", description = "Request failed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping("/dashboard/details")
	public ResponseEntity<?> getDashboardDetails(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestParam("pageNumber") String pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "${controller.dealer.home.pageSize}") String pageSize,
			@RequestParam(value = "distributor", defaultValue = "optional") String distributor,
			@RequestParam(value = "keyParam", defaultValue = "optional") String keyParam,
			@RequestParam(value = "search", defaultValue = "optional") String search) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
            final String userName = userResponse.getUserName();
			if (userName != null) {
                log.info("dashboardDetails: GET Request for user{}", userName);
				return new ResponseEntity<>(
                        dealerDashboardResponseService.getDealerDashboardDetails(userName, search, distributor,
                                keyParam, Integer.parseInt(pageNumber), Integer.parseInt(pageSize)), HttpStatus.OK);
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error("Issue faced while getting data for dealer home page");
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    /*
     * API to Get Dealer Dashboard Data
     */
    @GetMapping(value = "/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Dealer Dashboard page data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dealer dashboard data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DealerDashboard.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT, readOnly = true)
    public ResponseEntity<?> getDashboardData(@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
                                              @RequestParam(value = "search", defaultValue = "optional") String search,
                                              @RequestParam(value = "type", defaultValue = "optional") String type) {
        UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
        String userName = userResponse.getUserName();
        try {
            if (userName == null) {
                log.warn("Session expired for user details: {}", userDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            log.info("dashboard: GET Dealer dashboard for user {}", userName);
            DealerDashboard response = dealerDashboardResponseService.getDealerDashboardResponse(userName, search, type);
            return ResponseEntity.ok(response);

        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting data for dealer home page : {}", userName, e);
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiError(e.getStatus(), e.getMessage(), e.getErrorCode(), e.getDetails()));

        } catch (final Exception e) {
            log.error("Unexpected error fetching dealer dashboard data.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }
    
    /*
	 * API to Get Dealer dashboard All Machines
	 */
	@CrossOrigin
	@Operation(summary = "Dealer dashboard All Machines")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Dealer dashboard All Machines", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MachineListResponse.class)) }),
			@ApiResponse(responseCode = "401", description = "Auth Failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }),
			@ApiResponse(responseCode = "500", description = "Request failed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }) })
	@Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT, readOnly = true)
	@GetMapping("/dashboard/customer/machinesV3")
	public ResponseEntity<?> getCustomerMachinesVThreee(
			@RequestHeader(MessagesList.LOGGED_IN_USER_ROLE) String userDetails,
			@RequestParam("customerId") String customerId, @RequestParam("pageNumber") String pageNumber,
			@RequestParam("distributer") String distributer, @RequestParam("keyParam") String keyParam,
			@RequestParam("tabSeparator") String tabSeparator,
			@RequestParam(value = "pageSize", defaultValue = "${controller.dealer.serviceoverdue.pageSize}") String pageSize,
			@ApiParam(value = "3DX Super ecoXcellence") @RequestParam(value = "filter", defaultValue = "optional") String filter,
			@RequestParam(value = "search", defaultValue = "optional") String search,
			@RequestParam(value = "skipReports", required = false) Boolean skipReports) {
		try {
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			if (userName != null) {
				log.info("dashboardcustomersmachines: GET  request for user" + userName);
				return new ResponseEntity<CustomerMachinesV3>(
						dealerCustomerMachinesResponseService.getMachinesWithCustomerV3(userName, distributer, keyParam,
								tabSeparator, customerId, filter, search, skipReports, Integer.parseInt(pageNumber),
								Integer.parseInt(pageSize)),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error("Issue faced while getting data for dealer machines");
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
