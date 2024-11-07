package com.wipro.jcb.livelink.app.machines.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.constants.FuelLevelNAConstant;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.constants.UserType;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerInfo;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerMachinesV3;
import com.wipro.jcb.livelink.app.machines.dto.EngineHistoryDataListV2;
import com.wipro.jcb.livelink.app.machines.dto.FuelHistoryDataListV2;
import com.wipro.jcb.livelink.app.machines.dto.MachineDetailResponse;
import com.wipro.jcb.livelink.app.machines.entity.Customer;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineEnginestatusHistory;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedLocation;
import com.wipro.jcb.livelink.app.machines.entity.MachineFeedParserData;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.enums.LivelinkRenawal;
import com.wipro.jcb.livelink.app.machines.enums.ServiceCategory;
import com.wipro.jcb.livelink.app.machines.enums.ServiceStatus;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.CustomerRepository;
import com.wipro.jcb.livelink.app.machines.repo.DealerRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineEngineStatusHistoryDataRepo;
import com.wipro.jcb.livelink.app.machines.repo.MachineFeedLocationRepo;
import com.wipro.jcb.livelink.app.machines.repo.MachineFeedParserDataRepo;
import com.wipro.jcb.livelink.app.machines.repo.MachineFuelHistoryDataRepo;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineUtilizationDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.UserRepository;
import com.wipro.jcb.livelink.app.machines.service.DealerCustomerMachinesResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineDetailResponseService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineResponseListV3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealerCustomerMachinesResponseServiceImpl implements DealerCustomerMachinesResponseService {
	
	@Value("${custom.formatter.timezone}")
	private String timezone;
	
	@Value("${livelinkserver.loadHistoricalDataForDays}")
	private int loadHistoricalDataForDays;
	
	@Value("${livelinkserver.loadAlertsDataForDays}")
	private int loadAlertsDataForDays;
	
	@Value("${machine.approachingservicedays}")
	private int serviceDueMaxDays;
	
	@Value("${machine.servicedueminhours}")
	private Double serviceDueMinHours;
	
	@Value("${machine.lesser.used.max.range}")
	private int lesserUsedMaxRange;
	
	@Value("${machine.moderate.used.max.range}")
	private int moderateUsedMaxRange;
	
	@Value("${machine.havily.used.max.range}")
	private int heavilyUsedMaxRange;
	
	@Value("${machine.utilization.day}")
	private int machineUtilizationDay;
	
	@Value("${machine.dealerjcbnoncommunicatingdate}")
	private int noncommunicatingdate;
	
	@Autowired
	private DealerRepository dealerRepo;
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private Utilities utilities;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MachineFeedParserDataRepo machineFeedParserDataRepo;
	
	@Autowired
	private MachineFeedLocationRepo machineFeedLocationRepo;
	
	@Autowired
	private MachineEngineStatusHistoryDataRepo machineEngineStatusHistoryDataRepo;
	
	@Autowired
	private MachineDetailResponseService machineDetailResponseService;
	
	@Autowired
	private MachineFuelHistoryDataRepo machineFuelHistoryDataRepo;
	
	@Autowired
	private MachineService machineService;
	
	@Autowired
	private MachineUtilizationDataRepository machineUtilizationRepo;
	
	@Override
	public CustomerMachinesV3 getMachinesWithCustomerV3(String userName, String distributer, String keyParam,
			String tabSeparator, String customerId, String filter, String search, Boolean skipReports,int pageNumber,
			int pageSize) throws ProcessCustomError {
		CustomerMachinesV3 allMAchines = new CustomerMachinesV3();
		List<MachineResponseListV3> machineResponseList = new ArrayList<>();
		long start = System.currentTimeMillis();
		try {
			Long customerMachineCount =null;
			final User customerUser = userRepository.findByUserName(customerId);
			final User user = userRepository.findByUserName(userName);
			if(user.getUserType() == UserType.JCB) {
				if(keyParam.equals("Telehandler")) {
					keyParam = "Loadall";
				}else if(keyParam.equals("Skid Steer Loader")) {
					keyParam = "Robot";
				}else if(keyParam.equals("Mini Excavator")) {
					keyParam = "JCB30 +MINI EXC";
				}else if(keyParam.equals("Generator")) {
					keyParam = "GENSET Power Products";
				}else if(keyParam.equals("Super Loader")) {
					keyParam = "Front End Loader";
				}
				customerMachineCount = getCustomeMachinesResponseV3(machineResponseList, userName, distributer,
						keyParam, tabSeparator, customerId, filter, search, skipReports, pageNumber, pageSize);
			} else {
				customerMachineCount = getCustomeMachinesResponseV3(machineResponseList, userName, distributer,
						keyParam, tabSeparator, customerId, filter, search, skipReports, pageNumber, pageSize);
			}
			
			// Sorting of machines on the basis of status as on time
			if (!machineResponseList.isEmpty()) { 

				Collections.sort(machineResponseList, new Comparator<MachineResponseListV3>() {
					@Override
					public int compare(MachineResponseListV3 o1, MachineResponseListV3 o2) {
						return o2.getStatusAsOnTime().compareTo(o1.getStatusAsOnTime());
					}

				});
				
				if (customerUser != null && !"utilizationV3".equals(distributer)) {
					final Customer cust = customerRepository.findById(customerId).get();
					allMAchines = new CustomerMachinesV3(new CustomerInfo(cust.getName(), customerUser.getThumbnail(),
							cust.getId(), customerMachineCount, cust.getPhonenumber(), cust.getAddress(),
							customerUser.getCountry()), machineResponseList);
				} else if ("utilizationV3".equals(distributer)) {
					allMAchines = new CustomerMachinesV3(null, machineResponseList);
				} else {
					final List<Machine> machines = dealerRepo.getMachinesByUsersUserNameAndCustomerName(userName,customerId, PageRequest.of(pageNumber, pageSize));
					allMAchines = new CustomerMachinesV3(
							new CustomerInfo(machines.get(0).getCustomer().getName(), "",
									machines.get(0).getCustomer().getId(), customerMachineCount,
									machines != null ? machines.get(0).getCustomer().getPhonenumber() : "-",
									machines != null ? machines.get(0).getCustomer().getAddress() : "-", "-"),
							machineResponseList);
				}
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error(
					"getMachinesWithCustomer: Retriving Customer  machines failed with message " + ex.getMessage());
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("getMachinesWithCustomer: GET  request for user" + userName);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		log.info("DashboardCustomerMachinesV2 API Duration :"+elapsedTime);
		return allMAchines;
	}
	
	private Long getCustomeMachinesResponseV3(List<MachineResponseListV3> machineResponse, String userName,
			String distributer, String keyParam, String tabSeparator, String customerId, String filter, String search,
			Boolean skipReports, int pageNumber, int pageSize) throws ProcessCustomError {
		Long count = 0L;
		try {
			log.debug("Applying filters for getMachineResponseList : " + filter);
			if ("topCustomers".equals(distributer)) {
				if ("optional".equals(search)) {
					count = dealerRepo.countByUsersUserNameByCustomer(userName, customerId);
					for (final Machine machine : dealerRepo.getByUsersUserNameByCustomer(userName, customerId,
							PageRequest.of(pageNumber, pageSize))) {
						updateMachineResponseV3(machineResponse, machine, skipReports);
					}
				} else {
					count = dealerRepo.countByUsersUserNameByCustomerWithSearch(userName, customerId, search);
					for (final Machine machine : dealerRepo.getByUsersUserNameByCustomerWithSearch(userName,
							customerId, search, PageRequest.of(pageNumber, pageSize))) {
						updateMachineResponseV3(machineResponse, machine, skipReports);
					}
				}
			}
			if ("platformDist".equals(distributer)) {
				if ("ALL".equals(keyParam)) {
					count = dealerRepo.countByUsersUserNamePlatformCustomer(userName, tabSeparator, customerId);
					for (final Machine machine : dealerRepo.getByUsersUserNamePlatformCustomer(userName, tabSeparator,
							customerId, PageRequest.of(pageNumber, pageSize))) {
						updateMachineResponseV3(machineResponse, machine, skipReports);
					}
				} else {
					count = dealerRepo.countByUsersUserNamePlatformModelCustomer(userName, keyParam, tabSeparator, customerId);
					for (final Machine machine : dealerRepo.getByUsersUserNamePlatformModelCustomer(userName, keyParam,
							tabSeparator, customerId, PageRequest.of(pageNumber, pageSize))) {
						updateMachineResponseV3(machineResponse, machine, skipReports);
					}
				}
			}
			if ("alerts".equals(distributer)) {
				if ("ALL".equals(keyParam)) {
					count = dealerRepo.findbyMachineAndEventLevel(userName, customerId, EventLevel.valueOf(tabSeparator));
					for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(userName, customerId,
							EventLevel.valueOf(tabSeparator), PageRequest.of(pageNumber, pageSize))) {
						updateMachineResponseV3(machineResponse, machine, skipReports);
					}
				} else {
					count = dealerRepo.findbyMachineAndEventLevel(userName, customerId, tabSeparator, EventLevel.valueOf(keyParam));
					for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(userName, customerId,
							tabSeparator, EventLevel.valueOf(keyParam), PageRequest.of(pageNumber, pageSize))) {
						updateMachineResponseV3(machineResponse, machine, skipReports);
					}
				}
			}
			if ("connectivity".equals(distributer)) {
				final Date commDate = utilities.getStartDateTimeInDateFormat(noncommunicatingdate);
				if ("ALL".equals(keyParam)) {
					if ("COMMUNICATING".equals(tabSeparator)) {
						count = dealerRepo.countByUsersUserNameCommunicatingMachinesCustomer(userName, commDate,
								customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameCommunicatingMachinesCustomer(
								userName, commDate, customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
					if ("NON_COMMUNICATING".equals(tabSeparator)) {
						count = dealerRepo.countByUsersUserNameNonCommunicatingMachinesCustomer(userName, commDate,
								customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameNonCommunicatingMachinesCustomer(
								userName, commDate, customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				} else {
					if ("COMMUNICATING".equals(keyParam)) {
						count = dealerRepo.countByUsersUserNameCommunicatingMachinesCustomerPlatform(userName,
								commDate, customerId, tabSeparator);
						for (final Machine machine : dealerRepo
								.getByUsersUserNameCommunicatingMachinesCustomerPlatform(userName, commDate, customerId,
										tabSeparator, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
					if ("NON_COMMUNICATING".equals(keyParam)) {
						count = dealerRepo.countByUsersUserNameNonCommunicatingMachinesCustomerPlatform(userName,
								commDate, customerId, tabSeparator);
						for (final Machine machine : dealerRepo
								.getByUsersUserNameNonCommunicatingMachinesCustomerPlatform(userName, commDate,
										customerId, tabSeparator, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				}
			}
			if ("renewalStatus".equals(distributer)) {
				final Date today = utilities.getDate(utilities.getStartDate(0));
				final Date maxRenewalDate = utilities.getDate(utilities.getEndDate(30));
				final Date communicatingDate = utilities.getStartDateTimeInDateFormat(-60);
				if ("ALL".equals(keyParam)) {
					final LivelinkRenawal livelinkRenewalstatus = LivelinkRenawal.valueOf(tabSeparator);
					switch (livelinkRenewalstatus) {
					case RENEWAL_OVERDUE:
						count = dealerRepo.getCountByCustomeridForRenewalOverDue(userName, today, customerId,
								communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridForRenewalOverDue(userName, today,
								customerId, communicatingDate, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case RENEWAL_APPROACHING:
						count = dealerRepo.getCountByCustomeridForRenewalApproaching(userName, maxRenewalDate,
								customerId, communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridForRenewalApproaching(userName,
								maxRenewalDate, customerId, communicatingDate, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case RENEWAL_IMMEDIATE:
						count = dealerRepo.getCountByCustomeridForRenewalImmediate(userName, today, maxRenewalDate,
								customerId, communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridForRenewalImmediate(userName, today,
								maxRenewalDate, customerId, communicatingDate, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case RENEWAL_NO_DATA:
						count = dealerRepo.getCountByCustomeridForRenewalNoData(userName, customerId,
								communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridForRenewalNoData(userName, customerId,
								communicatingDate, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case ALL:
						break;
					}
				} else {
					final LivelinkRenawal livelinkRenewal = LivelinkRenawal.valueOf(keyParam);
					switch (livelinkRenewal) {
					case RENEWAL_OVERDUE:
						count = dealerRepo.getCountByCustomeridPlatformForRenewalOverDue(userName, today, customerId,
								tabSeparator, communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridPlatformForRenewalOverDue(userName,
								today, customerId, tabSeparator, communicatingDate,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case RENEWAL_APPROACHING:
						count = dealerRepo.getCountByCustomeridPlatfromForRenewalApproaching(userName, maxRenewalDate,
								customerId, tabSeparator, communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridPlatfromForRenewalApproaching(userName,
								maxRenewalDate, customerId, tabSeparator, communicatingDate,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case RENEWAL_IMMEDIATE:
						count = dealerRepo.getCountByCustomeridPlatfromForRenewalImmediate(userName, today,
								maxRenewalDate, customerId, tabSeparator, communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridPlatfromForRenewalImmediate(userName,
								today, maxRenewalDate, customerId, tabSeparator, communicatingDate,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case RENEWAL_NO_DATA:
						count = dealerRepo.getCountByCustomeridPlatformForRenewalNoData(userName, customerId,
								tabSeparator, communicatingDate);
						for (final Machine machine : dealerRepo.getByCustomeridPlatformForRenewalNoData(userName,
								customerId, tabSeparator, communicatingDate, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case ALL:
						break;
					}
				}
			}
			if ("serviceStatus".equals(distributer)) {
				if ("ALL".equals(keyParam)) {
					final ServiceCategory targetServiceCategory = ServiceCategory.valueOf(tabSeparator);
					Date lastDate;
					switch (targetServiceCategory) {
					case SERVICE_DUE:
						lastDate = utilities.getDate(utilities.getEndDate(serviceDueMaxDays));
						count = dealerRepo.countByUsersUserNameServiceDueCustomer(userName, lastDate,
								serviceDueMinHours, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceDueCustomer(userName,
								lastDate, serviceDueMinHours, customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case SERVICE_OVERDUE:
						count = dealerRepo.countByUsersUserNameServiceOverDueCustomer(userName, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceOverDueCustomer(userName,
								customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case SERVICE_NORMAL:
						lastDate = utilities.getDate(utilities.getEndDate(serviceDueMaxDays));
						count = dealerRepo.countByUsersUserNameServiceNormalCustomer(userName, lastDate,
								serviceDueMinHours, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceNormalCustomer(userName,
								lastDate, serviceDueMinHours, customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case NO_DATA_AVAILABLE:
						count = dealerRepo.countByUsersUserNameServiceNoDataCustomer(userName, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceNoDataCustomer(userName,
								customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					}
				} else {
					final ServiceCategory targetServiceCategory = ServiceCategory.valueOf(keyParam);
					Date lastDate;
					switch (targetServiceCategory) {
					case SERVICE_DUE:
						lastDate = utilities.getDate(utilities.getEndDate(serviceDueMaxDays));
						count = dealerRepo.countByUsersUserNameServiceDueCustomerPlatform(userName, lastDate,
								serviceDueMinHours, customerId, tabSeparator);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceDueCustomerPlatform(userName,
								lastDate, serviceDueMinHours, customerId, tabSeparator,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case SERVICE_OVERDUE:
						count = dealerRepo.countByUsersUserNameServiceOverDueCustomerPlatform(userName, customerId,
								tabSeparator);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceOverDueCustomerPlatform(
								userName, customerId, tabSeparator, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case SERVICE_NORMAL:
						lastDate = utilities.getDate(utilities.getEndDate(serviceDueMaxDays));
						count = dealerRepo.countByUsersUserNameServiceNormalCustomerPlatform(userName, lastDate,
								serviceDueMinHours, customerId, tabSeparator);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceNormalCustomerPlatform(
								userName, lastDate, serviceDueMinHours, customerId, tabSeparator,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					case NO_DATA_AVAILABLE:
						count = dealerRepo.countByUsersUserNameServiceNoDataCustomerPlatform(userName, customerId,
								tabSeparator);
						for (final Machine machine : dealerRepo.getByUsersUserNameServiceNoDataCustomerPlatform(
								userName, customerId, tabSeparator, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
						break;
					}
				}
			}
			if ("utilization".equals(distributer)) {
				final Date startDate = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
				final Date endDate = utilities.getDate(utilities.getEndDate(0));
				long days = (endDate.getTime() - startDate.getTime()) / 86400000L;
				if ("ALL".equals(keyParam)) {
					final List<String> allVin = dealerRepo.getByUsersUserNameAndCustomerName(userName, customerId);
					if ("LESSER_USED".equals(tabSeparator)) {
						final List<String> vinList = machineUtilizationRepo.findByMachineUsageForDuration(allVin,
								startDate, endDate, (double)(0), (double)(lesserUsedMaxRange * days));
						count = Long.valueOf(vinList.size());
						if (vinList != null && vinList.size() > 0) {
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(vinList,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
					if ("HEAVILY_USED".equals(tabSeparator)) {
						double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
						final List<String> vinList = machineUtilizationRepo.findByMachineUsageForDuration(allVin,
								startDate, endDate, heavilyUsedMin, (double)(heavilyUsedMaxRange * days));
						count = Long.valueOf(vinList.size());
						if (vinList != null && vinList.size() > 0) {
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(vinList,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
					if ("MODERATED_USED".equals(tabSeparator)) {
						double moderateUsedMin = (lesserUsedMaxRange * days) + 1D;
						final List<String> vinList = machineUtilizationRepo.findByMachineUsageForDuration(allVin,
								startDate, endDate, moderateUsedMin, (double)(moderateUsedMaxRange * days));
						count = Long.valueOf(vinList.size());
						if (vinList != null && vinList.size() > 0) {
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(vinList,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
					if ("NO_DATA_AVAILABLE".equals(tabSeparator)) {
						final List<String> vinList = machineUtilizationRepo.findByMachineNotUsedForDuration(allVin,
								startDate, endDate);
						allVin.removeAll(vinList);
						if (allVin.size() > 0) {
							count = Long.valueOf(allVin.size());
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(allVin,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
				} else {
					final List<String> allVin = dealerRepo.getByUsersUserNameAndCustomerName(userName, customerId);
					if ("LESSER_USED".equals(keyParam)) {
						final List<String> vinList = machineUtilizationRepo.findByMachineUsageForDuration(allVin,
								startDate, endDate, (double)(0), (double)(lesserUsedMaxRange * days), tabSeparator);
						count = Long.valueOf(vinList.size());
						if (vinList != null && vinList.size() > 0) {
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(vinList,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
					if ("HEAVILY_USED".equals(keyParam)) {
						double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
						final List<String> vinList = machineUtilizationRepo.findByMachineUsageForDuration(allVin,
								startDate, endDate, heavilyUsedMin, (double)(heavilyUsedMaxRange * days),
								tabSeparator);
						count = Long.valueOf(vinList.size());
						if (vinList != null && vinList.size() > 0) {
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(vinList,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
					if ("MODERATED_USED".equals(keyParam)) {
						double moderateUsedMin = (lesserUsedMaxRange * days) + 1D;
						final List<String> vinList = machineUtilizationRepo.findByMachineUsageForDuration(allVin,
								startDate, endDate, moderateUsedMin, (double)(moderateUsedMaxRange * days),
								tabSeparator);
						count = Long.valueOf(vinList.size());
						if (vinList != null && vinList.size() > 0) {
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(vinList,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
					if ("NO_DATA_AVAILABLE".equals(keyParam)) {
						final List<String> vinList = machineUtilizationRepo.findByMachineNotUsedForDuration(allVin,
								startDate, endDate, tabSeparator);
						allVin.removeAll(vinList);
						if (allVin.size() > 0) {
							count = Long.valueOf(allVin.size());
							for (final Machine machine : dealerRepo.getByUsersUserNameAndVinList(allVin,
									PageRequest.of(pageNumber, pageSize))) {
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
				}
			} else if ("utilizationV3".equals(distributer)) {
				final Date startDate = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
				final Date endDate = utilities.getDate(utilities.getEndDate(0));
				long days = (endDate.getTime() - startDate.getTime()) / 86400000L;
				Machine machine = null;
				if ("LESSER_USED".equals(keyParam)) {
					final List<String> machineVinList = machineUtilizationRepo.findMachineByUtilizationUsageForDuration(
							userName, startDate, endDate, (double)(0), (double)(lesserUsedMaxRange * days),
							PageRequest.of(pageNumber, pageSize));
					count = Long.valueOf(machineVinList.size());
					if (machineVinList != null && machineVinList.size() > 0) {
						for (final String vin : machineVinList) {
							machine = machineRepository.findByVin(vin);
							if (machine != null)
								updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				} else if ("HEAVILY_USED".equals(keyParam)) {
					double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
					final List<String> machineVinList = machineUtilizationRepo.findMachineByUtilizationUsageForDuration(
							userName, startDate, endDate, heavilyUsedMin, (double)(heavilyUsedMaxRange * days),
							PageRequest.of(pageNumber, pageSize));
					count = Long.valueOf(machineVinList.size());
					if (machineVinList != null && machineVinList.size() > 0) {
						for (final String vin : machineVinList) {
							machine = machineRepository.findByVin(vin);
							if (machine != null)
								updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				} else if ("MODERATED_USED".equals(keyParam)) {
					double moderateUsedMin = (lesserUsedMaxRange * days) + 1D;
					final List<String> machineVinList = machineUtilizationRepo.findMachineByUtilizationUsageForDuration(
							userName, startDate, endDate, moderateUsedMin, (double)(moderateUsedMaxRange * days),
							PageRequest.of(pageNumber, pageSize));
					count = Long.valueOf(machineVinList.size());
					if (machineVinList != null && machineVinList.size() > 0) {
						for (final String vin : machineVinList) {
							machine = machineRepository.findByVin(vin);
							if (machine != null)
								updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				} else if ("NO_DATA_AVAILABLE".equals(keyParam)) {
					final List<String> allVin = dealerRepo.getByUsersUserName(userName);
					final List<String> vinList = machineUtilizationRepo.findByMachineNotUsedForDuration(userName,
							startDate, endDate);
					allVin.removeAll(vinList);
					count = Long.valueOf(allVin.size());
					if (allVin != null && allVin.size() > 0) {
						int index = pageNumber * pageSize;
						int recordCount = 0;
						for (int i = index; i < allVin.size(); i++) {
							if (recordCount == pageSize)
								break;
							machine = machineRepository.findByVin(allVin.get(i));
							if (machine != null) {
								++recordCount;
								updateMachineResponseV3(machineResponse, machine, skipReports);
							}
						}
					}
				}
			}
			if ("warranty".equals(distributer)) {
				if ("ALL".equals(keyParam)) {
				} else {
				}
			}
			if ("machineLocator".equals(distributer)) {
				TransitMode targetTransitMode;
				if ("ALL".equals(keyParam)) {
					targetTransitMode = TransitMode.valueOf(tabSeparator);
					if (TransitMode.NORMAL.equals(targetTransitMode)) {
						count = dealerRepo.countByUsersUserNameTransitModeMachinesCustomer(userName,
								TransitMode.NORMAL, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameTransitModeMachinesCustomer(userName,
								TransitMode.NORMAL, customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
					if (TransitMode.TRANSIT.equals(targetTransitMode)) {
						count = dealerRepo.countByUsersUserNameTransitModeMachinesCustomer(userName,
								TransitMode.TRANSIT, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameTransitModeMachinesCustomer(userName,
								TransitMode.TRANSIT, customerId, PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				} else {
					targetTransitMode = TransitMode.valueOf(keyParam);
					if (TransitMode.NORMAL.equals(targetTransitMode)) {
						count = dealerRepo.countByUsersUserNameTransitModeMachinesCustomer(userName,
								TransitMode.NORMAL, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameTransitModeMachinesCustomerPlatform(
								userName, TransitMode.NORMAL, customerId, tabSeparator,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
					if (TransitMode.TRANSIT.equals(targetTransitMode)) {
						count = dealerRepo.countByUsersUserNameTransitModeMachinesCustomer(userName,
								TransitMode.TRANSIT, customerId);
						for (final Machine machine : dealerRepo.getByUsersUserNameTransitModeMachinesCustomerPlatform(
								userName, TransitMode.TRANSIT, customerId, tabSeparator,
								PageRequest.of(pageNumber, pageSize))) {
							updateMachineResponseV3(machineResponse, machine, skipReports);
						}
					}
				}
			}
			if (machineResponse.size() == 0) {
				// return all machines of customer
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error("getCustomeMachinesResponse: Failed to process " + ex.getMessage());
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return count;
	}
	
	private void updateMachineResponseV3(List<MachineResponseListV3> machineResponse, Machine machine,
			Boolean skipReports) {
		try {
			final SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
			format.setTimeZone(TimeZone.getTimeZone(timezone));
			final String machineStartDateRange = format.format(utilities.getDate(utilities.getStartDate(loadHistoricalDataForDays)));
			final String machineEndDateRange = format.format(utilities.getDate(utilities.getEndDate(-1)));
			utilities.getDate(utilities.getStartDate(loadAlertsDataForDays));
			utilities.getDate(utilities.getEndDate(1));
			MachineFeedParserData machineFeedParserData = machineFeedParserDataRepo.findByVin(machine.getVin());
			MachineFeedLocation machineFeedLocation = machineFeedLocationRepo.findByVin(machine.getVin());
			final String vin = machine.getVin();
			List<EngineHistoryDataListV2> engineHistoryDayDataList = new ArrayList<>();
			List<FuelHistoryDataListV2> fuelHistoryDayDataList = new ArrayList<>();
			MachineEnginestatusHistory machineEnginestatusHistory = null;

			boolean flag = true;
			
			if ((!FuelLevelNAConstant.getExceptionMachines().contains(vin))
					&& FuelLevelNAConstant.getFuellevelnaconfig().containsKey(machine.getPlatform()) && FuelLevelNAConstant
							.getFuellevelnaconfig().get(machine.getPlatform()).contains(vin.substring(3, 8))) {
				flag = false;
				fuelHistoryDayDataList = null;
			}

			if (skipReports == null || !skipReports) {
				for (int i = 6; i >= 0; i--) {
					Date startDate = utilities.getDate(utilities.getStartDate(i));
					Date endDate = utilities.getDate(utilities.getStartDate(i - 1));
					EngineHistoryDataListV2 engineHistoryData = new EngineHistoryDataListV2();
					engineHistoryData.date = startDate;
					engineHistoryData.timestamps = i > 2 ? new ArrayList<>()
							: machineEngineStatusHistoryDataRepo.getDateByVin(vin, startDate, endDate);
					engineHistoryData.values = i > 2 ? new ArrayList<>()
							: machineEngineStatusHistoryDataRepo.getByVin(vin, startDate, endDate);
					engineHistoryDayDataList.add(engineHistoryData);
					if (flag) {
						FuelHistoryDataListV2 fuelHistoryData = new FuelHistoryDataListV2();
						fuelHistoryData.date = startDate;
						fuelHistoryData.values = i > 2 ? new ArrayList<>()
								: machineFuelHistoryDataRepo.getFuelLevelByVin(vin, startDate, endDate);
						fuelHistoryData.timestamps = i > 2 ? new ArrayList<>()
								: machineFuelHistoryDataRepo.getDateByVin(vin, startDate, endDate);
						fuelHistoryDayDataList.add(fuelHistoryData);
					}
				}
			} else {
				Date startDate = utilities.getDate(utilities.getStartDate(0));
				Date endDate = utilities.getDate(utilities.getStartDate(-1));
				machineEnginestatusHistory = machineEngineStatusHistoryDataRepo.findByVinAndBetweenDate(vin, startDate,
						endDate);
			}
			
			final MachineDetailResponse machinedetails = machineDetailResponseService
					.getMachineDetailResponseListV2(machine, machineFeedParserData, skipReports);

			if (!engineHistoryDayDataList.isEmpty()
					&& engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values != null
					&& !(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.isEmpty())) {
				if (engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).timestamps
						.get(engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).timestamps.size() - 1)
						.getKey().after(machine.getStatusAsOnTime())) {
					machinedetails.getMachine().setEngineStatus(
							engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.get(
									engineHistoryDayDataList.get(engineHistoryDayDataList.size() - 1).values.size() - 1)
									.getVal() == 1 ? true : false);
				} else {
					machinedetails.getMachine()
							.setEngineStatus("on".equalsIgnoreCase(machine.getEngineStatus()) ? true : false);
				}
				machinedetails.getMachine()
						.setEngineValue(machinedetails.getMachine().getEngineStatus() == true ? "on" : "off");
			} else if (machineEnginestatusHistory != null && skipReports) {
				machinedetails.getMachine()
						.setEngineStatus(machineEnginestatusHistory.getDateTime().after(machine.getStatusAsOnTime())
								? machineEnginestatusHistory.getIsEngineOn()
								: "on".equalsIgnoreCase(machine.getEngineStatus()) ? true : false);
				machinedetails.getMachine()
						.setEngineValue(machinedetails.getMachine().getEngineStatus() == true ? "on" : "off");
			}

			final ServiceStatus machineServiceStatus = machineService.getMachineServiceHistoryStatus(machine);
			if (null != machineFeedParserData && null != machineFeedLocation
					&& null != machineFeedParserData.getStatusAsOnTime()) {
				machineResponse.add(new MachineResponseListV3.Builder(machine, machinedetails, machineServiceStatus)
						.dateRange(machineStartDateRange + " - " + machineEndDateRange)
						.feedFuelLevel(machineFeedParserData, machine)
						.setFeedData(machineFeedParserData, machineFeedLocation, "-")
						.supportedFeatures(machineService.doSupportFeatures(machine.getVin()))
						.fuelHistoryDayDataList(fuelHistoryDayDataList)
						.engineHistoryDayDataList(engineHistoryDayDataList)
						.utilizationDateRange(format.format(utilities.getDate(utilities.getStartDate(6))) + " - "
								+ format.format(utilities.getDate(utilities.getStartDate(0))))
						.build());
			} else {
				machineResponse.add(new MachineResponseListV3.Builder(machine, machinedetails, machineServiceStatus)
						.dateRange(machineStartDateRange + " - " + machineEndDateRange)
						.fuelLevel(machine.getFuelLevel() >= 0 && machine.getFuelLevel() < 6 ? "low"
								: machine.getFuelLevel() >= 6 && machine.getFuelLevel() <= 15 ? "normal" : "high")
						.supportedFeatures(machineService.doSupportFeatures(machine.getVin()))
						.fuelHistoryDayDataList(fuelHistoryDayDataList)
						.engineHistoryDayDataList(engineHistoryDayDataList)
						.utilizationDateRange(format.format(utilities.getDate(utilities.getStartDate(6))) + " - "
								+ format.format(utilities.getDate(utilities.getStartDate(0))))
						.build());
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error("Failed to process " + ex.getMessage());
		}
	}

}
