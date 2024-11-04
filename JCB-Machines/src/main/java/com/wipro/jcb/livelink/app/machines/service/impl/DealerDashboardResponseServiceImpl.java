package com.wipro.jcb.livelink.app.machines.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.constants.UserType;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerDistribution;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerInfo;
import com.wipro.jcb.livelink.app.machines.dealer.response.DistributionParams;
import com.wipro.jcb.livelink.app.machines.entity.Customer;
import com.wipro.jcb.livelink.app.machines.entity.DashboardData;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.entity.UtilizationLegendWiseCustomerMachineCount;
import com.wipro.jcb.livelink.app.machines.entity.UtilizationPlatformWiseCustomerMachineCount;
import com.wipro.jcb.livelink.app.machines.entity.UtilizationPlatformWiseMachineCount;
import com.wipro.jcb.livelink.app.machines.enums.CommunicationType;
import com.wipro.jcb.livelink.app.machines.enums.LivelinkRenawal;
import com.wipro.jcb.livelink.app.machines.enums.MachineUtilizationCategory;
import com.wipro.jcb.livelink.app.machines.enums.ServiceCategory;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.AlertRepository;
import com.wipro.jcb.livelink.app.machines.repo.CustomerRepository;
import com.wipro.jcb.livelink.app.machines.repo.DashboardDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.GenericeQueryRepo;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineUtilizationDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.UserRepository;
import com.wipro.jcb.livelink.app.machines.repo.UtilizationLegendWiseCustomerMachineCountRepository;
import com.wipro.jcb.livelink.app.machines.repo.UtilizationLegendWiseMachineCountRepository;
import com.wipro.jcb.livelink.app.machines.repo.UtilizationPlatformWiseCustomerMachineCountRepository;
import com.wipro.jcb.livelink.app.machines.repo.UtilizationPlatformWiseMachineCountRepository;
import com.wipro.jcb.livelink.app.machines.service.DealerDashboardResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealerDashboardResponseServiceImpl implements DealerDashboardResponseService {
	
	@Value("${machine.dealerjcbnoncommunicatingdate}")
	private int noncommunicatingdate;
	
	@Value("${machine.lesser.used.max.range}")
	private int lesserUsedMaxRange;
	
	@Value("${machine.moderate.used.max.range}")
	private int moderateUsedMaxRange;
	
	@Value("${machine.havily.used.max.range}")
	private int heavilyUsedMaxRange;
	
	@Value("${machine.utilization.day}")
	private int machineUtilizationDay;
	
	@Value("${machine.approachingservicedays}")
	private int machineApproachingServiceDays;
	
	@Value("${admin.userName}")
	private String adminUserName;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Autowired
	private Utilities utilities;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository custRepo;
	
	@Autowired
	private MachineRepository machineRepo;
	
	@Autowired
	private GenericeQueryRepo genericeQueryRepo;
	
	@Autowired
	private DashboardDataRepository dashboardDataRepository;
	
	@Autowired
	private UtilizationLegendWiseCustomerMachineCountRepository utilizationLegendWiseCustomerMachineCountRepository;
	
	@Autowired
	private UtilizationLegendWiseMachineCountRepository utilizationLegendWiseMachineCountRepository;
	
	@Autowired
	private UtilizationPlatformWiseCustomerMachineCountRepository utilizationPlatformWiseCustomerMachineCountRepository;
	
	@Autowired
	private UtilizationPlatformWiseMachineCountRepository utilizationPlatformWiseMachineCountRepository;
	
	@Autowired
	private MachineUtilizationDataRepository machineUtilizationRepo;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd_MM_yyyy");
	
	public static final String PLATFORM_PARAM_NAME = "platform";
	public static final String MODEL_PARAM_NAME = "model";

	@Override
	public CustomerDistribution getDealerDashboardDetails(String userName, String search, String distributor,
			String keyParam, int pageNumber, int pageSize) throws ProcessCustomError {
		long start = System.currentTimeMillis();
		try {
			CustomerDistribution custDistribution = new CustomerDistribution();
			final Long machineCount = genericeQueryRepo.getMachineCount(userName, true);
			custDistribution.setMachineCount(machineCount);
			final User user = userRepository.findByUserName(userName);
			if (user.getUserType() == UserType.JCB) {
				if (keyParam.equals("Telehandler")) {
					keyParam = "Loadall";
				} else if (keyParam.equals("Skid Steer Loader")) {
					keyParam = "Robot";
				} else if (keyParam.equals("Mini Excavator")) {
					keyParam = "JCB30 +MINI EXC";
				} else if (keyParam.equals("Generator")) {
					keyParam = "GENSET Power Products";
				} else if (keyParam.equals("Super Loader")) {
					keyParam = "Front End Loader";
				}
			}
			if ("platformDist".equals(distributor)) {
				if ("ALL".equals(keyParam)) {
					custDistribution.setMachineCount(machineCount);
				} else {
					custDistribution.setMachineCount(machineRepo.countByUsersUserNamePlatform(userName, keyParam));
				}

				if (user.getUserType() == UserType.JCB) {
					log.info("Platform Details for Admin User(JCB)-" + keyParam);
					if (pageNumber == 0 || pageNumber == 1) {
						String platformKey = dateformat.format(new Date()) + "JCB_PLATFORM_" + keyParam + "_"+ pageNumber;
						DashboardData dashboardData = dashboardDataRepository.getDashboardData(platformKey);
						if (dashboardData == null) {
							custDistribution.setDistributinParams(getByPlatform(userName, machineCount, keyParam, search, pageNumber, pageSize));
							String jsonString = new com.google.gson.Gson().toJson(custDistribution);
							DashboardData data = new DashboardData();
							data.setType(platformKey);
							data.setData(jsonString);
							dashboardDataRepository.save(data);
							return custDistribution;
						} else {
							DashboardData data = dashboardDataRepository.getDashboardData(platformKey);
							Gson gsonObj = new Gson();
							CustomerDistribution returnData = gsonObj.fromJson(data.getData(),CustomerDistribution.class);
							return custDistribution = returnData;
						}

					} else {
						custDistribution.setDistributinParams(
								getByPlatform(userName, machineCount, keyParam, search, pageNumber, pageSize));
					}
				} else {

					custDistribution.setDistributinParams(
							getByPlatform(userName, machineCount, keyParam, search, pageNumber, pageSize));
				}
			}
			if ("alerts".equals(distributor)) {
				if ("ALL".equals(keyParam)) {
					custDistribution.setMachineCount(machineCount);
				} else {
					custDistribution.setMachineCount(getAlertCount(userName, keyParam));
				}
				custDistribution.setDistributinParams(getByAlertsLevel(userName, machineCount, keyParam, search, pageNumber, pageSize));
			}
			if ("serviceStatus".equals(distributor)) {
				if ("ALL".equals(keyParam)) {
					custDistribution.setMachineCount(machineCount);
				} else {
					custDistribution.setMachineCount(getMachineCountByServiceStatus(userName, keyParam));
				}
				custDistribution.setDistributinParams(
						getByServiceStatus(userName, machineCount, keyParam, search, pageNumber, pageSize));
			}
			if ("utilization".equals(distributor)) {
				if (userRepository.findByUserName(userName).getUserType() == UserType.JCB && pageNumber == 0) {
					if ("ALL".equals(keyParam)) {
						custDistribution.setMachineCount(machineCount);
						custDistribution.setDistributinParams(getAllUtilizationData(pageNumber, pageSize));
					} else {
						MachineUtilizationCategory utilizationCategory = MachineUtilizationCategory.valueOf(keyParam);
						custDistribution.setMachineCount(Long.valueOf(utilizationLegendWiseMachineCountRepository
								.findByUtilizationCategory(utilizationCategory).getMachineCount()));
						custDistribution.setDistributinParams(
								getLegendWiseUtilizationData(utilizationCategory, pageNumber, pageSize));
					}
				} else {
					if ("ALL".equals(keyParam)) {
						custDistribution.setMachineCount(machineCount);
					} else {
						custDistribution.setMachineCount(getMachineCountByUtilizationStatus(userName, keyParam));
					}
					custDistribution.setDistributinParams(
							getByUtilizationStatus(userName, machineCount, keyParam, search, pageNumber, pageSize));
				}
			}
			if ("machineLocator".equals(distributor)) {
				if ("ALL".equals(keyParam)) {
					custDistribution.setMachineCount(machineCount);
				} else {
					custDistribution.setMachineCount(getMachineCountByMachineLocater(userName, keyParam));
				}
				custDistribution.setDistributinParams(getByMachineLocater(userName, keyParam, pageNumber, pageSize));
			}
			if ("connectivity".equals(distributor)) {
				if ("ALL".equals(keyParam)) {
					custDistribution.setMachineCount(genericeQueryRepo.getMachineCountWithRenewalFlag(userName, true));
				} else {
					custDistribution.setMachineCount(getByCommunicatingData(userName, keyParam));
				}
				custDistribution.setDistributinParams(getByCommunicating(userName, keyParam, pageNumber, pageSize));
			}
			if ("renewalStatus".equals(distributor)) {
				if ("ALL".equals(keyParam)) {
					custDistribution.setMachineCount(machineCount);
				} else {
					custDistribution.setMachineCount(getRenewalDataWithCustomers(userName, keyParam));
				}
				custDistribution.setDistributinParams(getByRenewal(userName, keyParam, pageNumber, pageSize));
			}
			log.info("getDealerDashboardDetails:end of request for user {}", userName);
			long end = System.currentTimeMillis();
			long elapsedTime = end - start;
			log.info("DashboardDetails API Duration :" + elapsedTime + "-" + userName + "-" + distributor + "-"+ keyParam);
			return custDistribution;
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error("DashboardDetails Failed to Process request with message " + userName + " - " + ex.getMessage());
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	private List<DistributionParams> getByRenewal(String userName, String keyParam, int pageNumber, int pageSize) {
		final List<DistributionParams> distributions = new LinkedList<>();
		final Date today = utilities.getDate(utilities.getStartDate(0));
		final Date maxRenewalDate = utilities.getDate(utilities.getEndDate(30));
		final Date communicatingDate = utilities.getStartDateTimeInDateFormat(-60);
		List<MachineWithCustomerId> machineCust = null;
		DistributionParams dist = null;
		final LivelinkRenawal caseLivelinkRenawal = LivelinkRenawal.valueOf(keyParam);
		switch (caseLivelinkRenawal) {
		case ALL:
			
			// RENEWAL_OVERDUE
			machineCust = machineRepo.getByAllCustomerForRenewalOverDue(userName, today, communicatingDate,PageRequest.of(pageNumber, pageSize));
			dist = new DistributionParams();
			dist.setMachineCount(machineRepo.getCountByRenewalOverDueWithCustomers(userName, today, communicatingDate));
			dist.setCategory(LivelinkRenawal.RENEWAL_OVERDUE.getName());
			distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			
			// RENEWAL_APPROACHING
			machineCust = machineRepo.getByAllCustomerForRenewalApproaching(userName, maxRenewalDate, communicatingDate,PageRequest.of(pageNumber, pageSize));
			dist = new DistributionParams();
			dist.setMachineCount(machineRepo.getCountByRenewalApproachingWithCustomers(userName, maxRenewalDate, communicatingDate));
			dist.setCategory(LivelinkRenawal.RENEWAL_APPROACHING.getName());
			distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			
			// RENEWAL_IMMEDIATE
			machineCust = machineRepo.getByAllCustomerForRenewalImmediate(userName, today, maxRenewalDate,communicatingDate, PageRequest.of(pageNumber, pageSize));
			dist = new DistributionParams();
			dist.setMachineCount(machineRepo.getCountByRenewalImmediateWithCustomers(userName, today, maxRenewalDate, communicatingDate));
			dist.setCategory(LivelinkRenawal.RENEWAL_IMMEDIATE.getName());
			distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			
			// RENEWAL_NO_DATA
			machineCust = machineRepo.getByAllCustomerForRenewalNoData(userName, communicatingDate,PageRequest.of(pageNumber, pageSize));
			dist = new DistributionParams();
			dist.setMachineCount(machineRepo.getCountByRenewalNoDataWithCustomers(userName, communicatingDate));
			dist.setCategory(LivelinkRenawal.RENEWAL_NO_DATA.getName());
			distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			break;
			
		case RENEWAL_OVERDUE:
			final List<MachineWithPlatform> platformListRO = machineRepo.getByPlatformForRenewalOverDue(userName, today,
					communicatingDate);
			for (final MachineWithPlatform platform : platformListRO) {
				machineCust = machineRepo.getByAllCustomerForRenewalOverDue(userName, today, platform.getPlatform(),
						communicatingDate, PageRequest.of(pageNumber, pageSize));
				dist = new DistributionParams();
				dist.setMachineCount(platform.getMachineCount());
				dist.setCategory(platform.getPlatform());
				distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			}
			break;
		case RENEWAL_APPROACHING:
			final List<MachineWithPlatform> platformListRA = machineRepo.getByPlatformForRenewalApproaching(userName,
					maxRenewalDate, communicatingDate);
			for (final MachineWithPlatform platform : platformListRA) {
				machineCust = machineRepo.getByAllCustomerForRenewalApproaching(userName, maxRenewalDate,
						platform.getPlatform(), communicatingDate, PageRequest.of(pageNumber, pageSize));
				dist = new DistributionParams();
				dist.setMachineCount(platform.getMachineCount());
				dist.setCategory(platform.getPlatform());
				distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			}
			break;
		case RENEWAL_IMMEDIATE:
			final List<MachineWithPlatform> platformListRI = machineRepo.getByPlatformForRenewalImmediate(userName,
					today, maxRenewalDate, communicatingDate);
			for (final MachineWithPlatform platform : platformListRI) {
				machineCust = machineRepo.getByAllCustomerForRenewalImmediate(userName, today, maxRenewalDate,
						platform.getPlatform(), communicatingDate, PageRequest.of(pageNumber, pageSize));
				dist = new DistributionParams();
				dist.setMachineCount(platform.getMachineCount());
				dist.setCategory(platform.getPlatform());
				distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			}
			break;
		case RENEWAL_NO_DATA:
			final List<MachineWithPlatform> platformListRN = machineRepo.getByPlatformForRenewalNoData(userName,
					communicatingDate);
			for (final MachineWithPlatform platform : platformListRN) {
				machineCust = machineRepo.getByAllCustomerForRenewalNoData(userName, platform.getPlatform(),
						communicatingDate, PageRequest.of(pageNumber, pageSize));
				dist = new DistributionParams();
				dist.setMachineCount(platform.getMachineCount());
				dist.setCategory(platform.getPlatform());
				distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			}
			break;
		}
		return distributions;
	}
	
	private Long getRenewalDataWithCustomers(String userName, String keyParam) {
		final Date today = utilities.getDate(utilities.getStartDate(0));
		final Date maxRenewalDate = utilities.getDate(utilities.getEndDate(30));
		final Date communicatingDate = utilities.getDateTime(utilities.getEndDate(-60));
		final LivelinkRenawal resultLivelinkRenawal = LivelinkRenawal.valueOf(keyParam);
		return switch (resultLivelinkRenawal) {
		case RENEWAL_OVERDUE -> machineRepo.getCountByRenewalOverDueWithCustomers(userName, today, communicatingDate);
		case RENEWAL_APPROACHING ->
			machineRepo.getCountByRenewalApproachingWithCustomers(userName, maxRenewalDate, communicatingDate);
		case RENEWAL_IMMEDIATE ->
			machineRepo.getCountByRenewalImmediateWithCustomers(userName, today, maxRenewalDate, communicatingDate);
		case RENEWAL_NO_DATA -> machineRepo.getCountByRenewalNoDataWithCustomers(userName, communicatingDate);
		default -> 0L;
		};
	}
	
	private List<DistributionParams> getByCommunicating(String userName, String keyParam, int pageNumber, int pageSize) {
		final CommunicationType communicationType = CommunicationType.valueOf(keyParam);
		final List<DistributionParams> distributions = new LinkedList<>();
		switch (communicationType) {
		case COMMUNICATING:
			loadCommunicationByPlatform(userName, true, distributions, pageNumber, pageSize);
			break;
		case NON_COMMUNICATING:
			loadCommunicationByPlatform(userName, false, distributions, pageNumber, pageSize);
			break;
		case ALL:
			loadCommunicationByMachine(userName, distributions, pageNumber, pageSize);
			break;
		default:
			break;
		}
		return distributions;
	}
	
	private void loadCommunicationByMachine(String userName, List<DistributionParams> distributions, int pageNumber,
			int pageSize) {
		DistributionParams distributionParams = null;
		final Date date = utilities.getStartDateTimeInDateFormat(noncommunicatingdate);
		// Communicating
		distributionParams = getCustomerForCommunicable(userName, date, true, null, pageNumber, pageSize);
		distributionParams.setCategory(CommunicationType.COMMUNICATING.getName());
		distributionParams.setMachineCount(genericeQueryRepo.getCommunicationData(userName, date, true, true));
		distributions.add(distributionParams);
		
		// NonCommunicating
		distributionParams = getCustomerForCommunicable(userName, date, false, null, pageNumber, pageSize);
		distributionParams.setCategory(CommunicationType.NON_COMMUNICATING.getName());
		distributionParams.setMachineCount(genericeQueryRepo.getCommunicationData(userName, date, false, true));
		distributions.add(distributionParams);
	}
	
	private DistributionParams getCustomerForCommunicable(String userName, Date date, boolean isCommunicable,
			String platform, int pageNumber, int pageSize) {
		List<Tuple> custs = null;
		custs = genericeQueryRepo.getCommunicatingByCustomer(userName, date, isCommunicable, platform, pageNumber, pageSize);
		return loadCustomerDetails(custs);
	}
	
	private void loadCommunicationByPlatform(String userName, boolean isCommunicable,
			List<DistributionParams> distributions, int pageNumber, int pageSize) {
		DistributionParams distributionParams = null;
		final Date date = utilities.getStartDateTimeInDateFormat(noncommunicatingdate);
		final List<Tuple> tuples = genericeQueryRepo.getCommunicationByPlatform(userName, date, isCommunicable);
		for (final Tuple tuple : tuples) {
			distributionParams = getCustomerForCommunicable(userName, date, isCommunicable, tuple.get(0).toString(), pageNumber, pageSize);
			distributionParams.setCategory(tuple.get(0).toString());
			distributionParams.setMachineCount(Long.valueOf(tuple.get(1).toString()));
			distributions.add(distributionParams);
		}
	}
	
	private List<DistributionParams> getByMachineLocater(String userName, String transitMode, int pageNumber,
			int pageSize) {
		final List<DistributionParams> distributions = new LinkedList<>();
		final TransitMode modeOfTransit = TransitMode.valueOf(transitMode);
		switch (modeOfTransit) {
		case ALL:
			loadMachineLocaterDataByMachines(userName, distributions, pageNumber, pageSize);
			break;
		case TRANSIT:
			loadMachineLocaterDataByPlatform(userName, 0, distributions, pageNumber, pageSize);
			break;
		case NORMAL:
			loadMachineLocaterDataByPlatform(userName, 1, distributions, pageNumber, pageSize);
			break;
		default:
			break;
		}
		return distributions;
	}
	
	private Long getByCommunicatingData(String userName, String keyParam) {
		final CommunicationType communicationType = CommunicationType.valueOf(keyParam);
		final Date date = utilities.getStartDateTimeInDateFormat(noncommunicatingdate);
		switch (communicationType) {
		case COMMUNICATING:
			return genericeQueryRepo.getCommunicationData(userName, date, true, true);
		case NON_COMMUNICATING:
			return genericeQueryRepo.getCommunicationData(userName, date, false, true);
		default:
			return 0L;
		}
	}
	
	private void loadMachineLocaterDataByPlatform(String userName, int transitMode,
			List<DistributionParams> distributions, int pageNumber, int pageSize) {
		DistributionParams distributionParams = null;
		final List<Tuple> tuples = genericeQueryRepo.countMachineLocaterByPlatform(userName, transitMode);
		for (final Tuple tuple : tuples) {
			distributionParams = getCustomerForMachineLocater(userName, transitMode, tuple.get(0).toString(),
					pageNumber, pageSize);
			distributionParams.setCategory(tuple.get(0).toString());
			distributionParams.setMachineCount(0l);
			distributions.add(distributionParams);
		}
	}
	
	private void loadMachineLocaterDataByMachines(String userName, List<DistributionParams> distributions,
			int pageNumber, int pageSize) {
		DistributionParams distributionParams = null;
		/* Transit mode */
		distributionParams = getCustomerForMachineLocater(userName, 0, null, pageNumber, pageSize);
		distributionParams.setCategory(String.valueOf(TransitMode.TRANSIT));
		distributionParams.setMachineCount(genericeQueryRepo.countByMachineLocater(userName, 0));
		distributions.add(distributionParams);
		
		// normal mode
		distributionParams = getCustomerForMachineLocater(userName, 1, null, pageNumber, pageSize);
		distributionParams.setCategory(String.valueOf(TransitMode.NORMAL));
		distributionParams.setMachineCount(genericeQueryRepo.countByMachineLocater(userName, 1));
		distributions.add(distributionParams);
	}
	
	private DistributionParams getCustomerForMachineLocater(String userName, int category, String platform,
			int pageNumber, int pageSize) {
		List<Tuple> custs = null;
		custs = genericeQueryRepo.getMachineLocaterByCustomer(userName, category, platform, pageNumber, pageSize);
		return loadCustomerDetails(custs);
	}
	
	private List<DistributionParams> getByPlatform(String userName, Long machineCount, String platformName,
			String search, int pageNumber, int pageSize) {
		final List<DistributionParams> distributons = new ArrayList<DistributionParams>();
		if ("ALL".equals(platformName)) {
			final List<Tuple> list = genericeQueryRepo.getGroupByParamCount(userName, PLATFORM_PARAM_NAME, null, null,
					search, pageNumber, pageSize, true);
			log.debug("GET By Platform dist " + list.size());
			DistributionParams dist = null;
			for (final Tuple tuple : list) {
				dist = getPlatformGroupByCustomer(userName, tuple, platformName, search, pageNumber, pageSize);
				dist.setCategory(tuple.get(0).toString());
				dist.setMachineCount(Long.valueOf(tuple.get(1).toString()));
				distributons.add(dist);
			}
		} else {
			final List<Tuple> list = genericeQueryRepo.getGroupByParamCount(userName, MODEL_PARAM_NAME, null, search,
					platformName, pageNumber, pageSize, true);
			log.debug("GET By Platform dist " + list.size());
			DistributionParams dist = null;
			for (final Tuple tuple : list) {
				dist = getPlatformGroupByCustomer(userName, tuple, platformName, search, pageNumber, pageSize);
				dist.setCategory(tuple.get(0).toString());
				dist.setMachineCount(Long.valueOf(tuple.get(1).toString()));
				distributons.add(dist);
			}
		}
		return distributons;
	}
	
	private DistributionParams getPlatformGroupByCustomer(String userName, Tuple tuple, String platformName,
			String search, int pageNumber, int pageSize) {
		final List<Tuple> custs = genericeQueryRepo.getCustomerGroupByParamCount(userName, platformName,
				tuple.get(0).toString(), null, pageNumber, pageSize);
		return loadCustomerDetails(custs);
	}
	
	private Long getAlertCount(String userName, String keyParam) {
		final EventLevel eventLevel = EventLevel.valueOf(keyParam);
        return switch (eventLevel) {
            case RED ->
                    alertRepository.countMachinesByCriticalAlertsCustomer(userName, EventLevel.RED);
            case YELLOW ->
                    alertRepository.countMachinesByHighAlertsCustomer(userName, EventLevel.RED, EventLevel.YELLOW);
            default -> 0L;
        };
	}
	
	private List<DistributionParams> getByAlertsLevel(String userName, Long machineCount, String alertLevel,
			String search, int pageNumber, int pageSize) {
		final List<DistributionParams> distributions = new LinkedList<>();
		List<MachineWithCustomerId> machineCust = null;
		List<MachineWithPlatform> list = null;
		DistributionParams dist = null;
		if ("ALL".equals(alertLevel)) {
			machineCust = alertRepository.getMachinesByCriticalAlertsCustomer(userName, EventLevel.RED, PageRequest.of(pageNumber, pageSize));
			dist = new DistributionParams();
			dist.setMachineCount(alertRepository.countMachinesByCriticalAlertsCustomer(userName, EventLevel.RED));
			dist.setCategory(EventLevel.RED.toString());
			distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
			machineCust = alertRepository.getMachinesByCriticalAlertsCustomer(userName, EventLevel.YELLOW, PageRequest.of(pageNumber, pageSize));
			dist = new DistributionParams();
			dist.setMachineCount(alertRepository.countMachinesByCriticalAlertsCustomer(userName, EventLevel.YELLOW));
			dist.setCategory(EventLevel.YELLOW.toString());
			distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
		} else {
			list = alertRepository.getAlertCountGroupByPlatform(userName, EventLevel.valueOf(alertLevel));
			for (final MachineWithPlatform machineWithPlatform : list) {
				dist = getEventLevelGroupByCustomer(userName, alertLevel, machineWithPlatform.getPlatform(), search, pageNumber, pageSize);
				dist.setCategory(machineWithPlatform.getPlatform());
				dist.setMachineCount(machineWithPlatform.getMachineCount());
				distributions.add(dist);
			}
		}
		return distributions;
	}
	
	private DistributionParams loadCustomerInMachineWithCID(final DistributionParams dist,
			final List<MachineWithCustomerId> machineWithCust) {
		final List<CustomerInfo> custInfoList = new LinkedList<>();
		if (null != machineWithCust) {
			for (final MachineWithCustomerId custWithCount : machineWithCust) {
				final CustomerInfo custInfo = new CustomerInfo();
				custInfo.setMachineCount(custWithCount.getMachineCount());
				custInfo.setCustomerId(custWithCount.getCustomerId());
				final Customer cust = custRepo.findById(custWithCount.getCustomerId()).get();
				custInfo.setAddress(cust.getAddress());
				custInfo.setCountry("-");
				custInfo.setCustomerName(cust.getName());
				custInfo.setPhoneNumber(cust.getPhonenumber());
				custInfo.setThumbnail("");
				final User customerUser = userRepository.findByUserName(custWithCount.getCustomerId());
				if (customerUser != null) {
					custInfo.setThumbnail(customerUser.getThumbnail());
					custInfo.setCountry(customerUser.getCountry());
				}
				custInfoList.add(custInfo);
			}
		}
		dist.setCustomerInfo(custInfoList);
		return dist;
	}
	
	private DistributionParams getEventLevelGroupByCustomer(String userName, String tuple, String paramName,
			String search, int pageNumber, int pageSize) {
		final DistributionParams dist = new DistributionParams();
		if ("eventLevel".equals(paramName)) {
			final List<MachineWithCustomerId> machineWithCust = alertRepository.getByCountWithCustomers(userName,
					EventLevel.valueOf(tuple), PageRequest.of(pageNumber, pageSize));
			loadCustomerInMachineWithCID(dist, machineWithCust);
		} else {
			final List<MachineWithCustomerId> machineWithCust = alertRepository.getByCountWithCustomers(userName,
					EventLevel.valueOf(tuple), paramName, PageRequest.of(pageNumber, pageSize));
			loadCustomerInMachineWithCID(dist, machineWithCust);
		}
		return dist;
	}
	
	private Long getMachineCountByServiceStatus(String userName, String keyParam) {
		final ServiceCategory serviceCat = ServiceCategory.valueOf(keyParam);
        return switch (serviceCat) {
            case SERVICE_OVERDUE -> genericeQueryRepo.getMachineServiceOverDueCount(userName, true);
            case SERVICE_DUE -> genericeQueryRepo.getMachineServiceDueCount(userName, true);
            case SERVICE_NORMAL -> genericeQueryRepo.getMachineServicNormalCount(userName, true);
            case NO_DATA_AVAILABLE -> genericeQueryRepo.getMachineServiceNoDataCount(userName, true);
            default -> 0L;
        };
	}
	
	private List<DistributionParams> getByServiceStatus(String userName, Long machineCount, String serviceStatus,
			String search, int pageNumber, int pageSize) {
		final List<DistributionParams> distributons = new ArrayList<DistributionParams>();
		if ("ALL".equals(serviceStatus)) {
			
			DistributionParams distDue = new DistributionParams();
			distDue = getCustomerForServiceStatus(userName, ServiceCategory.SERVICE_DUE, serviceStatus, search, pageNumber, pageSize);
			distDue.setCategory(ServiceCategory.SERVICE_DUE.getName());
			distDue.setMachineCount(genericeQueryRepo.getMachineServiceDueCount(userName, true));
			distributons.add(distDue);
			
			DistributionParams distOverDue = new DistributionParams();
			distOverDue = getCustomerForServiceStatus(userName, ServiceCategory.SERVICE_OVERDUE, serviceStatus, search, pageNumber, pageSize);
			distOverDue.setCategory(ServiceCategory.SERVICE_OVERDUE.getName());
			distOverDue.setMachineCount(genericeQueryRepo.getMachineServiceOverDueCount(userName, true));
			distributons.add(distOverDue);
			
			DistributionParams distNormal = new DistributionParams();
			distNormal = getCustomerForServiceStatus(userName, ServiceCategory.SERVICE_NORMAL, serviceStatus, search, pageNumber, pageSize);
			distNormal.setCategory(ServiceCategory.SERVICE_NORMAL.getName());
			distNormal.setMachineCount(genericeQueryRepo.getMachineServicNormalCount(userName, true));
			distributons.add(distNormal);
			
			DistributionParams distNoData = new DistributionParams();
			distNoData = getCustomerForServiceStatus(userName, ServiceCategory.NO_DATA_AVAILABLE, serviceStatus, search, pageNumber, pageSize);
			distNoData.setCategory(ServiceCategory.NO_DATA_AVAILABLE.getName());
			distNoData.setMachineCount(genericeQueryRepo.getMachineServiceNoDataCount(userName, true));
			distributons.add(distNoData);
		} else {
			final ServiceCategory serviceCat = ServiceCategory.valueOf(serviceStatus);
			List<Tuple> tuples = null;
			switch (serviceCat) {
			case SERVICE_OVERDUE:
				DistributionParams distOverDue = new DistributionParams();
				tuples = genericeQueryRepo.countByServiceOverDueWithPlatform(userName, search);
				for (final Tuple tuple : tuples) {
					distOverDue = getCustomerForServiceStatus(userName, ServiceCategory.SERVICE_OVERDUE,
							tuple.get(0).toString(), search, pageNumber, pageSize);
					distOverDue.setCategory(tuple.get(0).toString());
					distOverDue.setMachineCount(Long.valueOf(tuple.get(1).toString()));
					distributons.add(distOverDue);
				}
				break;
			case SERVICE_DUE:
				DistributionParams distDue = new DistributionParams();
				tuples = genericeQueryRepo.countByServiceDueWithPlatform(userName, search);
				for (final Tuple tuple : tuples) {
					distDue = getCustomerForServiceStatus(userName, ServiceCategory.SERVICE_DUE,
							tuple.get(0).toString(), search, pageNumber, pageSize);
					distDue.setCategory(tuple.get(0).toString());
					distDue.setMachineCount(Long.valueOf(tuple.get(1).toString()));
					distributons.add(distDue);
				}
				break;
			case SERVICE_NORMAL:
				DistributionParams distNormal = new DistributionParams();
				tuples = genericeQueryRepo.countByServiceNormalWithPlatform(userName, search);
				for (final Tuple tuple : tuples) {
					distNormal = getCustomerForServiceStatus(userName, ServiceCategory.SERVICE_NORMAL,
							tuple.get(0).toString(), search, pageNumber, pageSize);
					distNormal.setCategory(tuple.get(0).toString());
					distNormal.setMachineCount(Long.valueOf(tuple.get(1).toString()));
					distributons.add(distNormal);
				}
				break;
			case NO_DATA_AVAILABLE:
				tuples = genericeQueryRepo.countByServiceNoDataWithPlatform(userName, search);
				for (final Tuple tuple : tuples) {
					DistributionParams distNoData = new DistributionParams();
					distNoData = getCustomerForServiceStatus(userName, ServiceCategory.NO_DATA_AVAILABLE,
							tuple.get(0).toString(), search, pageNumber, pageSize);
					distNoData.setCategory(tuple.get(0).toString());
					distNoData.setMachineCount(Long.valueOf(tuple.get(1).toString()));
					distributons.add(distNoData);
				}
				break;
			}
		}
		return distributons;
	}
	
	private DistributionParams getCustomerForServiceStatus(String userName, ServiceCategory category, String platform,
			String search, int pageNumber, int pageSize) {
		List<Tuple> custs = null;
		if ("ALL".equals(platform)) {
            custs = switch (category) {
                case SERVICE_OVERDUE ->
                        genericeQueryRepo.getMachineServiceStatusOverDueByCustomer(userName, search, pageNumber, pageSize);
                case SERVICE_DUE ->
                        genericeQueryRepo.getMachineServiceStatusDueByCustomer(userName, search, pageNumber, pageSize);
                case SERVICE_NORMAL ->
                        genericeQueryRepo.getMachineServiceStatusNormalByCustomer(userName, search, pageNumber, pageSize);
                case NO_DATA_AVAILABLE ->
                        genericeQueryRepo.getMachineServiceStatusNoDataByCustomer(userName, search, pageNumber, pageSize);
            };
		} else {
            custs = switch (category) {
                case SERVICE_OVERDUE ->
                        genericeQueryRepo.getCustomerForServiceOverDueByPlatform(userName, platform, search, pageNumber, pageSize);
                case SERVICE_DUE ->
                        genericeQueryRepo.getCustomerForServiceDueByPlatform(userName, platform, search, pageNumber, pageSize);
                case SERVICE_NORMAL ->
                        genericeQueryRepo.getCustomerForServiceNormalByPlatform(userName, platform, search, pageNumber, pageSize);
                case NO_DATA_AVAILABLE ->
                        genericeQueryRepo.getCustomerForServiceNoDataByPlatform(userName, platform, search, pageNumber, pageSize);
            };
		}
		return loadCustomerDetails(custs);
	}
	
	private DistributionParams loadCustomerDetails(List<Tuple> custs) {
		final DistributionParams dist = new DistributionParams();
		final List<CustomerInfo> custInfoList = new ArrayList<>();
		for (final Tuple tuple : custs) {
			final CustomerInfo custInfo = new CustomerInfo();
			custInfo.setMachineCount(Long.valueOf(tuple.get(0).toString()));
			custInfo.setCustomerId(tuple.get(1).toString());
			final Customer cust = custRepo.findById(tuple.get(1).toString()).get();
			custInfo.setAddress(cust.getAddress());
			custInfo.setCountry("-");
			custInfo.setCustomerName(cust.getName());
			custInfo.setPhoneNumber(cust.getPhonenumber());
			custInfo.setThumbnail("");
			final User customerUser = userRepository.findByUserName(tuple.get(1).toString());
			if (customerUser != null) {
				custInfo.setThumbnail(customerUser.getThumbnail());
				custInfo.setCountry(customerUser.getCountry());
			}
			custInfoList.add(custInfo);
		}
		dist.setCustomerInfo(custInfoList);
		return dist;
	}
	
	private List<DistributionParams> getAllUtilizationData(int pageNumber, int pageSize) {

		List<DistributionParams> distributionParams = new LinkedList<>();

		for (MachineUtilizationCategory utilizationCategory : MachineUtilizationCategory.values()) {
			List<CustomerInfo> customerInfo = new LinkedList<>();
			for (UtilizationLegendWiseCustomerMachineCount utilizationLegendWiseCustomerMachineCount : utilizationLegendWiseCustomerMachineCountRepository
					.findByUtilizationCategoryOrderByMachineCountDescCustomerIdAsc(utilizationCategory, PageRequest.of(pageNumber, pageSize))) {
				String customerId = utilizationLegendWiseCustomerMachineCount.getCustomerId();
				User user = userRepository.findByUserName(customerId);
				Customer cust = utilizationLegendWiseCustomerMachineCount.getCustomer();
				customerInfo.add(new CustomerInfo(customerId, user != null ? user.getThumbnail() : "", cust.getName(),
						cust.getPhonenumber(), cust.getAddress(), user != null ? user.getCountry() : "-",
						utilizationLegendWiseCustomerMachineCount.getMachineCount()));
			}
			if(utilizationLegendWiseMachineCountRepository.findByUtilizationCategory(utilizationCategory)!=null) {
				distributionParams.add(new DistributionParams(
						Long.valueOf(utilizationLegendWiseMachineCountRepository.findByUtilizationCategory(utilizationCategory).getMachineCount()),
						utilizationCategory.getName(), customerInfo));
			}
		}
		return distributionParams;
	}
	
	private List<DistributionParams> getLegendWiseUtilizationData(MachineUtilizationCategory utilizationCategory,
			int pageNumber, int pageSize) {

		List<DistributionParams> distributionParams = new LinkedList<>();
		log.info("getLegendWiseUtilizationData: get legend wise platform list");
		log.info("getLegendWiseUtilizationData: platform list: " + utilizationPlatformWiseMachineCountRepository
				.findDistinctPlatformByUtilizationCategoryOrderByMachineCountDesc(utilizationCategory));
		for (UtilizationPlatformWiseMachineCount platform : utilizationPlatformWiseMachineCountRepository
				.findDistinctPlatformByUtilizationCategoryOrderByMachineCountDesc(utilizationCategory)) {
			List<CustomerInfo> customerInfo = new LinkedList<>();
			for (UtilizationPlatformWiseCustomerMachineCount platformWiseCustomerMachineCount : utilizationPlatformWiseCustomerMachineCountRepository
					.findByUtilizationCategoryAndPlatformOrderByMachineCountDescCustomerIdAsc(utilizationCategory,
							platform.getPlatform(), PageRequest.of(pageNumber, pageSize))) {
				String customerId = platformWiseCustomerMachineCount.getCustomerId();
				User user = userRepository.findByUserName(customerId);
				Customer customer = platformWiseCustomerMachineCount.getCustomer();
				customerInfo.add(new CustomerInfo(customerId, user != null ? user.getThumbnail() : "",
						customer.getName(), customer.getPhonenumber(), customer.getAddress(),
						user != null ? user.getCountry() : "-", platformWiseCustomerMachineCount.getMachineCount()));
			}
			distributionParams
					.add(new DistributionParams(platform.getMachineCount(), platform.getPlatform(), customerInfo));

		}

		return distributionParams;

	}
	
	private Long getMachineCountByUtilizationStatus(String userName, String keyParam) {
		if ("HEAVILY_USED".equals(keyParam)) {
			return genericeQueryRepo.getUtilizationData(userName, "HeavilyUsed", true);
		}
		if ("MODERATED_USED".equals(keyParam)) {
			return genericeQueryRepo.getUtilizationData(userName, "ModerateUsed", true);
		}
		if ("LESSER_USED".equals(keyParam)) {
			return genericeQueryRepo.getUtilizationData(userName, "LesserUsed", true);
		}
		if ("NO_DATA_AVAILABLE".equals(keyParam)) {
			return (long) genericeQueryRepo.getUtilizationNoData(userName, true).size();
		}
		return 0L;
	}
	
	private List<DistributionParams> getByUtilizationStatus(String userName, Long machineCount, String utilization,
			String search, int pageNumber, int pageSize) throws ProcessCustomError {
		final Date startDate = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
		final Date endDate = utilities.getDate(utilities.getEndDate(1));
		long days = (endDate.getTime() - startDate.getTime()) / 86400000L;
		List<MachineWithCustomerId> machineCust = null;
		DistributionParams dist = null;
		List<MachineWithPlatform> platformList = null;
		final List<DistributionParams> distributons = new ArrayList<DistributionParams>();
		try {
			if ("ALL".equals(utilization)) {
				DistributionParams distLesser = new DistributionParams();
				distLesser = getCustomerForMachineUtilization(userName, MachineUtilizationCategory.LESSER_USED,
						utilization, search, pageNumber, pageSize);
				distLesser.setCategory(MachineUtilizationCategory.LESSER_USED.getName());
				distLesser.setMachineCount(
						getMachineCountByUtilizationStatus(userName, MachineUtilizationCategory.LESSER_USED.getName()));
				distributons.add(distLesser);
				DistributionParams distHeavy = new DistributionParams();
				distHeavy = getCustomerForMachineUtilization(userName, MachineUtilizationCategory.HEAVILY_USED,
						utilization, search, pageNumber, pageSize);
				distHeavy.setCategory(MachineUtilizationCategory.HEAVILY_USED.getName());
				distHeavy.setMachineCount(getMachineCountByUtilizationStatus(userName,
						MachineUtilizationCategory.HEAVILY_USED.getName()));
				distributons.add(distHeavy);
				DistributionParams distModerate = new DistributionParams();
				distModerate = getCustomerForMachineUtilization(userName, MachineUtilizationCategory.MODERATED_USED,
						utilization, search, pageNumber, pageSize);
				distModerate.setCategory(MachineUtilizationCategory.MODERATED_USED.getName());
				distModerate.setMachineCount(getMachineCountByUtilizationStatus(userName,
						MachineUtilizationCategory.MODERATED_USED.getName()));
				distributons.add(distModerate);
				DistributionParams distNoData = new DistributionParams();
				distNoData = getCustomerForMachineUtilization(userName, MachineUtilizationCategory.NO_DATA_AVAILABLE,
						utilization, search, pageNumber, pageSize);
				distNoData.setCategory(MachineUtilizationCategory.NO_DATA_AVAILABLE.getName());
				distNoData.setMachineCount(getMachineCountByUtilizationStatus(userName,
						MachineUtilizationCategory.NO_DATA_AVAILABLE.getName()));
				distributons.add(distNoData);
			} else {
				if (MachineUtilizationCategory.LESSER_USED.getName().equals(utilization)) {
					platformList = machineUtilizationRepo.getplatformsByMachineUsageForDuration(userName, startDate,
							endDate, Double.valueOf(0), Double.valueOf(lesserUsedMaxRange * days));
					for (final MachineWithPlatform platform : platformList) {
						machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate,
								endDate, Double.valueOf(0), Double.valueOf(lesserUsedMaxRange * days), platform.getPlatform(),
								PageRequest.of(pageNumber, pageSize));
						dist = new DistributionParams();
						dist.setMachineCount(platform.getMachineCount());
						dist.setCategory(platform.getPlatform());
						loadCustomerInMachineWithCID(dist, machineCust);
						distributons.add(dist);
					}
				}
				if (MachineUtilizationCategory.HEAVILY_USED.getName().equals(utilization)) {
					double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
					platformList = machineUtilizationRepo.getplatformsByMachineUsageForDuration(userName, startDate,
							endDate, heavilyUsedMin, Double.valueOf(heavilyUsedMaxRange * days));
					for (final MachineWithPlatform platform : platformList) {
						machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate,
								endDate, heavilyUsedMin, Double.valueOf(heavilyUsedMaxRange * days), platform.getPlatform(),
								PageRequest.of(pageNumber, pageSize));
						dist = new DistributionParams();
						dist.setMachineCount(platform.getMachineCount());
						dist.setCategory(platform.getPlatform());
						loadCustomerInMachineWithCID(dist, machineCust);
						distributons.add(dist);
					}
				}
				if (MachineUtilizationCategory.MODERATED_USED.getName().equals(utilization)) {
					double moderateUsedMin = (lesserUsedMaxRange * days) + 1D;
					platformList = machineUtilizationRepo.getplatformsByMachineUsageForDuration(userName, startDate,
							endDate, moderateUsedMin, Double.valueOf(moderateUsedMaxRange * days));
					for (final MachineWithPlatform platform : platformList) {
						machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate,
								endDate, moderateUsedMin, Double.valueOf(moderateUsedMaxRange * days),
								platform.getPlatform(), PageRequest.of(pageNumber, pageSize));
						dist = new DistributionParams();
						dist.setMachineCount(platform.getMachineCount());
						dist.setCategory(platform.getPlatform());
						loadCustomerInMachineWithCID(dist, machineCust);
						distributons.add(dist);
					}
				}
				if (MachineUtilizationCategory.NO_DATA_AVAILABLE.getName().equals(utilization)) {
					platformList = machineUtilizationRepo.getplatformsByUnusedMachineForDuration(userName, startDate,
							endDate);
					for (final MachineWithPlatform platform : platformList) {
						machineCust = machineUtilizationRepo.getCustomersByUnusedMachineForDuration(userName, startDate,
								endDate, platform.getPlatform(), PageRequest.of(pageNumber, pageSize));
						dist = new DistributionParams();
						dist.setMachineCount(platform.getMachineCount());
						dist.setCategory(platform.getPlatform());
						loadCustomerInMachineWithCID(dist, machineCust);
						distributons.add(dist);
					}
				}
			}
		} catch (final Exception ex) {
			log.error("Failed to process request " + ex.getMessage());
			ex.printStackTrace();
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return distributons;
	}
	
	private DistributionParams getCustomerForMachineUtilization(String userName, MachineUtilizationCategory category,
			String platform, String search, int pageNumber, int pageSize) {
		final Date startDate = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
		final Date endDate = utilities.getDate(utilities.getEndDate(1));
		long days = (endDate.getTime() - startDate.getTime()) / 86400000L;
		List<MachineWithCustomerId> machineCust = null;
		if ("ALL".equals(platform)) {
			if (MachineUtilizationCategory.LESSER_USED.getName().equals(category.getName())) {
				machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        (double) 0, (double) (lesserUsedMaxRange * days), PageRequest.of(pageNumber, pageSize));
			}
			if (MachineUtilizationCategory.HEAVILY_USED.getName().equals(category.getName())) {
				double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
				machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
						heavilyUsedMin, (double) (heavilyUsedMaxRange * days), PageRequest.of(pageNumber, pageSize));
			}
			if (MachineUtilizationCategory.MODERATED_USED.getName().equals(category.getName())) {
				double modarateUsedMin = (lesserUsedMaxRange * days) + 1D;
				machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
						modarateUsedMin, Double.valueOf(moderateUsedMaxRange * days),
						PageRequest.of(pageNumber, pageSize));
			}
			if (MachineUtilizationCategory.NO_DATA_AVAILABLE.getName().equals(category.getName())) {
				machineCust = machineUtilizationRepo.getCustomersByUnusedMachineForDuration(userName, startDate,
						endDate, PageRequest.of(pageNumber, pageSize));
			}
		} else {
			if (MachineUtilizationCategory.LESSER_USED.getName().equals(category.getName())) {
				machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
						Double.valueOf(0), Double.valueOf(lesserUsedMaxRange * days), platform,
						PageRequest.of(pageNumber, pageSize));
			}
			if (MachineUtilizationCategory.HEAVILY_USED.getName().equals(category.getName())) {
				double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
				machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
						heavilyUsedMin, Double.valueOf(heavilyUsedMaxRange), platform,
						PageRequest.of(pageNumber, pageSize));
			}
			if (MachineUtilizationCategory.MODERATED_USED.getName().equals(category.getName())) {
				double modarateUsedMin = (lesserUsedMaxRange * days) + 1D;
				machineCust = machineUtilizationRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
						modarateUsedMin, Double.valueOf(moderateUsedMaxRange), platform,
						PageRequest.of(pageNumber, pageSize));
			}
			if (MachineUtilizationCategory.NO_DATA_AVAILABLE.getName().equals(category.getName())) {
				machineCust = machineUtilizationRepo.getCustomersByUnusedMachineForDuration(userName, startDate,
						endDate, platform, PageRequest.of(pageNumber, pageSize));
			}
		}
		final DistributionParams dist = new DistributionParams();
		return loadCustomerInMachineWithCID(dist, machineCust);
	}
	
	private Long getMachineCountByMachineLocater(String userName, String keyParam) {
		final TransitMode modeOfTransit = TransitMode.valueOf(keyParam);
		switch (modeOfTransit) {
		case TRANSIT:
			return genericeQueryRepo.countByMachineLocater(userName, 0);
		case NORMAL:
			return genericeQueryRepo.countByMachineLocater(userName, 1);
		default:
			return 0L;
		}
	}

}
