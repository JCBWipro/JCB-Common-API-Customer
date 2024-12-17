package com.wipro.jcb.livelink.app.machines.service.impl;

import com.google.gson.Gson;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.config.UserMappingThread;
import com.wipro.jcb.livelink.app.machines.constants.*;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerDistribution;
import com.wipro.jcb.livelink.app.machines.dealer.response.CustomerInfo;
import com.wipro.jcb.livelink.app.machines.dealer.response.DistributionParams;
import com.wipro.jcb.livelink.app.machines.dto.*;
import com.wipro.jcb.livelink.app.machines.entity.*;
import com.wipro.jcb.livelink.app.machines.enums.*;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.*;
import com.wipro.jcb.livelink.app.machines.service.DealerDashBoardDetailsCacheService;
import com.wipro.jcb.livelink.app.machines.service.DealerDashboardResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class DealerDashboardResponseServiceImpl implements DealerDashboardResponseService {

    @Value("${machine.dealerjcbnoncommunicatingdate}")
    int nonCommunicatingDate;

    @Value("${machine.lesser.used.max.range}")
    int lesserUsedMaxRange;

    @Value("${machine.moderate.used.max.range}")
    int moderateUsedMaxRange;

    @Value("${machine.havily.used.max.range}")
    int heavilyUsedMaxRange;

    @Value("${machine.utilization.day}")
    int machineUtilizationDay;

    @Value("${admin.userName}")
    String adminUserName;

    @Value("${livelinkserver.resttemplateurl}")
    String restTemplateUrl;

    @Value("${server.evn.baseurl}")
    String env;

    @Autowired
    DealerRepository dealerRepo;

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    Utilities utilities;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DealerDashboardRepo dealerDashboardRepo;

    @Autowired
    CustomerRepository custRepo;

    @Autowired
    GenericeQueryRepo genericeQueryRepo;

    @Autowired
    DashboardDataRepository dashboardDataRepository;

    @Autowired
    MachineRepository machineRepository;

    @Autowired
    DDashboardCustomerRepo dealerDashboardCustomerRepo;

    @Autowired
    DDCustomerPlatformRepo ddCustomerPlatformRepo;

    @Autowired
    DealerDashBoardDetailsCacheService dealerDashBoardDetailsCacheService;

    @Autowired
    UtilizationLegendWiseCustomerMachineCountRepository utilizationLegendWiseCustomerMachineCountRepository;

    @Autowired
    UtilizationLegendWiseMachineCountRepository utilizationLegendWiseMachineCountRepository;

    @Autowired
    UtilizationPlatformWiseCustomerMachineCountRepository utilizationPlatformWiseCustomerMachineCountRepository;

    @Autowired
    UtilizationPlatformWiseMachineCountRepository utilizationPlatformWiseMachineCountRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");

    public static final String PLATFORM_PARAM_NAME = "platform";
    public static final String MODEL_PARAM_NAME = "model";

    private static final String TRANSIT_PARAM_NAME = "transitMode";
    public static final String CUSTOMER_PARAM_NAME = "customer";

    private static final int DEFAULT_COUNT = 100;
    private static final long MACHINE_COUNT_DUMMY = 200;
    private static final Integer CUSTOMER_LIST_START = 0;
    private static final Integer CUSTOMER_LIST_SIZE = 5;

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
                    custDistribution.setMachineCount(dealerRepo.countByUsersUserNamePlatform(userName, keyParam));
                }

                if (user.getUserType() == UserType.JCB) {
                    log.info("Platform Details for Admin User(JCB)-" + keyParam);
                    if (pageNumber == 0 || pageNumber == 1) {
                        String platformKey = dateFormat.format(new Date()) + "JCB_PLATFORM_" + keyParam + "_" + pageNumber;
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
                            CustomerDistribution returnData = gsonObj.fromJson(data.getData(), CustomerDistribution.class);
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
            log.info("DashboardDetails API Duration :" + elapsedTime + "-" + userName + "-" + distributor + "-" + keyParam);
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
                machineCust = dealerRepo.getByAllCustomerForRenewalOverDue(userName, today, communicatingDate, PageRequest.of(pageNumber, pageSize));
                dist = new DistributionParams();
                dist.setMachineCount(dealerRepo.getCountByRenewalOverDueWithCustomers(userName, today, communicatingDate));
                dist.setCategory(LivelinkRenawal.RENEWAL_OVERDUE.getName());
                distributions.add(loadCustomerInMachineWithCID(dist, machineCust));

                // RENEWAL_APPROACHING
                machineCust = dealerRepo.getByAllCustomerForRenewalApproaching(userName, maxRenewalDate, communicatingDate, PageRequest.of(pageNumber, pageSize));
                dist = new DistributionParams();
                dist.setMachineCount(dealerRepo.getCountByRenewalApproachingWithCustomers(userName, maxRenewalDate, communicatingDate));
                dist.setCategory(LivelinkRenawal.RENEWAL_APPROACHING.getName());
                distributions.add(loadCustomerInMachineWithCID(dist, machineCust));

                // RENEWAL_IMMEDIATE
                machineCust = dealerRepo.getByAllCustomerForRenewalImmediate(userName, today, maxRenewalDate, communicatingDate, PageRequest.of(pageNumber, pageSize));
                dist = new DistributionParams();
                dist.setMachineCount(dealerRepo.getCountByRenewalImmediateWithCustomers(userName, today, maxRenewalDate, communicatingDate));
                dist.setCategory(LivelinkRenawal.RENEWAL_IMMEDIATE.getName());
                distributions.add(loadCustomerInMachineWithCID(dist, machineCust));

                // RENEWAL_NO_DATA
                machineCust = dealerRepo.getByAllCustomerForRenewalNoData(userName, communicatingDate, PageRequest.of(pageNumber, pageSize));
                dist = new DistributionParams();
                dist.setMachineCount(dealerRepo.getCountByRenewalNoDataWithCustomers(userName, communicatingDate));
                dist.setCategory(LivelinkRenawal.RENEWAL_NO_DATA.getName());
                distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
                break;

            case RENEWAL_OVERDUE:
                final List<MachineWithPlatform> platformListRO = dealerRepo.getByPlatformForRenewalOverDue(userName, today,
                        communicatingDate);
                for (final MachineWithPlatform platform : platformListRO) {
                    machineCust = dealerRepo.getByAllCustomerForRenewalOverDue(userName, today, platform.getPlatform(),
                            communicatingDate, PageRequest.of(pageNumber, pageSize));
                    dist = new DistributionParams();
                    dist.setMachineCount(platform.getMachineCount());
                    dist.setCategory(platform.getPlatform());
                    distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
                }
                break;
            case RENEWAL_APPROACHING:
                final List<MachineWithPlatform> platformListRA = dealerRepo.getByPlatformForRenewalApproaching(userName,
                        maxRenewalDate, communicatingDate);
                for (final MachineWithPlatform platform : platformListRA) {
                    machineCust = dealerRepo.getByAllCustomerForRenewalApproaching(userName, maxRenewalDate,
                            platform.getPlatform(), communicatingDate, PageRequest.of(pageNumber, pageSize));
                    dist = new DistributionParams();
                    dist.setMachineCount(platform.getMachineCount());
                    dist.setCategory(platform.getPlatform());
                    distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
                }
                break;
            case RENEWAL_IMMEDIATE:
                final List<MachineWithPlatform> platformListRI = dealerRepo.getByPlatformForRenewalImmediate(userName,
                        today, maxRenewalDate, communicatingDate);
                for (final MachineWithPlatform platform : platformListRI) {
                    machineCust = dealerRepo.getByAllCustomerForRenewalImmediate(userName, today, maxRenewalDate,
                            platform.getPlatform(), communicatingDate, PageRequest.of(pageNumber, pageSize));
                    dist = new DistributionParams();
                    dist.setMachineCount(platform.getMachineCount());
                    dist.setCategory(platform.getPlatform());
                    distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
                }
                break;
            case RENEWAL_NO_DATA:
                final List<MachineWithPlatform> platformListRN = dealerRepo.getByPlatformForRenewalNoData(userName,
                        communicatingDate);
                for (final MachineWithPlatform platform : platformListRN) {
                    machineCust = dealerRepo.getByAllCustomerForRenewalNoData(userName, platform.getPlatform(),
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
            case RENEWAL_OVERDUE ->
                    dealerRepo.getCountByRenewalOverDueWithCustomers(userName, today, communicatingDate);
            case RENEWAL_APPROACHING ->
                    dealerRepo.getCountByRenewalApproachingWithCustomers(userName, maxRenewalDate, communicatingDate);
            case RENEWAL_IMMEDIATE ->
                    dealerRepo.getCountByRenewalImmediateWithCustomers(userName, today, maxRenewalDate, communicatingDate);
            case RENEWAL_NO_DATA -> dealerRepo.getCountByRenewalNoDataWithCustomers(userName, communicatingDate);
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
        final Date date = utilities.getStartDateTimeInDateFormat(nonCommunicatingDate);
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
        final Date date = utilities.getStartDateTimeInDateFormat(nonCommunicatingDate);
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
        final Date date = utilities.getStartDateTimeInDateFormat(nonCommunicatingDate);
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
            case RED -> dealerRepo.countMachinesByCriticalAlertsCustomer(userName, EventLevel.RED);
            case YELLOW -> dealerRepo.countMachinesByHighAlertsCustomer(userName, EventLevel.RED, EventLevel.YELLOW);
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
            machineCust = dealerRepo.getMachinesByCriticalAlertsCustomer(userName, EventLevel.RED, PageRequest.of(pageNumber, pageSize));
            dist = new DistributionParams();
            dist.setMachineCount(dealerRepo.countMachinesByCriticalAlertsCustomer(userName, EventLevel.RED));
            dist.setCategory(EventLevel.RED.toString());
            distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
            machineCust = dealerRepo.getMachinesByCriticalAlertsCustomer(userName, EventLevel.YELLOW, PageRequest.of(pageNumber, pageSize));
            dist = new DistributionParams();
            dist.setMachineCount(dealerRepo.countMachinesByCriticalAlertsCustomer(userName, EventLevel.YELLOW));
            dist.setCategory(EventLevel.YELLOW.toString());
            distributions.add(loadCustomerInMachineWithCID(dist, machineCust));
        } else {
            list = dealerRepo.getAlertCountGroupByPlatform(userName, EventLevel.valueOf(alertLevel));
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
            final List<MachineWithCustomerId> machineWithCust = dealerRepo.getByCountWithCustomers(userName,
                    EventLevel.valueOf(tuple), PageRequest.of(pageNumber, pageSize));
            loadCustomerInMachineWithCID(dist, machineWithCust);
        } else {
            final List<MachineWithCustomerId> machineWithCust = dealerRepo.getByCountWithCustomers(userName,
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
            if (utilizationLegendWiseMachineCountRepository.findByUtilizationCategory(utilizationCategory) != null) {
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
                    platformList = dealerRepo.getplatformsByMachineUsageForDuration(userName, startDate,
                            endDate, Double.valueOf(0), Double.valueOf(lesserUsedMaxRange * days));
                    for (final MachineWithPlatform platform : platformList) {
                        machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate,
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
                    platformList = dealerRepo.getplatformsByMachineUsageForDuration(userName, startDate,
                            endDate, heavilyUsedMin, Double.valueOf(heavilyUsedMaxRange * days));
                    for (final MachineWithPlatform platform : platformList) {
                        machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate,
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
                    platformList = dealerRepo.getplatformsByMachineUsageForDuration(userName, startDate,
                            endDate, moderateUsedMin, Double.valueOf(moderateUsedMaxRange * days));
                    for (final MachineWithPlatform platform : platformList) {
                        machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate,
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
                    platformList = dealerRepo.getplatformsByUnusedMachineForDuration(userName, startDate,
                            endDate);
                    for (final MachineWithPlatform platform : platformList) {
                        machineCust = dealerRepo.getCustomersByUnusedMachineForDuration(userName, startDate,
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
                machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        (double) 0, (double) (lesserUsedMaxRange * days), PageRequest.of(pageNumber, pageSize));
            }
            if (MachineUtilizationCategory.HEAVILY_USED.getName().equals(category.getName())) {
                double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
                machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        heavilyUsedMin, (double) (heavilyUsedMaxRange * days), PageRequest.of(pageNumber, pageSize));
            }
            if (MachineUtilizationCategory.MODERATED_USED.getName().equals(category.getName())) {
                double modarateUsedMin = (lesserUsedMaxRange * days) + 1D;
                machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        modarateUsedMin, Double.valueOf(moderateUsedMaxRange * days),
                        PageRequest.of(pageNumber, pageSize));
            }
            if (MachineUtilizationCategory.NO_DATA_AVAILABLE.getName().equals(category.getName())) {
                machineCust = dealerRepo.getCustomersByUnusedMachineForDuration(userName, startDate,
                        endDate, PageRequest.of(pageNumber, pageSize));
            }
        } else {
            if (MachineUtilizationCategory.LESSER_USED.getName().equals(category.getName())) {
                machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        Double.valueOf(0), Double.valueOf(lesserUsedMaxRange * days), platform,
                        PageRequest.of(pageNumber, pageSize));
            }
            if (MachineUtilizationCategory.HEAVILY_USED.getName().equals(category.getName())) {
                double heavilyUsedMin = (moderateUsedMaxRange * days) + 1D;
                machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        heavilyUsedMin, Double.valueOf(heavilyUsedMaxRange), platform,
                        PageRequest.of(pageNumber, pageSize));
            }
            if (MachineUtilizationCategory.MODERATED_USED.getName().equals(category.getName())) {
                double modarateUsedMin = (lesserUsedMaxRange * days) + 1D;
                machineCust = dealerRepo.getCustomersByMachineUsageForDuration(userName, startDate, endDate,
                        modarateUsedMin, Double.valueOf(moderateUsedMaxRange), platform,
                        PageRequest.of(pageNumber, pageSize));
            }
            if (MachineUtilizationCategory.NO_DATA_AVAILABLE.getName().equals(category.getName())) {
                machineCust = dealerRepo.getCustomersByUnusedMachineForDuration(userName, startDate,
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

    @Override
    public DealerDashboard getDealerDashboardResponse(String userName, String search, String type)
            throws ProcessCustomError {
        long start = System.currentTimeMillis();
        String registerKey;
        try {
            DealerDashboard dealerDashboard;
            final User user = userRepository.findByUserName(userName);

            // Fetching subscribed count, handling potential null result
            DealerDashboardData subscribedCountData = dealerDashboardRepo
                    .findByUserNameAndGraphTypeAndCategory(adminUserName, "CONNECTIVITY", "SUBSCRIBED_COUNT");
            int subscribedCount = (subscribedCountData != null) ? subscribedCountData.getMachineCount() : 0;

            if (user != null && user.getUserType() == UserType.JCB) {
                try {
                    UserMappingThread thread = new UserMappingThread(restTemplateUrl, env, userName);
                    Thread runner = new Thread(thread);
                    runner.start();
                } catch (final Exception exception) {
                    log.error("Error while calling machine mapping API: {}", exception.getMessage());
                    throw new ProcessCustomError(MessagesList.SESSION_EXPIRED, MessagesList.SESSION_EXPIRED,
                            HttpStatus.UNAUTHORIZED);
                }
                log.info("Started the data process for Admin :{}", userName);
                registerKey = "JCB_" + dateFormat.format(new Date()) + "_" + type;
                log.info("Register Key: {}", registerKey);
                DealerDashboard details = dealerDashBoardDetailsCacheService.getDealerDashboardDetailsByUsername(registerKey);
                log.info("Details from Cache: {}", details);

                if (details == null || details.getPlatformDist().getKeys().isEmpty()) {
                    log.info("Data From DB");
                    dealerDashboard = getDashboardFromDB(adminUserName, type, subscribedCount, null, false);
                    log.info("DealerDashboard before caching: {}", dealerDashboard);
                    dealerDashBoardDetailsCacheService.setDealerDashboardDetailsByUsername(dealerDashboard, registerKey);
                    log.info("Data From DB and set into RedisCache.");
                } else {
                    log.info("Data From REDIS");
                    dealerDashboard = details;
                }
                if (dealerDashboard.getPlatformDist() != null) {
                    log.info("size {}", dealerDashboard.getPlatformDist().getKeys().size());
                    for (int i = 0; i < dealerDashboard.getPlatformDist().getKeys().size(); i++) {
                        switch (dealerDashboard.getPlatformDist().getKeys().get(i).getCategory()) {
                            case "Loadall" ->
                                    dealerDashboard.getPlatformDist().getKeys().get(i).setCategory("Telehandler");
                            case "Robot" ->
                                    dealerDashboard.getPlatformDist().getKeys().get(i).setCategory("Skid Steer Loader");
                            case "JCB30 +MINI EXC" ->
                                    dealerDashboard.getPlatformDist().getKeys().get(i).setCategory("Mini Excavator");
                            case "GENSET Power Products" ->
                                    dealerDashboard.getPlatformDist().getKeys().get(i).setCategory("Generator");
                            case "Front End Loader" ->
                                    dealerDashboard.getPlatformDist().getKeys().get(i).setCategory("Super Loader");
                        }
                    }

                }

                return dealerDashboard;

            } else {
                if (user != null && StringUtils.hasLength(user.getUserAppVersion())
                        && Integer.parseInt(user.getUserAppVersion().replace(".", "")) > 210) {
                    dealerDashboard = new DealerDashboard();
                    final Long machineCount = machineRepository.getCountByUsersUserName(userName);
                    if (machineCount != 0L) {
                        registerKey = userName + "_" + dateFormat.format(new Date()) + "_" + type;
                        log.info("Dealer Login {}", userName);
                        //Check DealerDetails in Cache
                        DealerDashboard details = dealerDashBoardDetailsCacheService.getDealerDashboardDetailsByUsername(registerKey);
                        if (details == null) {
                            populateDashBoardList(dealerDashboard, machineCount, userName, type, search);
                            dealerDashBoardDetailsCacheService.setDealerDashboardDetailsByUsername(dealerDashboard, registerKey);
                        } else {
                            dealerDashboard = details;
                        }

                    }
                } else {
                    dealerDashboard = getDashboardFromDB(adminUserName, type, subscribedCount, userName, true);
                }

            }
            log.info("getDealerDashboardResponse: End of dashboard Response for userName: {}", userName);
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("Dashboard API Duration :{}-{}-{}", userName, elapsedTime, type);
            return dealerDashboard;
        } catch (final Exception ex) {
            log.error("getDealerDashboardResponse: Failed to Process request with message {} User : {}",
                    ex.getMessage(), userName, ex);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.EXPECTATION_FAILED);
        }
    }

    private DealerDashboard getDashboardFromDB(String userName, String type, int subscribedCount, String dealerUserName,
                                               Boolean isDealer) {
        log.info("Entering getDashboardFromDB for user: {}, type: {}, isDealer: {}", userName, type, isDealer);
        final DealerDashboard dealerDBDashboard = new DealerDashboard();
        try {
            log.debug("Fetching dealerDashboardData for userName: {}, type: {}", userName, type);
            List<DealerDashboardData> dealerDashboardData = "optional".equals(type)
                    ? dealerDashboardRepo.findAll(userName)
                    : dealerDashboardRepo.findByGraphTypeAndUserName(type, userName);
            log.debug("Retrieved dealerDashboardData: {}", dealerDashboardData);

            DealerDashboardData machineCountAll;
            Long totalMachineCount;

            if (!"optional".equals(type)) {
                log.debug("Fetching machineCountAll for userName: {}, type:{}", userName, type);
                machineCountAll = dealerDashboardRepo.findByUserNameAndGraphTypeAndCategory(userName, "ALL", "ALL");
                log.debug("machineCountAll retrieved: {}", machineCountAll);

                // Handling the potential null value of machineCountAll
                if (machineCountAll != null) {
                    totalMachineCount = (long) machineCountAll.getMachineCount();
                    // Null checks before setting machine counts
                    if (dealerDBDashboard.getPlatformDist() != null) {
                        dealerDBDashboard.getPlatformDist().setMachineCount(totalMachineCount);
                    } else {
                        log.error("Unexpected error: getPlatformDist() returned null.");
                    }

                    if (dealerDBDashboard.getAlerts() != null) {
                        dealerDBDashboard.getAlerts().setMachineCount(totalMachineCount);
                    } else {
                        log.error("Unexpected error: getAlerts() returned null.");
                    }

                    if (dealerDBDashboard.getUtilization() != null) {
                        dealerDBDashboard.getUtilization().setMachineCount(totalMachineCount);
                    } else {
                        log.error("Unexpected error: getUtilization() returned null.");
                    }
                    if (dealerDBDashboard.getServiceStatus() != null) {
                        dealerDBDashboard.getServiceStatus().setMachineCount(totalMachineCount);
                    } else {
                        log.error("Unexpected error: getServiceStatus() returned null.");
                    }

                    if (dealerDBDashboard.getMachineLocator() != null) {
                        dealerDBDashboard.getMachineLocator().setMachineCount(totalMachineCount);
                    } else {
                        log.error("Unexpected error: getMachineLocator() returned null.");
                    }

                    if (dealerDBDashboard.getConnectivity() != null) {
                        dealerDBDashboard.getConnectivity().setMachineCount((long) subscribedCount);
                    } else {
                        log.error("Unexpected error: getConnectivity() returned null.");
                    }

                    if (dealerDBDashboard.getRenewalStatus() != null) {
                        dealerDBDashboard.getRenewalStatus().setMachineCount(totalMachineCount);
                    } else {
                        log.error("Unexpected error: getRenewalStatus() returned null.");
                    }

                    if (dealerDBDashboard.getTopCustomers() != null) {
                        dealerDBDashboard.getTopCustomers().setMachineCount(machineCountAll.getMachineCount());
                    } else {
                        log.error("Unexpected error: getTopCustomers() returned null.");
                    }
                    if ("WARRANTY".equals(type) && dealerDBDashboard.getWarranty() != null) {
                        dealerDBDashboard.setWarranty(getWarrantyData());
                        dealerDBDashboard.getWarranty().setMachineCount(totalMachineCount);
                    }
                } else {
                    log.warn("machineCountAll is null for userName: {}, type:{}. Using default totalMachineCount.", userName, type);
                }
            }

            log.info("Size dealerDashboardData: {}", dealerDashboardData.size());
            for (DealerDashboardData record : dealerDashboardData) {
                log.debug("Processing record: {}", record);
                CategoryMachineCountList inst = null;
                switch (DashboardGraph.valueOf(record.getGraphType())) {
                    case PLATFORM:
                        inst = dealerDBDashboard.getPlatformDist();
                        break;
                    case ALERT:
                        inst = dealerDBDashboard.getAlerts();
                        break;
                    case UTILIZATION:
                        inst = dealerDBDashboard.getUtilization();
                        break;
                    case SERVICE_STATUS:
                        inst = dealerDBDashboard.getServiceStatus();
                        break;
                    case MACHINE_LOCATER:
                        inst = dealerDBDashboard.getMachineLocator();
                        break;
                    case CONNECTIVITY:
                        inst = dealerDBDashboard.getConnectivity();
                        break;
                    case RENEWAL_STATUS:
                        inst = dealerDBDashboard.getRenewalStatus();
                        break;
                    case ALL:
                        dealerDBDashboard.getPlatformDist().setMachineCount((long) record.getMachineCount());
                        dealerDBDashboard.getAlerts().setMachineCount((long) record.getMachineCount());
                        dealerDBDashboard.getUtilization().setMachineCount((long) record.getMachineCount());
                        dealerDBDashboard.getServiceStatus().setMachineCount((long) record.getMachineCount());
                        dealerDBDashboard.getMachineLocator().setMachineCount((long) record.getMachineCount());
                        dealerDBDashboard.getConnectivity().setMachineCount((long) subscribedCount);
                        dealerDBDashboard.getRenewalStatus().setMachineCount((long) record.getMachineCount());
                        dealerDBDashboard.setWarranty(getWarrantyData());
                        dealerDBDashboard.getTopCustomers().setMachineCount(record.getMachineCount());
                        final List<String> platforms = machineRepository
                                .findDistinctPlatformForUsers(!isDealer ? userName : dealerUserName);
                        final List<Platforms> allPlatforms = new ArrayList<>();
                        for (final String platform : platforms) {
                            allPlatforms.add(new Platforms(platform));
                        }
                        dealerDBDashboard.getTopCustomers().setPlatforms(allPlatforms);
                        break;
                }
                if (null != inst) {
                    if (!isDealer) {
                        final List<MachineCountWIthCategory> machineListByCategory = inst.getKeys();
                        MachineCountWIthCategory machineCountWIthCategory = new MachineCountWIthCategory(
                                record.getMachineCount(), record.getCategory());
                        machineListByCategory.add(machineCountWIthCategory);
                    } else {
                        DashboardGraph.valueOf(record.getGraphType());
                        final List<MachineCountWIthCategory> machineListByCategory = inst.getKeys();
                        MachineCountWIthCategory machineCountWIthCategory = new MachineCountWIthCategory(
                                record.getMachineCount(), record.getCategory());
                        machineListByCategory.add(machineCountWIthCategory);
                    }
                }
            }

            if (isDealer) {
                if ("optional".equals(type) || type.equals("PLATFORM")) {
                    final Long machineCount = machineRepository.getCountByUsersUserName(dealerUserName);
                    if (machineCount != 0L) {
                        log.debug("Before evaluateByPlatform: dealerUserName={}, machineCount={}", dealerUserName, machineCount);
                        dealerDBDashboard.setPlatformDist(evaluateByPlatform(dealerUserName, machineCount, null));
                        log.debug("After evaluateByPlatform: platformDist={}", dealerDBDashboard.getPlatformDist());
                        dealerDBDashboard.getPlatformDist().setMachineCount(machineCount);
                    }
                }
            } else if ("TOP_CUSTOMERS".equals(type)) {
                final List<String> platforms = machineRepository
                        .findDistinctPlatformForUsers(userName);
                final List<Platforms> allPlatforms = new ArrayList<>();
                for (final String platform : platforms) {
                    allPlatforms.add(new Platforms(platform));
                }
                dealerDBDashboard.getTopCustomers().setPlatforms(allPlatforms);
            }
            if ("optional".equals(type) || "TOP_CUSTOMERS".equals(type)) {
                List<TopCustomer> topCustomerList = new LinkedList<>();
                List<DealerDashboardCustomer> dealerDashboardCustomerList = dealerDashboardCustomerRepo
                        .findAll(userName, PageRequest.of(0, 5));
                for (DealerDashboardCustomer dealerDashBoardCustomer : dealerDashboardCustomerList) {
                    List<MachineCountWIthCategory> platforms = new LinkedList<>();
                    List<DealerDashboardCustomerPlatform> CustomerPlatformList = ddCustomerPlatformRepo
                            .findAll(dealerDashBoardCustomer.getCustomerId());
                    for (DealerDashboardCustomerPlatform platform : CustomerPlatformList) {
                        MachineCountWIthCategory machineCountWIthCategory = new MachineCountWIthCategory(
                                platform.getMachineCount(), platform.getPlatformName());
                        platforms.add(machineCountWIthCategory);
                    }
                    TopCustomer topC = new TopCustomer(dealerDashBoardCustomer.getCustomerName(), dealerDashBoardCustomer.getCustomerId(),
                            dealerDashBoardCustomer.getMachineCount(), platforms);
                    topCustomerList.add(topC);
                }
                dealerDBDashboard.getTopCustomers().setCustomerInfo(topCustomerList);
            }
        } catch (Exception ex) {
            log.error("Exception for getting admin dashboard: {} Trace: {} AdminUser: {} Dealer: {}",
                    ex.getMessage(), Arrays.toString(ex.getStackTrace()), userName, dealerUserName);
        }
        log.info("Exiting getDashboardFromDB for user: {}, type: {}, isDealer: {}", userName, type, isDealer);
        return dealerDBDashboard;
    }

    // getting all records for Warranty Category
    private CategoryMachineCountList getWarrantyData() {
        final List<MachineCountWIthCategory> categoryList = new LinkedList<>();
        MachineCountWIthCategory machineCountWIthCategory;
        // added records for underWarranty
        machineCountWIthCategory = new MachineCountWIthCategory(DEFAULT_COUNT,
                String.valueOf(MachineWarranty.UNDER_WARRANTY));
        categoryList.add(machineCountWIthCategory);
        // added records for warrantyExpired
        machineCountWIthCategory = new MachineCountWIthCategory(DEFAULT_COUNT,
                String.valueOf(MachineWarranty.WARRANTY_EXPIRED));
        categoryList.add(machineCountWIthCategory);
        return new CategoryMachineCountList(MACHINE_COUNT_DUMMY, categoryList);
    }

    // evaluating all parameter for platformDist Category
    private CategoryMachineCountList evaluateByPlatform(String userName, Long machineCount, String search) {
        final List<Tuple> list = genericeQueryRepo.getGroupByParamCount(userName, PLATFORM_PARAM_NAME, "", "",
                search, 0, 100, false);
        return new CategoryMachineCountList(machineCount, populateCategoryList(list));
    }

    // loading data for machine count by platform(list platform name ,machine count)
    private List<MachineCountWIthCategory> populateCategoryList(List<Tuple> list) {
        final List<MachineCountWIthCategory> machineListByCategory = new LinkedList<>();
        MachineCountWIthCategory machineCountWIthCategory;
        for (final Tuple tuple : list) {
            machineCountWIthCategory = new MachineCountWIthCategory();
            machineCountWIthCategory.setCategory(tuple.get(0).toString());
            machineCountWIthCategory.setMachineCount(Integer.parseInt(tuple.get(1).toString()));
            machineListByCategory.add(machineCountWIthCategory);
        }
        return machineListByCategory;
    }

    // evaluating all parameter for platformDist Category
    private void populateDashBoardList(DealerDashboard dealerDashboard, Long machineCount, String userName, String type,
                                       String search) {
        if ("optional".equals(type)) {
            // loading records for machine platform like Backhoe Excavators
            dealerDashboard.setPlatformDist(evaluateByPlatform(userName, machineCount, search));
            // loading records for alerts cases like critical and high
            dealerDashboard.setAlerts(evaluateByAlerts(userName, machineCount));
            // loading records for service status like service overdue,service due normal
            dealerDashboard.setServiceStatus(evaluateByServiceStatus(userName, machineCount));
            // loading records for utilization lesserUsed, moderateUsed , heavilyUsed
            dealerDashboard.setUtilization(evaluateByUtilization(userName, machineCount));
            // loading records for machine locater cases transit , normal
            dealerDashboard.setMachineLocator(evaluateByMachineLocater(userName, machineCount, search));
            // loading records for statusAsOnTime like communicating ,nonCommunicating
            dealerDashboard.setConnectivity(evaluateByStatusAsOnTime(userName));
            // loading records for top 5 customer details
            dealerDashboard.setTopCustomers(
                    getTopCustomerData(userName, machineCount, search));
            // loading records for warranty
            dealerDashboard.setWarranty(getWarrantyData());
            // loading records for renewal status
            dealerDashboard.setRenewalStatus(getRenewalData(machineCount, userName));
        } else if ("PLATFORM".equals(type)) {
            dealerDashboard.setPlatformDist(evaluateByPlatform(userName, machineCount, search));
        } else if ("ALERT".equals(type)) {
            dealerDashboard.setAlerts(evaluateByAlerts(userName, machineCount));
        } else if ("SERVICE_STATUS".equals(type)) {
            dealerDashboard.setServiceStatus(evaluateByServiceStatus(userName, machineCount));
        } else if ("UTILIZATION".equals(type)) {
            dealerDashboard.setUtilization(evaluateByUtilization(userName, machineCount));
        } else if ("CONNECTIVITY".equals(type)) {
            dealerDashboard.setConnectivity(evaluateByStatusAsOnTime(userName));
        } else if ("TOP_CUSTOMERS".equals(type)) {
            dealerDashboard.setTopCustomers(
                    getTopCustomerData(userName, machineCount, search));
        } else if ("WARRANTY".equals(type)) {
            dealerDashboard.setWarranty(getWarrantyData());
        } else if ("RENEWAL_STATUS".equals(type)) {
            dealerDashboard.setRenewalStatus(getRenewalData(machineCount, userName));

        }
    }

    // evaluating all parameter for alert Category
    private CategoryMachineCountList evaluateByAlerts(String userName, Long machineCount) {
        final List<MachineCountWIthCategory> machinesWithCategory = new LinkedList<>();
        try {
            final Long criticalAlertCount = alertRepository.countMachinesByCriticalAlerts(userName, EventLevel.RED);
            machinesWithCategory
                    .add(new MachineCountWIthCategory(criticalAlertCount.intValue(), String.valueOf(EventLevel.RED)));
            log.info("highAlertCount ");
            final Long highAlertCount = alertRepository.countMachinesByHighAlerts(userName, EventLevel.RED,
                    EventLevel.YELLOW);

            machinesWithCategory
                    .add(new MachineCountWIthCategory(highAlertCount.intValue(), String.valueOf(EventLevel.YELLOW)));
        } catch (final Exception ex) {
            log.error("Failed to Process request with message  {}", ex.getMessage());
        }
        return new CategoryMachineCountList(machineCount, machinesWithCategory);
    }

    // evaluating all parameter for ServiceStaus Category
    private CategoryMachineCountList evaluateByServiceStatus(String userName, Long machineCount) {
        final List<MachineCountWIthCategory> machineListByCategory = new LinkedList<>();
        MachineCountWIthCategory machineCountWithCategory;
        Long count;
        // getting records for service due status
        count = genericeQueryRepo.getMachineServiceDueCount(userName, false);
        machineCountWithCategory = new MachineCountWIthCategory();
        machineCountWithCategory.setCategory(ServiceCategory.SERVICE_DUE.getName());
        machineCountWithCategory.setMachineCount(count.intValue());
        machineListByCategory.add(machineCountWithCategory);
        // getting records for service overDue status
        count = genericeQueryRepo.getMachineServiceOverDueCount(userName, false);
        machineCountWithCategory = new MachineCountWIthCategory();
        machineCountWithCategory.setCategory(ServiceCategory.SERVICE_OVERDUE.getName());
        machineCountWithCategory.setMachineCount(count.intValue());
        machineListByCategory.add(machineCountWithCategory);
        // getting data for normal status
        count = genericeQueryRepo.getMachineServiceNoDataCount(userName, false);
        machineCountWithCategory = new MachineCountWIthCategory();
        machineCountWithCategory.setCategory(ServiceCategory.NO_DATA_AVAILABLE.getName());
        machineCountWithCategory.setMachineCount(count.intValue());
        machineListByCategory.add(machineCountWithCategory);
        // getting data for no data status
        count = genericeQueryRepo.getMachineServicNormalCount(userName, false);
        machineCountWithCategory = new MachineCountWIthCategory();
        machineCountWithCategory.setCategory(ServiceCategory.SERVICE_NORMAL.getName());
        machineCountWithCategory.setMachineCount(count.intValue());
        machineListByCategory.add(machineCountWithCategory);
        return new CategoryMachineCountList(machineCount, machineListByCategory);
    }

    // evaluating for Machine Utilization Category
    private CategoryMachineCountList evaluateByUtilization(String userName, Long machineCount) {
        MachineCountWIthCategory machineCountWithCategory;
        final List<MachineCountWIthCategory> machineListByCategory = new LinkedList<>();
        final List<String> resultList = genericeQueryRepo.getUtilizationNoData(userName, false);

        if (!resultList.isEmpty()) {
            machineCountWithCategory = new MachineCountWIthCategory(resultList.size(),
                    String.valueOf(MachineUtilizationCategory.NO_DATA_AVAILABLE));
        } else {
            machineCountWithCategory = new MachineCountWIthCategory(0,
                    String.valueOf(MachineUtilizationCategory.NO_DATA_AVAILABLE));
        }
        //final Date day = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
        machineListByCategory.add(machineCountWithCategory);
        machineCountWithCategory = new MachineCountWIthCategory(
                genericeQueryRepo.getUtilizationData(userName, "ModerateUsed", false).intValue(),
                String.valueOf(MachineUtilizationCategory.MODERATED_USED));
        machineListByCategory.add(machineCountWithCategory);
        machineCountWithCategory = new MachineCountWIthCategory(
                genericeQueryRepo.getUtilizationData(userName, "HeavilyUsed", false).intValue(),
                String.valueOf(MachineUtilizationCategory.HEAVILY_USED));
        machineListByCategory.add(machineCountWithCategory);
        machineCountWithCategory = new MachineCountWIthCategory(
                genericeQueryRepo.getUtilizationData(userName, "LesserUsed", false).intValue(),
                String.valueOf(MachineUtilizationCategory.LESSER_USED));
        machineListByCategory.add(machineCountWithCategory);
        return new CategoryMachineCountList(machineCount, machineListByCategory);
    }

    // evaluating all parameter for machineLocater Category
    private CategoryMachineCountList evaluateByMachineLocater(String userName, Long machineCount, String search) {
        final List<Tuple> list = genericeQueryRepo.getGroupByParamCount(userName, TRANSIT_PARAM_NAME, null, null,
                search, 0, 100, false);
        return new CategoryMachineCountList(machineCount, populateCategoryListForMachineLocator(list));
    }

    // evaluating all parameter for Connectivity Category
    private CategoryMachineCountList evaluateByStatusAsOnTime(String userName) {
        MachineCountWIthCategory machineCountWithCategory;
        final Date date = utilities.getStartDateTimeInDateFormat(nonCommunicatingDate);
        final List<MachineCountWIthCategory> machineListByCategory = new LinkedList<>();
        final Long countNonCommunicating = genericeQueryRepo.getCommunicationData(userName, date, false, false);
        machineCountWithCategory = new MachineCountWIthCategory(countNonCommunicating.intValue(),
                String.valueOf(CommunicationType.NON_COMMUNICATING));
        machineListByCategory.add(machineCountWithCategory);
        // getting data for communicating date
        final Long countCommunicating = genericeQueryRepo.getCommunicationData(userName, date, true, false);
        machineCountWithCategory = new MachineCountWIthCategory(countCommunicating.intValue(),
                String.valueOf(CommunicationType.COMMUNICATING));
        machineListByCategory.add(machineCountWithCategory);
        // renewal flag added
        final Long subscribedCount = genericeQueryRepo.getMachineCountWithRenewalFlag(userName, false);
        machineCountWithCategory = new MachineCountWIthCategory(subscribedCount.intValue(),
                String.valueOf(CommunicationType.SUBSCRIBED_COUNT));
        machineListByCategory.add(machineCountWithCategory);

        log.info(" count details {} - {} - {} - {}", date, countNonCommunicating, countCommunicating, subscribedCount);
        return new CategoryMachineCountList(subscribedCount, machineListByCategory);
    }

    private List<MachineCountWIthCategory> populateCategoryListForMachineLocator(List<Tuple> list) {
        final List<MachineCountWIthCategory> machineListByCategory = new LinkedList<>();
        MachineCountWIthCategory transit = new MachineCountWIthCategory();
        transit.setCategory("TRANSIT");
        transit.setMachineCount(0);
        machineListByCategory.add(transit);
        MachineCountWIthCategory normal = new MachineCountWIthCategory();
        normal.setCategory("NORMAL");
        normal.setMachineCount(0);
        machineListByCategory.add(normal);
        for (final Tuple tuple : list) {
            final TransitMode modeOfTransit = TransitMode.valueOf(tuple.get(0).toString());
            switch (modeOfTransit) {
                case TRANSIT:
                    transit.setMachineCount(Integer.parseInt(tuple.get(1).toString()));
                    break;
                case NORMAL:
                    normal.setMachineCount(Integer.parseInt(tuple.get(1).toString()));
                    break;
                default:
                    break;
            }
        }
        return machineListByCategory;
    }

    // evaluating all records for top customer Category
    private TopCustomerRecords getTopCustomerData(String userName, Long machineCount, String search) {
        final List<TopCustomer> topCustomerList = new LinkedList<>();
        final List<Platforms> allPlatforms = new ArrayList<>();
        try {

            TopCustomer topCustomer;
            final List<Tuple> list = genericeQueryRepo.getGroupByParamCount(userName, CUSTOMER_PARAM_NAME, null, null,
                    search, DealerDashboardResponseServiceImpl.CUSTOMER_LIST_START, DealerDashboardResponseServiceImpl.CUSTOMER_LIST_SIZE, false);
            for (final Tuple tuple : list) {
                final List<Tuple> paramList = genericeQueryRepo.getGroupByParamCount(userName, PLATFORM_PARAM_NAME,
                        tuple.get(2).toString(), null, search, DealerDashboardResponseServiceImpl.CUSTOMER_LIST_START, DealerDashboardResponseServiceImpl.CUSTOMER_LIST_SIZE, false);
                topCustomer = new TopCustomer(tuple.get(1).toString(), tuple.get(2).toString(),
                        Integer.parseInt(tuple.get(0).toString()));
                topCustomer.setPlatforms(populateCategoryList(paramList));
                topCustomerList.add(topCustomer);
            }
            final List<String> platforms = machineRepository.findDistinctPlatformForUsers(userName);

            for (final String platform : platforms) {
                allPlatforms.add(new Platforms(platform));
            }
        } catch (Exception e) {
            log.error("Error in Top Customer :{}", userName);
        }
        return new TopCustomerRecords(machineCount.intValue(), topCustomerList, allPlatforms);

    }

    // getting all records for renewal type Category
    private CategoryMachineCountList getRenewalData(Long machineCount, String userName) {
        final List<MachineCountWIthCategory> categoryList = new LinkedList<>();
        MachineCountWIthCategory machineCountWIthCategory;
        // added records for renewalOverdue
        final Date today = utilities.getDate(utilities.getStartDate(0));
        final Date maxRenewalDate = utilities.getDate(utilities.getEndDate(30));
        final Date communicatingDate = utilities.getStartDateTimeInDateFormat(-60);
        machineCountWIthCategory = new MachineCountWIthCategory(
                machineRepository.getCountByRenewalOverDue(userName, today, communicatingDate).intValue(),
                String.valueOf(LivelinkRenawal.RENEWAL_OVERDUE));
        categoryList.add(machineCountWIthCategory);
        // added records for renewalApproaching
        machineCountWIthCategory = new MachineCountWIthCategory(
                machineRepository.getCountByRenewalApproaching(userName, maxRenewalDate, communicatingDate).intValue(),
                String.valueOf(LivelinkRenawal.RENEWAL_APPROACHING));
        categoryList.add(machineCountWIthCategory);
        // added record for immediate
        machineCountWIthCategory = new MachineCountWIthCategory(
                machineRepository.getCountByRenewalImmediate(userName, today, maxRenewalDate, communicatingDate).intValue(),
                String.valueOf(LivelinkRenawal.RENEWAL_IMMEDIATE));
        categoryList.add(machineCountWIthCategory);
        // added record for noData
        machineCountWIthCategory = new MachineCountWIthCategory(
                machineRepository.getCountByRenewalNoData(userName).intValue(),
                String.valueOf(LivelinkRenawal.RENEWAL_NO_DATA));
        categoryList.add(machineCountWIthCategory);
        return new CategoryMachineCountList(machineCount, categoryList);
    }
}
