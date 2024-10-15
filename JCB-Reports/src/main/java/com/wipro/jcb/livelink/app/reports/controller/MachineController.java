package com.wipro.jcb.livelink.app.reports.controller;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.wipro.jcb.livelink.app.reports.commonUtils.AuthCommonUtils;
import com.wipro.jcb.livelink.app.reports.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.reports.constants.MessagesList;
import com.wipro.jcb.livelink.app.reports.dto.UserDetails;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.exception.ApiError;
import com.wipro.jcb.livelink.app.reports.report.AdvanceReportsV2;
import com.wipro.jcb.livelink.app.reports.service.MachineResponseService;
import com.wipro.jcb.livelink.app.reports.service.MachineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@PropertySource("application.properties")
@Tag(name = "Customer", description = "Customer Machine API")
@RequestMapping(value = "/user/reports", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MachineController {
	
	@Value("${livelinkserver.loadAdvanceReportDataForDays}")
	private int loadAdvanceReportDataForDays;
	
	@Value("${custom.formatter.timezone}")
	private String timezone;
	
	@Autowired
    Utilities utilities;
	
	@Autowired
    MachineService machineService;
	
	 @Autowired
	 MachineResponseService machineResponseService;
	
	/*
	 * This End Point is to Get Machine Reports Data
	 */
	@CrossOrigin
	@Operation(summary = "Machine Reports Data", description = "Machine Reports Data")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Machine Reports Data"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@Transactional(readOnly = true)
	@GetMapping("/getAdvanceReportsV2")
	public ResponseEntity<?> getMachineAdvanceReportV2(@RequestHeader(MessagesList.LoggedInUserRole) String userDetails,
			@RequestParam("vin") String vin) {
		String userName = null;
		try {
			log.info("getMachineAdvanceReportV2: machine advance report request received for machine: " + vin);
			UserDetails userResponse = AuthCommonUtils.getUserDetails(userDetails);
			userName = userResponse.getUserName();
			if (userName != null) {
				Machine machine = machineResponseService.getMachineDetails(vin, userName);
				if (machine != null) {
					final Date startDate = utilities.getDate(utilities.getStartDate(loadAdvanceReportDataForDays));
					final Date endDate = utilities.getDate(utilities.getStartDate(1));
					final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
					format.setTimeZone(TimeZone.getTimeZone(timezone));
					return new ResponseEntity<AdvanceReportsV2>(
							new AdvanceReportsV2(vin, format.format(startDate) + " - " + format.format(endDate),
									machineService.getReportInstanceV2(vin, startDate, endDate),
									machineService.getIntelliReportV2(vin, startDate, endDate),
									machineService.loadIntelliDigReport(vin, startDate, endDate)),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"Please select correct machine", "Please select correct machine", null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("getMachineAdvanceReportV2: No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("getMachineAdvanceReportV2: Get Machine Advance Report data failed: " + e.getMessage());
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesList.ADVANCE_REPORT_ERROR, MessagesList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
