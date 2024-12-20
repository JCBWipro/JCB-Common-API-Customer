package com.wipro.jcb.livelink.app.alerts.controller;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertCommonUtils;
import com.wipro.jcb.livelink.app.alerts.commonUtils.AlertUtilities;
import com.wipro.jcb.livelink.app.alerts.config.ConstantConfig;
import com.wipro.jcb.livelink.app.alerts.constants.MessagesList;
import com.wipro.jcb.livelink.app.alerts.constants.NotificationsConstants;
import com.wipro.jcb.livelink.app.alerts.dto.*;
import com.wipro.jcb.livelink.app.alerts.exception.ApiError;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.repo.UserNotificationDetailRepo;
import com.wipro.jcb.livelink.app.alerts.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.alerts.service.AlertInfoResponseService;
import com.wipro.jcb.livelink.app.alerts.service.EmailService;
import com.wipro.jcb.livelink.app.alerts.service.response.AlertObject;
import com.wipro.jcb.livelink.app.alerts.service.response.Data;
import com.wipro.jcb.livelink.app.alerts.service.response.MessageContent;
import com.wipro.jcb.livelink.app.alerts.service.response.Notification;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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

    @Autowired
    UserNotificationDetailRepo userNotificationDetailRepo;

    @Autowired
    EmailService emailService;


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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))})
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))})
    @GetMapping(value = "/servicealertsV2", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getServiceAlertsVTwo(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                  @RequestParam(value = "from", defaultValue = "serverdecided") String from,
                                                  @RequestParam(value = "to", defaultValue = "serverdecided") String to,
                                                  @RequestParam("pageNumber") String pageNumber,
                                                  @RequestParam(value = "pageSize", defaultValue = "${controller.customer.servicealerts.pageSize}") String pageSize,
                                                  @Parameter(description = "3DX Super ecoXcellence,2DX Super ecoXcellence") @RequestParam(value = "filter", defaultValue = "optional") String filter,
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

    //UnRead Notification Count
    @Operation(summary = "Notification Count Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaion Count Report", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiOK.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/notificationcount", produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> notificationCount(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails) {
        String userName = null;
        try {
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            userName = userResponse.getUserName();
            log.info("Unread notification count for the user: {}", userName);
            if (userName == null) {
                log.info("Remove notification : No Valid session present");
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
            return new ResponseEntity<>(advanceReportService.unReadNotificationCount(userName), HttpStatus.OK);

        } catch (final Exception e) {
            log.error("Failed to Unread notification count : {}", e.getMessage());
            log.info("Exception occurred for Notification count API :{}Exception -{}", userName, e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.ADVANCE_REPORT_ERROR, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete Notification
    @Operation(summary = "Delete Notification Report Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete Notification Report", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiOK.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(value = "/deletenotification", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteNotification(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                @RequestParam(name = "id", defaultValue = "-1") int notificationId,
                                                @RequestParam(name = "notificationType") String type) {
        try {
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            Integer id = notificationId;
            log.info("Requested Delete notification for the User:{} , Notification Id : {}", userName, id);
            if (userName == null) {
                log.info("Delete notification : No Valid session present");
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }
            if (type.equals("ALL")) {
                return new ResponseEntity<>(advanceReportService.deleteAllNotification(userName), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(advanceReportService.deleteNotification(id, userName), HttpStatus.OK);
            }

        } catch (final Exception e) {
            log.error("Failed to Delete notification : {}", e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.ADVANCE_REPORT_ERROR, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/readnotification", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Remove Notification Report Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification Removed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationRemovedResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))})
    public ResponseEntity<?> removeNotification(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
                                                @RequestParam(name = "alertId", defaultValue = "-1") int alertId,
                                                @RequestParam(name = "notificationType") String type) {
        try {
            log.debug("Received request to remove notification. User details: {}, alertId: {}, type: {}",
                    userDetails, alertId, type);

            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();

            if (userName == null) {
                log.warn("removeNotification: Username not found in user details: {}", userDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiError(HttpStatus.UNAUTHORIZED, "Invalid user details"));
            }

            NotificationRemovedResponse response;
            if (type.equals("ALL")) {
                log.info("Removing all notifications for user: {}", userName);
                response = advanceReportService.readAllNotification(userName);
                log.debug("Response after removing all notifications: {}", response.getMessage());
            } else {
                log.info("Removing notification with ID {} for user: {}", alertId, userName);
                response = advanceReportService.readNotification(alertId, userName);
                log.debug("Response after removing notification with ID {}: {}", alertId, response.getMessage());
            }

            return ResponseEntity.ok(response);

        } catch (final Exception e) {
            log.error("Failed to remove notification. User details: {}, notificationId: {}, type: {}",
                    userDetails, alertId, type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process request", "SERVER_ERROR", null));
        }
    }

    @GetMapping(value = "/pushnotification", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Send Push Notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Push notification sent successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Auth Failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Request failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @Transactional(timeout = ConstantConfig.REQUEST_TIMEOUT)
    public ResponseEntity<?> breakFastNotification(
            @RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
            @RequestParam(value = "ostype") String osType,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "minusDays", required = false) Optional<Integer> minusDays,
            @RequestParam(value = "pushToken", defaultValue = "optional") String pushToken) {

        log.info("Send Push Notification Request: OsType={}, type={}, minusDays={}, pushToken={}", osType, type, minusDays, pushToken);

        try {
            UserDetails userResponse = AlertCommonUtils.getUserDetails(userDetails);
            String userName = userResponse.getUserName();
            String yesterday = alertUtilities.getDDMMYY(1);
            log.info("START :: sendBreakfastUpdateNotification {}", type);

            if (userName == null) {
                return new ResponseEntity<>(new ApiError(HttpStatus.EXPECTATION_FAILED,
                        "No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
            }


            if ("optional".equals(pushToken)) {
                //pushToken = "cr3VYqumSHG7rLtzGp7ytb:APssA91b12GJ65FHlL2_eRjfAj7F76yAuHv6Qstb5XZNiNovaw7N9tkpfDLLOWY7derw5vRI1aLEQefXQfwzFa8m7CuWU1_iTZeGkPWk7a5rZMH15ym18i4Ct41231231Oc4ievLdHba1neelS11BDcxP8H";
                pushToken = "123456yhntgb789";
            }

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            dateformat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            List<String> users = osType.equalsIgnoreCase("Android")
                    ? userNotificationDetailRepo.getAndroidFCMKeyForSalesforceUniqueUser(userName, userDetails)
                    : userNotificationDetailRepo.getIosFCMKeyForSalesforce(userName, userDetails);


            switch (type) {
                case "Alert" -> {
                    log.info("Alert Push Notification From Firebase - Start");
                    MessageContent pushNotification = new MessageContent();
                    pushNotification.setData(new Data("Alert Description", "Alert Event Type", "Alert", "New Alert Event Type Alert"));
                    pushNotification.setNotification(new Notification("Alert", "Alert Event Type"));

                    for (String firebaseToken : users) {

                        pushNotification.setToken(firebaseToken);
                        alertUtilities.sendNotificationUsingV1(pushNotification);
                    }

                    log.info("Alert Push Notification From Firebase - End");
                }
                case "Breakfast" -> {
                    log.info("notification list : {}", users.size());
                    log.info("Breakfast Push Notification From Firebase - Start");
                    MessageContent pushNotification = new MessageContent();
                    pushNotification.setData(new Data(NotificationsConstants.BREAKFAST_ALERT_DESCRIPTION + yesterday,
                            NotificationsConstants.BREAKFAST_ALERT_TYPE, NotificationsConstants.BREAKFAST_ALERT_KEY, NotificationsConstants.BREAKFAST_ALERT_TITLE));
                    pushNotification.setNotification(new Notification(NotificationsConstants.BREAKFAST_ALERT_NOTIFICATION_TITLE + yesterday, NotificationsConstants.BREAKFAST_ALERT_NOTIFICATION_BODY));

                    for (String firebaseToken : users) {

                        pushNotification.setToken(firebaseToken);
                        alertUtilities.sendNotificationUsingV1(pushNotification);
                    }

                    log.info("Breakfast Push Notification From Firebase - End");
                }
                case "Salesforce" -> {
                    log.info("notification list :{}", users.size());
                    log.info("Salesforce Push Notification From Firebase - Start");
                    MessageContent pushNotification = new MessageContent();
                    pushNotification.setData(new Data(NotificationsConstants.SALESFORCE_ALERT_DESCRIPTION,
                            NotificationsConstants.SALESFORCE_ALERT_TYPE, NotificationsConstants.SALESFORCE_ALERT_KEY, NotificationsConstants.SALESFORCE_ALERT_TITLE));
                    pushNotification.setNotification(new Notification(NotificationsConstants.SALESFORCE_ALERT_NOTIFICATION_TITLE, NotificationsConstants.SALESFORCE_ALERT_NOTIFICATION_BODY));

                    for (String firebaseToken : users) {

                        pushNotification.setToken(firebaseToken);
                        alertUtilities.sendNotificationUsingV1(pushNotification);
                    }
                    log.info("Salesforce Push Notification From Firebase - End");
                }
                case "HighbandBHL" -> {
                    log.info("HighbandBHL Push Notification From Firebase - Start");
                    MessageContent pushNotification = new MessageContent();
                    pushNotification.setData(new Data(NotificationsConstants.HIGHBANDBHL_ALERT_DESCRIPTION, NotificationsConstants.HIGHBANDBHL_ALERT_TYPE, NotificationsConstants.HIGHBANDBHL_ALERT_KEY, NotificationsConstants.HIGHBANDBHL_ALERT_TITLE));
                    pushNotification.setNotification(new Notification(NotificationsConstants.HIGHBANDBHL_ALERT_NOTIFICATION_TITLE, NotificationsConstants.HIGHBANDBHL_ALERT_NOTIFICATION_BODY));

                    for (String firebaseToken : users) {

                        pushNotification.setToken(firebaseToken);
                        alertUtilities.sendNotificationUsingV1(pushNotification);
                    }
                    log.info("HighbandBHL Push Notification From Firebase - End");
                }
                case "HighbandExcavator" -> {
                    log.info("UserList size is: {}" , users.size());
                    log.info("HighbandExcavator Push Notification From Firebase - Start");
                    MessageContent pushNotification = new MessageContent();
                    pushNotification.setData(new Data(NotificationsConstants.HIGHBANDEXCAVATOR_ALERT_DESCRIPTION, NotificationsConstants.HIGHBANDEXCAVATOR_ALERT_TYPE, NotificationsConstants.HIGHBANDEXCAVATOR_ALERT_KEY, NotificationsConstants.HIGHBANDEXCAVATOR_ALERT_TITLE));
                    pushNotification.setNotification(new Notification(NotificationsConstants.HIGHBANDEXCAVATOR_ALERT_NOTIFICATION_TITLE, NotificationsConstants.HIGHBANDEXCAVATOR_ALERT_NOTIFICATION_BODY));

                    for (String firebaseToken : users) {

                        pushNotification.setToken(firebaseToken);
                        alertUtilities.sendNotificationUsingV1(pushNotification);
                    }
                    log.info("HighbandExcavator Push Notification From Firebase - End");
                }
                case "HighIdling" -> {
                    log.info("UserList size {}", users.size());
                    log.info("HighIdling Push Notification From Firebase - Start");
                    MessageContent pushNotification = new MessageContent();
                    pushNotification.setData(new Data(NotificationsConstants.HIGHIDLING_ALERT_DESCRIPTION, NotificationsConstants.HIGHIDLING_ALERT_TYPE, NotificationsConstants.HIGHIDLING_ALERT_KEY, NotificationsConstants.HIGHIDLING_ALERT_TITLE));
                    pushNotification.setNotification(new Notification(NotificationsConstants.HIGHIDLING_ALERT_NOTIFICATION_TITLE, NotificationsConstants.HIGHIDLING_ALERT_NOTIFICATION_BODY));

                    for (String firebaseToken : users) {

                        pushNotification.setToken(firebaseToken);
                        alertUtilities.sendNotificationUsingV1(pushNotification);
                    }
                    log.info("HighIdling Push Notification From Firebase - End");
                }
                case "Email" -> emailService.sentRetryMail("Machine Scheduler", "Return Empty Data", 0, 0);
                default -> {
                    return new ResponseEntity<>("Please give correct type", HttpStatus.OK);
                }
            }
            return ResponseEntity.ok("Push notification sent successfully");

        } catch (final Exception e) {
            log.error("Issue observed while testAndroidSalesforceNotification {}", e.getMessage());
            return new ResponseEntity<>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessagesList.ANDROID_PUSH_ERROR, MessagesList.ANDROID_PUSH_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}