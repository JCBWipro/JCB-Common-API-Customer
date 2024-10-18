package com.wipro.jcb.livelink.app.alerts.controller;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertCommonUtils;
import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertUtilities;
import com.wipro.jcb.livelink.app.alerts.constants.MessagesList;
import com.wipro.jcb.livelink.app.alerts.dto.AlertResponse;
import com.wipro.jcb.livelink.app.alerts.dto.UserDetails;
import com.wipro.jcb.livelink.app.alerts.exception.ApiError;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.service.AlertInfoResponseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-10-2024
 */

@Slf4j
@RestController
@PropertySource("application.properties")
@Tag(name = "Customer", description = "Customer Machine API")
@RequestMapping(value = "/user/alerts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AlertController {

    @Value("${livelinkserver.loadAlertsDataForDays}")
    int loadAlertDataForDays;

    @Autowired
    AlertInfoResponseService alertInfoResponseService;

    @Autowired
    AlertUtilities alertUtilities;

    @GetMapping(value = "/alertsV2")
    @Operation(summary = "Get alerts information typed by alert type. Giving previous 7 days data by default.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts Information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))})
    public ResponseEntity<?> getAlertsVTwo(
            @Parameter(description = "User details from the token", required = true) @RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
            @Parameter(description = "Start date (YYYY-MM-DD) or 'serverDecided' for default") @RequestParam(value = "from", defaultValue = "serverDecided") String from,
            @Parameter(description = "End date (YYYY-MM-DD) or 'serverDecided' for default") @RequestParam(value = "to", defaultValue = "serverDecided") String to,
            @Parameter(description = "Page number (zero-based)") @RequestParam("pageNumber") int pageNumber,
            @Parameter(description = "Page size") @RequestParam(value = "pageSize", defaultValue = "${controller.customer.alerts.pageSize}") int pageSize,
            @Parameter(description = "Filter criteria (comma-separated values, e.g., '3DX,Super,ecoXcellence')") @RequestParam(value = "filter", defaultValue = "optional") String filter,
            @Parameter(description = "Search term") @RequestParam(value = "search", defaultValue = "optional") String search) {

        log.info("Received request for getAlertsVTwo with parameters: from={}, to={}, pageNumber={}, pageSize={}, filter={}, search={}",
                from, to, pageNumber, pageSize, filter, search);
        try {
            // Handle default date values
            if ("serverDecided".equals(from)) {
                from = alertUtilities.getStartDate(loadAlertDataForDays);
                log.debug("Start date set to default: {}", from);
            }
            if ("serverDecided".equals(to)) {
                to = alertUtilities.getEndDate(1);
                log.debug("End date set to default: {}", to);
            }

            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("Fetching alerts for user: {}", userName);

                // Fetch alerts data
                AlertResponse response = alertInfoResponseService.getAlerts(userName, from, to, pageNumber, pageSize, filter, search, true);

                log.info("Successfully retrieved alerts for user: {}", userName);
                return ResponseEntity.ok(response);
            } else {
                log.warn("Unauthorized request: No valid session present");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiError(HttpStatus.UNAUTHORIZED, "No valid session present", "Session expired", null));
            }
        } catch (final ProcessCustomError e) {
            log.error("Issue faced while getting alerts for Customer. Parameters: from={}, to={}, filter={}, search={}",
                    from, to, filter, search, e);
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiError(e.getStatus(), e.getMessage(), e.getErrorCode(), e.getDetails()));

        } catch (final IllegalArgumentException e) {
            log.warn("Invalid input parameters: from={}, to={}, filter={}, search={}. Error: {}",
                    from, to, filter, search, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiError(HttpStatus.BAD_REQUEST, "Invalid input parameters", "INVALID_INPUT", null));

        } catch (final Exception e) {
            log.error("Unexpected error fetching alerts: from={}, to={}, filter={}, search={}",
                    from, to, filter, search, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }
}