package com.wipro.jcb.livelink.app.alerts.controller;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertCommonUtils;
import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertUtilities;
import com.wipro.jcb.livelink.app.alerts.constants.MessagesList;
import com.wipro.jcb.livelink.app.alerts.dto.*;
import com.wipro.jcb.livelink.app.alerts.exception.ApiError;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.alerts.service.AlertInfoResponseService;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertObject;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.Date;

import static com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants.loadHistoricalDataForDays;

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

    @Value("${livelinkserver.loadAdvanceReportDataForDays}")
    int loadAdvanceReportDataForDays;

    @Autowired
    AlertInfoResponseService alertInfoResponseService;

    @Autowired
    AlertUtilities alertUtilities;

    @Autowired
    AdvanceReportService advanceReportService;


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

    @GetMapping(value = "/alertinfo")
    @Operation(summary = "Alert info for a particular alertId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert Info",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertObject.class))),
            @ApiResponse(responseCode = "401", description = "Auth Failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
    public ResponseEntity<?> getAlertInfo(
            @RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
            @RequestParam String id,
            @RequestParam String vin,
            @RequestParam(value = "from", defaultValue = "serverDecided") String from,
            @RequestParam(value = "to", defaultValue = "serverDecided") String to) {

        log.info("alertInfo:GET alertInfo data for id {}", id);

        try {
            if ("serverDecided".equals(from)) {
                from = alertUtilities.getStartDate(loadHistoricalDataForDays);
                log.debug("Start Date set to default: {}", from);
            }
            if ("serverDecided".equals(to)) {
                to = alertUtilities.getEndDate(1);
                log.debug("End Date set to default: {}", to);
            }
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();

            if (userName != null) {
                return new ResponseEntity<>(
                        alertInfoResponseService.getAlertInfoObj(userName, id, vin, from, to), HttpStatus.OK);
            } else {
                log.warn("Unauthorized request: No valid Session present");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiError(HttpStatus.UNAUTHORIZED, "No valid session present", "Session expired", null));
            }

        } catch (final ProcessCustomError e) {
            log.error("No Alert or machine exist with given id {}", id, e);
            return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                    MessagesList.APP_REQUEST_PROCESSING_ALERT, MessagesList.APP_REQUEST_PROCESSING_ALERT, null),
                    HttpStatus.EXPECTATION_FAILED);

        } catch (final Exception e) {
            log.error("Issue faced while getting alertInfo for id {} ", id, e);
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of service alerts for the machines of the current user.
     */
    @Operation(summary = "List all service alerts for machines of current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Service Alerts",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceAlertList.class))),
            @ApiResponse(responseCode = "401", description = "Auth Failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))) })
    @GetMapping( value = "/servicealertsV2", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getServiceAlertsVTwo(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                  @RequestParam(value = "from", defaultValue = "serverdecided") String from,
                                                  @RequestParam(value = "to", defaultValue = "serverdecided") String to,
                                                  @RequestParam("pageNumber") String pageNumber,
                                                  @RequestParam(value = "pageSize", defaultValue = "${controller.customer.servicealerts.pageSize}") String pageSize,
                                                  @Parameter(description ="3DX Super ecoXcellence,2DX Super ecoXcellence") @RequestParam(value = "filter", defaultValue = "optional") String filter,
                                                  @RequestParam(value = "search", defaultValue = "optional") String search) {
        try {
            if ("serverdecided".equals(from)) {
                from = alertUtilities.getStartDate(loadAlertDataForDays);
            }
            if ("serverdecided".equals(to)) {
                to = alertUtilities.getEndDate(1);
            }
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName != null) {
                log.info("servicealerts: GET Request for user {}", userName);
                return new ResponseEntity<>(alertInfoResponseService.getServiceAlertsList(userName, from,
                        to, Integer.parseInt(pageNumber), Integer.parseInt(pageSize), filter, search, true),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final Exception e) {
            log.error("servicealerts: Failed GET all servicealerts for Customer");
            e.getMessage();
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/notify")
    @Operation(summary = "Enable/Disable notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enable/Disable notification status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiOK.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> notify(
            @Parameter(description = "User details from the token", required = true) @RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
            @RequestBody Boolean enable) {

        try {
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            if (userName == null) {
                log.warn("notify: Username not found in user details.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user details"));
            }

            log.info("notify: PUT Request from user: {}", userName);
            Boolean updatedStatus = alertUtilities.enableDisableNotification(userName, enable);

            String res = updatedStatus ? "Notification has been enabled" : "Notification has been disabled";
            log.info(res);
            return ResponseEntity.ok(new ApiOK(res));

        } catch (final ProcessCustomError e) {
            log.error("Enable/Disable notification failed for user: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiError(e.getStatus(), e.getMessage(), e.getErrorCode(), e.getDetails()));

        } catch (final Exception e) {
            log.error("Enable/Disable notification failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

    @GetMapping(value = "/getNotificationList", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get Notification Report Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification Report",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationListResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @Transactional(readOnly = true)
    public ResponseEntity<?> getNotificationList(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                 @RequestParam("pageNumber") String pageNumber,
                                                 @RequestParam(value = "pageSize", defaultValue = "${controller.customer.machines.pageSize}") String pageSize) {

        try {
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();

            if (userName == null) {
                log.warn("notify: Username not found in user details..");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
            }

            final Date startDate = alertUtilities.getDate(alertUtilities.getStartDate(loadAdvanceReportDataForDays));
            final Date endDate = alertUtilities.getDate(alertUtilities.getStartDate(0));

            NotificationListResponseDto response = advanceReportService.getNotificationListByGroupingDate(userName, startDate, endDate, pageNumber, pageSize);
            return ResponseEntity.ok(response);

        } catch (final Exception e) {
            log.error("Unexpected error fetching Notification Report data.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

}