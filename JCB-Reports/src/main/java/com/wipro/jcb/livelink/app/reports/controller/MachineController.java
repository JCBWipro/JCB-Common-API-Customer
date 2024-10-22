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

import com.wipro.jcb.livelink.app.reports.commonUtils.ReportCommonUtils;
import com.wipro.jcb.livelink.app.reports.commonUtils.ReportUtilities;
import com.wipro.jcb.livelink.app.reports.constants.MessagesConstantsList;
import com.wipro.jcb.livelink.app.reports.dto.UserDetails;
import com.wipro.jcb.livelink.app.reports.entity.Machine;
import com.wipro.jcb.livelink.app.reports.exception.ApiError;
import com.wipro.jcb.livelink.app.reports.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.reports.report.AdvanceReportsV2;
import com.wipro.jcb.livelink.app.reports.report.IntelliReportResponse;
import com.wipro.jcb.livelink.app.reports.report.ReportResponseV2;
import com.wipro.jcb.livelink.app.reports.report.VisualizationReportResponse;
import com.wipro.jcb.livelink.app.reports.service.AdvanceReportService;
import com.wipro.jcb.livelink.app.reports.service.CustomerReportService;
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
    private ReportUtilities utilities;
	
	@Autowired
    private MachineService machineService;
	
	@Autowired
	private MachineResponseService machineResponseService;
	
	@Autowired
	private CustomerReportService customerReportService;
	
	@Autowired
	private AdvanceReportService advanceReportService;
	
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
	public ResponseEntity<?> getMachineAdvanceReportV2(@RequestHeader(MessagesConstantsList.LoggedInUserRole) String userDetails,
			@RequestParam("vin") String vin) {
		String userName = null;
		try {
			log.info("getMachineAdvanceReportV2: machine advance report request received for machine: " + vin);
			UserDetails userResponse = ReportCommonUtils.getUserDetails(userDetails);
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
					MessagesConstantsList.ADVANCE_REPORT_ERROR, MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/*
	 * This End Point is to Get Reports for Current User
	 */
	@CrossOrigin
	@Operation(summary = "List Report for current user", description = "Report for current user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Report for current user"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@Transactional(readOnly = true)
	@GetMapping("/reportV2")
	public ResponseEntity<?> getReportV2(@RequestHeader(MessagesConstantsList.LoggedInUserRole) String userDetails,
			@RequestParam(value = "filter", defaultValue = "optional") String filter,
			@RequestParam(value = "startDate", defaultValue = "optional") String startDate,
			@RequestParam(value = "endDate", defaultValue = "optional") String endDate) {
		String userName = null;
		try {
			UserDetails userResponse = ReportCommonUtils.getUserDetails(userDetails);
			userName = userResponse.getUserName();
			if (userName != null) {
				log.info("report: GET request from user {}", userName);
				Machine machine = machineResponseService.getMachineDetails(filter, userName);
				if (machine != null) {
					return new ResponseEntity<ReportResponseV2>(
							customerReportService.getCustomerReportV2(userName, filter, startDate, endDate),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"Please select correct machine", "Please select correct machine", null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error(" report: Issue faced while getting report for Customer and inputs will be ", filter, startDate,
					endDate);
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error(" report: Issue faced while getting report for Customer and inputs will be ", filter, startDate,
					endDate);
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/*
	 * This End Point is to Get Visualization Machine Report
	 */
	@CrossOrigin
	@Operation(summary = "Visualization Report", description = "Visualization Report")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Visualization Report"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@Transactional(readOnly = true)
	@GetMapping("/getVisualizationReportsV2")
	public ResponseEntity<?> getMachineVisualizationReportV2(
			@RequestHeader(MessagesConstantsList.LoggedInUserRole) String userDetails, @RequestParam("vin") String vin,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		try {
			UserDetails userResponse = ReportCommonUtils.getUserDetails(userDetails);
			final String userName = userResponse.getUserName();
			log.info("getMachineVisualizationReportV2: machine intelli report request received for machine: " + vin);
			if (userName != null) {
				Date date = new Date();
				date.setDate(date.getDate() - 1);
				if (utilities.getDate(endDate).before(date) && utilities.getDate(startDate).before(date)) {
					if (utilities.getDate(startDate).before(utilities.getDate(endDate))
							|| utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
						Machine machine = machineResponseService.getMachineDetails(vin, userName);
						if (machine != null) {
							return new ResponseEntity<VisualizationReportResponse>(
									advanceReportService.getVisualizationReportV2(vin, startDate, endDate),
									HttpStatus.OK);
						} else {
							return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
									"Please select correct machine", "Please select correct machine", null),
									HttpStatus.EXPECTATION_FAILED);
						}
					} else {
						return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
								"Fromdate should be lessthan todate ", "Fromdate should be lessthan todate", null),
								HttpStatus.EXPECTATION_FAILED);
					}

				} else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"Date should be lessthan current date", "Date should be lessthan current date", null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("getMachineVisualizationReportV2 : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error("getMachineVisualizationReportV2 Report data failed: " + e.getMessage());
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error("getMachineVisualizationReportV2 Report data failed: " + e.getMessage());
			return new ResponseEntity<ApiError>(
					new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR, MessagesConstantsList.ADVANCE_REPORT_ERROR,
							MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This End Point is to Get Advance Reports V3 Data
	 */
	@CrossOrigin
	@Operation(summary = "Get AdvanceReports", description = "AdvanceReports")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "AdvanceReports"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@Transactional(readOnly = true)
	@GetMapping("/getAdvanceReportsV3")
	public ResponseEntity<?> getMachineAdvanceReportV3(@RequestHeader(MessagesConstantsList.LoggedInUserRole) String userDetails,
			@RequestParam("vin") String vin) {
		String userName =null;
		try {
			log.info("getMachineAdvanceReportV2: machine advance report request received for machine: " + vin);
			UserDetails userResponse = ReportCommonUtils.getUserDetails(userDetails);
			userName = userResponse.getUserName();
			if (userName != null) {
				Machine machine = machineResponseService.getMachineDetails(vin, userName);
				if(machine!=null) {
					final Date startDate = utilities.getDate(utilities.getStartDate(loadAdvanceReportDataForDays));
					final Date endDate = utilities.getDate(utilities.getStartDate(1));
					final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
					format.setTimeZone(TimeZone.getTimeZone(timezone));
					
					return new ResponseEntity<AdvanceReportsV2>(
							new AdvanceReportsV2(vin, format.format(startDate) + " - " + format.format(endDate),
									machineService.getReportInstanceV3(vin, startDate, endDate),
									machineService.getIntelliReportV3(vin, startDate, endDate),
									machineService.loadIntelliDigReport(vin, startDate, endDate)),
							HttpStatus.OK);
					
				}else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Please select correct machine","Please select correct machine", null), HttpStatus.EXPECTATION_FAILED);
				}
				
			} else {
				log.info("getMachineAdvanceReportV2: No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final Exception e) {
			log.error("getMachineAdvanceReportV2: Get Machine Advance Report data failed: " + e.getMessage());
			log.info("Exception occured for AdvancedreportV3 API :"+userName+"-Param-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesConstantsList.ADVANCE_REPORT_ERROR, MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This End Point is to Get Visualization Reports V3 Data
	 */
	@CrossOrigin
	@Operation(summary = "Get Visualization Reports", description = "Visualization Reports")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Visualization Reports"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@Transactional(readOnly = true)
	@GetMapping("/getVisualizationReportsV3")
	public ResponseEntity<?> getMachineVisualizationReportV3(@RequestHeader(MessagesConstantsList.LoggedInUserRole) String userDetails,
			@RequestParam("vin") String vin, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) {
		String userName = null;
		try {
			log.info("getMachineVisualizationReportV3: machine intelli report request received for machine: " + vin);
			UserDetails userResponse = ReportCommonUtils.getUserDetails(userDetails);
			userName = userResponse.getUserName();
			if (userName != null) {
				Date date= new Date();
				date.setDate(date.getDate()-1);
				if(utilities.getDate(endDate).before(date)  && utilities.getDate(startDate).before(date)) {
					if(utilities.getDate(startDate).before(utilities.getDate(endDate)) || utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
						Machine machine = machineResponseService.getMachineDetails(vin, userName);
						if(machine!=null) {
							return new ResponseEntity<VisualizationReportResponse>(
									advanceReportService.getVisualizationReportV3(vin, startDate, endDate), HttpStatus.OK);
						}else {
							return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Please select correct machine","Please select correct machine", null), HttpStatus.EXPECTATION_FAILED);
						}
					}else {
						return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Fromdate should be lessthan todate ","Fromdate should be lessthan todate", null), HttpStatus.EXPECTATION_FAILED);
					}
				
				}else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED, "Date should be lessthan current date","Date should be lessthan current date", null), HttpStatus.EXPECTATION_FAILED);
				}
				
			} else {
				log.info("getMachineVisualizationReportV3 : No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error("getMachineVisualizationReportV3 Report data failed: " + e.getMessage());
			log.info("Exception occured for VisualizationReportsV2 API :"+userName+"-Param-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error("getMachineVisualizationReportV3 Report data failed: " + e.getMessage());
			log.info("Exception occured for VisualizationReportsV2 API :"+userName+"-Param-"+vin+"Exception -"+e.getMessage());
			return new ResponseEntity<ApiError>(new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR,
					MessagesConstantsList.ADVANCE_REPORT_ERROR, MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This End Point is to Get Intelli Reports V3 Data
	 */
	@CrossOrigin
	@Operation(summary = "Get Intelli Reports", description = "Intelli Reports")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Intelli Reports"),
			@ApiResponse(responseCode = "401", description = "Auth Failed"),
			@ApiResponse(responseCode = "500", description = "Request failed") })
	@Transactional(readOnly = true)
	@GetMapping("/getIntelliReportsV3")
	public ResponseEntity<?> getMachineIntelliReportV3(
			@RequestHeader(MessagesConstantsList.LoggedInUserRole) String userDetails, @RequestParam("vin") String vin,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		String userName = null;
		try {
			log.info("getMachineIntelliReportV3: machine intelli report request received for machine: " + vin);
			UserDetails userResponse = ReportCommonUtils.getUserDetails(userDetails);
			userName = userResponse.getUserName();
			if (userName != null) {
				Date date = new Date();
				date.setDate(date.getDate() - 1);
				if (utilities.getDate(endDate).before(date) && utilities.getDate(startDate).before(date)) {
					if (utilities.getDate(startDate).before(utilities.getDate(endDate))
							|| utilities.getDate(startDate).equals(utilities.getDate(endDate))) {
						Machine machine = machineResponseService.getMachineDetails(vin, userName);
						if (machine != null) {
							return new ResponseEntity<IntelliReportResponse>(
									advanceReportService.getIntelliReportV3(vin, startDate, endDate), HttpStatus.OK);
						} else {
							return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
									"Please select correct machine", "Please select correct machine", null),
									HttpStatus.EXPECTATION_FAILED);
						}
					} else {
						return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
								"Fromdate should be lessthan todate ", "Fromdate should be lessthan todate", null),
								HttpStatus.EXPECTATION_FAILED);
					}
				} else {
					return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
							"Date should be lessthan current date", "Date should be lessthan current date", null),
							HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("getMachineIntelliReportV3: No Vallid session present");
				return new ResponseEntity<ApiError>(new ApiError(HttpStatus.EXPECTATION_FAILED,
						"No valid session present", "Session expired", null), HttpStatus.EXPECTATION_FAILED);
			}
		} catch (final ProcessCustomError e) {
			log.error("getMachineIntelliReportV3 Report data failed: " + e.getMessage());
			return new ResponseEntity<ApiError>(e.getApiMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final Exception e) {
			log.error("getMachineIntelliReportV3 Report data failed: " + e.getMessage());
			return new ResponseEntity<ApiError>(
					new ApiError(HttpURLConnection.HTTP_INTERNAL_ERROR, MessagesConstantsList.ADVANCE_REPORT_ERROR,
							MessagesConstantsList.APP_REQUEST_PROCESSING_FAILED, null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
