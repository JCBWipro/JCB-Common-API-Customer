package com.wipro.jcb.livelink.app.machines.controller;

import com.wipro.jcb.livelink.app.machines.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.CustomerDistribution;
import com.wipro.jcb.livelink.app.machines.dto.UserDetails;
import com.wipro.jcb.livelink.app.machines.exception.ApiError;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.DealerCustomerResponseService;
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
@RequestMapping(value = "/user/machines", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DealerController {
    @Autowired
    DealerCustomerResponseService dealerCustomerResponseService;

    /**
     * Handles GET requests to retrieve all customers for a dealer.
     */
    @GetMapping(value = "/dealers/dashboard/customers")
    @Operation(summary = "Dealers All Customers", description = "Dealers All Customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dealers All Customers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDistribution.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })

    public ResponseEntity<?> getDealerAllCustomers(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
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
}
