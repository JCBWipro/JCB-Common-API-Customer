package com.wipro.jcb.livelink.app.machines.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineUtilizationData;
import com.wipro.jcb.livelink.app.machines.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;

@Repository
public class GenericeQueryRepo {

	@Value("${machine.approachingservicedays}")
	private int machineApproachingServiceDays;

	@Value("${machine.servicedueminhours}")
	private Double serviceDueMinHours;
	
	@Value("${machine.utilization.day}")
	private int machineUtilizationDay;
	
	@Value("${machine.lesser.used.max.range}")
	private long lesserUsedMaxRange;
	
	@Value("${machine.moderate.used.max.range}")
	private long moderateUsedMaxRange;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Utilities utilities;

	private static final String USERS = "users";
	private static final String USER_NAME = "userName";
	private static final String SERVICE_DUE_DATE = "serviceDueDate";
	private static final String SERVICE_DUE_HOUR = "serviceDueHours";
	private static final String TOTAL_MACHINE_HOUR = "totalMachineHours";
	private static final String STATUS_AS_ON_TIME = "statusAsOnTime";
	private static final String CUSTOMER = "customer";
	private static final String CUSTOMER_PARAM_ID = "id";
	private static final String CUSTOMER_PARAM_NAME = "name";
	private static final String VIN = "vin";
	private static final String MACHINE_UTILIZATION_WORKING_HOUR = "workingHours";
	private static final String MACHINE_UTILIZATION_DAY = "day";
	public static final String PLATFORM_PARAM_NAME = "platform";
	public static final String MODEL_PARAM_NAME = "model";
	public static final String TRANSIT_MODE_PARAM_NAME = "transitMode";
	private static final String RENEWAL_FLAG = "renewalFlag";

	public Long getMachineCount(String userName, boolean isDashBoardDetails) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		if (isDashBoardDetails) {
			query.where(userPredicate, from.get(CUSTOMER).isNotNull());
		} else {
			query.where(userPredicate);
		}
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}

	private Predicate getUserPredicate(CriteriaBuilder builder, Root<Machine> from, String userName) {
		final SetJoin<Machine, User> users = from.joinSet(USERS);
		return builder.equal(users.<String>get(USER_NAME), userName);
	}

	public long getResult(TypedQuery<Long> query) {
		try {
			return query.getSingleResult();
		} catch (final NoResultException e) {
			return 0;
		}
	}

	public List<Tuple> getGroupByParamCount(String userName, String paramName, String customerId, String search,
			String filter, int pageNumber, int pageSize, boolean isDashBoardDetails) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createTupleQuery();
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> predicateList = new ArrayList<>();
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		predicateList.add(userPredicate);
		query.orderBy(builder.desc(builder.count(from)));
		Predicate modelPredicate = null;
		if (isDashBoardDetails) {
			final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
			predicateList.add(customerNotNullPredicate);
		}
		if (MODEL_PARAM_NAME.equals(paramName)) {
			if (filter != null) {
				modelPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), filter);
				predicateList.add(modelPredicate);
			}
		}
		if (customerId != null) {
			final Predicate customerPredicate = builder.equal(from.join(CUSTOMER).<String>get(CUSTOMER_PARAM_ID),
					customerId);
			predicateList.add(customerPredicate);
		}
		query.where(builder.and(predicateList.toArray(new Predicate[] {})));
		return (CUSTOMER.equals(paramName)) ? addSubParam(builder, from, query, paramName, pageNumber, pageSize)
				: addParam(builder, from, query, paramName);
	}

	private List<Tuple> addSubParam(CriteriaBuilder builder, Root<Machine> from, CriteriaQuery<Tuple> query,
			String paramName, int pageNumber, int pageSize) {
		query.groupBy(from.get(paramName).<String>get(CUSTOMER_PARAM_ID),
				from.get(paramName).<String>get(CUSTOMER_PARAM_NAME));
		query.multiselect(builder.count(from), from.get(paramName).<String>get(CUSTOMER_PARAM_NAME),
				from.get(paramName).<String>get(CUSTOMER_PARAM_ID));
		return getListBySize(entityManager.createQuery(query), pageNumber, pageSize);
	}

	public <U> List<U> getListBySize(TypedQuery<U> query, int pageNumber, int pageSize) {
		query.setFirstResult((pageNumber * pageSize));
		query.setMaxResults(pageSize);
		return getList(query);
	}

	public <U> List<U> getList(TypedQuery<U> query) {
		List<U> result = query.getResultList();
		if (result == null) {
			result = new LinkedList<>();
		}
		return result;
	}

	private List<Tuple> addParam(CriteriaBuilder builder, Root<Machine> from, CriteriaQuery<Tuple> query,
			String paramName) {
		query.groupBy(from.get(paramName));
		query.orderBy(builder.desc(builder.count(from)));
		query.multiselect(from.get(paramName), builder.count(from));
		return getList(entityManager.createQuery(query));
	}

	public Long getMachineCountWithRenewalFlag(String userName, boolean isDashBoardDetails) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		if (isDashBoardDetails) {
			query.where(userPredicate, from.get(CUSTOMER).isNotNull());
		} else {
			query.where(userPredicate);

		}
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}

	public Long getMachineServiceDueCount(String userName, Boolean isDashBoardDetails) {
		final Date serviceDueafter = utilities.getDateTime(utilities.getStartDateTime(0));
		final Date serviceDueBefore = utilities.getDateTime(utilities.getEndDateTime(machineApproachingServiceDays));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final Predicate hoursPredicate = builder
				.greaterThan(builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours), from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.between(dateExpression, builder.literal(serviceDueafter),
				builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commonCriteriaList = getCommonServicePredicate(builder, from, userName);
		if (isDashBoardDetails) {
			final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
			commonCriteriaList.add(customerNotNullPredicate);
		}
		final Predicate commonnHoursPredicate = builder.lessThan(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate currentDatePredicate = builder.greaterThanOrEqualTo(dateExpression,
				builder.literal(serviceDueafter));
		query.where(builder.and(commonCriteriaList.toArray(new Predicate[] {})),
				builder.or(criteriaList.toArray(new Predicate[] {})), builder.and(commonnHoursPredicate),
				builder.and(currentDatePredicate));
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}

	public Long getMachineServiceOverDueCount(String userName, Boolean isDashBoardDetails) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> serviceDueDate = from.get(SERVICE_DUE_DATE);
		final Date serviceDueBefore = utilities.getDateTime(utilities.getStartDateTime(0));
		final Predicate hoursPredicate = builder.greaterThanOrEqualTo(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.lessThan(serviceDueDate, builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commomPredicate = getCommonServicePredicate(builder, from, userName);
		if (isDashBoardDetails) {
			final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
			commomPredicate.add(customerNotNullPredicate);
		}
		query.where(builder.and(commomPredicate.toArray(new Predicate[] {})),
				builder.or(criteriaList.toArray(new Predicate[] {})));
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}

	public Long getMachineServiceNoDataCount(String userName, Boolean isDashBoardDetails) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate datePredicate = builder.isNull(from.get(SERVICE_DUE_DATE));
		criteriaList.add(datePredicate);
		if (isDashBoardDetails) {
			final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
			criteriaList.add(customerNotNullPredicate);
		}
		query.where(userPredicate, builder.and(criteriaList.toArray(new Predicate[] {})));
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}

	public Long getMachineServicNormalCount(String userName, Boolean isDashBoardDetails) {
		final Date date = utilities.getDateTime(utilities.getEndDateTime(30));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate hoursPredicate = builder.greaterThan(from.get(SERVICE_DUE_HOUR),
				builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours));
		final Predicate datePredicate = builder.greaterThan(dateExpression, builder.literal(date));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commomPredicate = getCommonServicePredicate(builder, from, userName);
		if (isDashBoardDetails) {
			final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
			commomPredicate.add(customerNotNullPredicate);
		}
		query.where(builder.and(commomPredicate.toArray(new Predicate[] {})),
				builder.and(criteriaList.toArray(new Predicate[] {})));
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}

	private List<Predicate> getCommonServicePredicate(CriteriaBuilder builder, Root<Machine> from, String userName) {
		final List<Predicate> commonCriteriaList = new ArrayList<>();
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final Predicate dueDatePredicate = builder.isNotNull(from.get(SERVICE_DUE_DATE));
		commonCriteriaList.add(dueDatePredicate);
		commonCriteriaList.add(userPredicate);
		return commonCriteriaList;
	}
	
	public List<Tuple> getMachineServiceStatusOverDueByCustomer(String userName, String search, int pageNumber,
			int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createTupleQuery();
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> serviceDueDate = from.get(SERVICE_DUE_DATE);
		final Date serviceDueBefore = utilities.getDateTime(utilities.getStartDateTime(0));
		final Predicate hoursPredicate = builder.greaterThanOrEqualTo(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.lessThan(serviceDueDate, builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		final Predicate[] commomPredicate = getCommonServicePredicate(builder, from, userName)
				.toArray(new Predicate[] {});
		query.where(builder.and(commomPredicate),  builder.or(criteriaList.toArray(new Predicate[] {})),
				builder.and(customerNotNullPredicate));
		return addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize);
	}
	
	private List<Tuple> addSubCustParam(CriteriaBuilder builder, Root<Machine> from, CriteriaQuery<Tuple> query,
			String paramName, int pageNumber, int pageSize) {
		query.orderBy(builder.desc(builder.count(from)));
		query.groupBy(from.get(paramName).<String>get(CUSTOMER_PARAM_ID));
		query.multiselect(builder.count(from), from.get(paramName).<String>get(CUSTOMER_PARAM_ID));
		return getListBySize(entityManager.createQuery(query), pageNumber, pageSize);
	}
	
	public List<Tuple> getMachineServiceStatusDueByCustomer(String userName, String search, int pageNumber,
			int pageSize) {
		final Date serviceDueafter = utilities.getDateTime(utilities.getStartDateTime(0));
		final Date serviceDueBefore = utilities.getDateTime(utilities.getEndDateTime(machineApproachingServiceDays));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final Predicate hoursPredicate = builder
				.greaterThan(builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours), from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.between(dateExpression, builder.literal(serviceDueafter),
				builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commonCriteriaList = getCommonServicePredicate(builder, from, userName);
		final Predicate commonnHoursPredicate = builder.lessThan(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate currentDatePredicate = builder.greaterThanOrEqualTo(dateExpression,
				builder.literal(serviceDueafter));
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		query.where(builder.and(commonCriteriaList.toArray(new Predicate[] {})),
				builder.or(criteriaList.toArray(new Predicate[] {})), builder.and(commonnHoursPredicate),
				builder.and(currentDatePredicate), builder.and(customerNotNullPredicate));
		return addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize);
	}
	
	public List<Tuple> getMachineServiceStatusNormalByCustomer(String userName, String search, int pageNumber,
			int pageSize) {
		final Date date = utilities.getDateTime(utilities.getEndDateTime(30));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate hoursPredicate = builder.greaterThan(from.get(SERVICE_DUE_HOUR),
				builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours));
		final Predicate datePredicate = builder.greaterThan(dateExpression, builder.literal(date));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		final Predicate[] commomPredicate = getCommonServicePredicate(builder, from, userName)
				.toArray(new Predicate[] {});
		query.where(builder.and(commomPredicate), builder.and(customerNotNullPredicate),
				builder.and(criteriaList.toArray(new Predicate[] {})));
		return addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize);
	}
	
	public List<Tuple> getMachineServiceStatusNoDataByCustomer(String userName, String search, int pageNumber,
			int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate datePredicate = builder.isNull(from.get(SERVICE_DUE_DATE));
		criteriaList.add(datePredicate);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		query.where(userPredicate, builder.and(customerNotNullPredicate),
				builder.and(criteriaList.toArray(new Predicate[] {})));
		return addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize);
	}
	
	public List<Tuple> getCustomerForServiceOverDueByPlatform(String userName, String platform, String search,
			int pageNumber, int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createTupleQuery();
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> serviceDueDate = from.get(SERVICE_DUE_DATE);
		final Date serviceDueBefore = utilities.getDateTime(utilities.getStartDateTime(0));
		final Predicate platformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), platform);
		final Predicate hoursPredicate = builder.greaterThanOrEqualTo(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.lessThan(serviceDueDate, builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		final Predicate[] commomPredicate = getCommonServicePredicate(builder, from, userName)
				.toArray(new Predicate[] {});
		query.where(builder.and(commomPredicate),  builder.or(criteriaList.toArray(new Predicate[] {})),
				builder.and(customerNotNullPredicate), builder.and(platformPredicate));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}
	
	public List<Tuple> getCustomerForServiceDueByPlatform(String userName, String platform, String search,
			int pageNumber, int pageSize) {
		final Date serviceDueafter = utilities.getDateTime(utilities.getStartDateTime(0));
		final Date serviceDueBefore = utilities.getDateTime(utilities.getEndDateTime(machineApproachingServiceDays));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final Predicate hoursPredicate = builder
				.greaterThan(builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours), from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.between(dateExpression, builder.literal(serviceDueafter),
				builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final Predicate platformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), platform);
		final List<Predicate> commonCriteriaList = getCommonServicePredicate(builder, from, userName);
		final Predicate commonnHoursPredicate = builder.lessThan(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate currentDatePredicate = builder.greaterThanOrEqualTo(dateExpression,
				builder.literal(serviceDueafter));
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		query.where(builder.and(commonCriteriaList.toArray(new Predicate[] {})),
				builder.or(criteriaList.toArray(new Predicate[] {})), builder.and(commonnHoursPredicate),
				builder.and(currentDatePredicate), builder.and(platformPredicate),
				builder.and(customerNotNullPredicate));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}
	
	public List<Tuple> getCustomerForServiceNormalByPlatform(String userName, String platform, String search,
			int pageNumber, int pageSize) {
		final Date date = utilities.getDateTime(utilities.getEndDateTime(30));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate hoursPredicate = builder.greaterThan(from.get(SERVICE_DUE_HOUR),
				builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours));
		final Predicate datePredicate = builder.greaterThan(dateExpression, builder.literal(date));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final Predicate platformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), platform);
		criteriaList.add(platformPredicate);
		final Predicate[] commomPredicate = getCommonServicePredicate(builder, from, userName)
				.toArray(new Predicate[] {});
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		criteriaList.add(customerNotNullPredicate);
		query.where(builder.and(commomPredicate), builder.and(criteriaList.toArray(new Predicate[] {})));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}

	public List<Tuple> getCustomerForServiceNoDataByPlatform(String userName, String platform, String search,
			int pageNumber, int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate datePredicate = builder.isNull(from.get(SERVICE_DUE_DATE));
		criteriaList.add(datePredicate);
		final Predicate platformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), platform);
		criteriaList.add(platformPredicate);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		query.where(userPredicate, customerNotNullPredicate, builder.and(criteriaList.toArray(new Predicate[] {})));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}
	
	public Long getUtilizationData(String userName, String keyCase, Boolean isDashBoardDetails) {		
		final Date startDate = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
		final Date endDate = utilities.getDate(utilities.getStartDate(1));
		long days = (endDate.getTime() - startDate.getTime())/ 86400000L;
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<MachineUtilizationData> from = query.from(MachineUtilizationData.class);
		final Expression<Date> dateExpression = from.get(MACHINE_UTILIZATION_DAY);
		final Expression<Boolean> utilizationExpression = getMachineUtilizationExpression(builder, from, keyCase,days);
		final Predicate datePredicate = builder.between(dateExpression, (builder.literal(startDate)),
				(builder.literal(endDate)));
		final List<Predicate> criteriaList = new LinkedList<>();
		final Subquery<String> subquery = query.subquery(String.class);
		final Root<Machine> machinefrom = subquery.from(Machine.class);
		final SetJoin<Machine, User> users = machinefrom.joinSet(USERS);
		subquery.select(machinefrom.get(VIN));
		if (isDashBoardDetails) {
			subquery.where(builder.equal(users.<String>get(USER_NAME), userName),
					machinefrom.get(CUSTOMER).isNotNull());
		} else {
			subquery.where(builder.equal(users.<String>get(USER_NAME), userName));
		}
		criteriaList.add(builder.in(from.get(VIN)).value(subquery));
		criteriaList.add(datePredicate);
		query.where(criteriaList.toArray(new Predicate[] {}));
		query.select(from.get(VIN)).distinct(true);
		query.groupBy(from.get(VIN));
		query.having(utilizationExpression);
		final List<String> vinList = getList(entityManager.createQuery(query));
		return Long.valueOf(vinList.size());
	}
	
	private Expression<Boolean> getMachineUtilizationExpression(CriteriaBuilder builder,
			Root<MachineUtilizationData> from, String key,long days) {
		Expression<Boolean> exp = null;
		
		switch (key) {
		case "LesserUsed":			
			long lesserUsedMax = lesserUsedMaxRange*days;			
			exp = builder.lessThan(builder.sum(from.get(MACHINE_UTILIZATION_WORKING_HOUR)), lesserUsedMax);
			break;
		case "ModerateUsed":
			long moderateUsedMin = (lesserUsedMaxRange*days);
			long moderateUsedMax = moderateUsedMaxRange*days;
			exp = builder.between(builder.sum(from.get(MACHINE_UTILIZATION_WORKING_HOUR)), moderateUsedMin,
					moderateUsedMax);
			break;
		case "HeavilyUsed":
			long heavilyUsedMax = (moderateUsedMaxRange * days);
			exp = builder.greaterThan(builder.sum(from.get(MACHINE_UTILIZATION_WORKING_HOUR)), heavilyUsedMax);
			break;
		default:
			break;
		}
		return exp;
	}
	
	public List<String> getUtilizationNoData(String userName, Boolean isDashBoardDetails) {
		List<String> vinList = null;
		final List<Predicate> criteriaList = new LinkedList<>();
		final Date startDate = utilities.getDate(utilities.getStartDate(machineUtilizationDay));
		final Date endDate = utilities.getDate(utilities.getStartDate(0));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<MachineUtilizationData> from = query.from(MachineUtilizationData.class);
		final Expression<Date> dateExpression = from.get(MACHINE_UTILIZATION_DAY);
		final Predicate datePredicate = builder.between(dateExpression, (builder.literal(startDate)),
				(builder.literal(endDate)));
		final Subquery<String> subquery = query.subquery(String.class);
		final Root<Machine> machinefrom = subquery.from(Machine.class);
		final SetJoin<Machine, User> users = machinefrom.joinSet(USERS);
		subquery.select(machinefrom.get(VIN));
		if (isDashBoardDetails) {
			subquery.where(builder.equal(users.<String>get(USER_NAME), userName),
					machinefrom.get(CUSTOMER).isNotNull());
			vinList = getMachineVinWithCust(userName);
		} else {
			subquery.where(builder.equal(users.<String>get(USER_NAME), userName));
			vinList = getMachineVin(userName);
		}
		criteriaList.add(builder.in(from.get(VIN)).value(subquery));
		criteriaList.add(datePredicate);
		query.where(criteriaList.toArray(new Predicate[] {}));
		query.select(from.get(VIN)).distinct(true);
		final List<String> tuples = getList(entityManager.createQuery(query));
		if (!tuples.isEmpty()) {
			vinList.removeAll(tuples);
		}
		return vinList;
	}
	
	public List<String> getMachineVinWithCust(String userName) {
		List<String> tuples;
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final Predicate renewalFlagPredicate = builder.equal(from.get(RENEWAL_FLAG), true);
		query.where(userPredicate, renewalFlagPredicate, from.get(CUSTOMER).isNotNull());
		query.select(from.get(VIN));
		tuples = getList(entityManager.createQuery(query));
		return tuples;
	}
	
	public List<String> getMachineVin(String userName) {
		List<String> tuples;
		final List<Predicate> criteriaList = new LinkedList<>();
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<String> query = builder.createQuery(String.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final Predicate renewalFlagPredicate = builder.equal(from.get(RENEWAL_FLAG), true);
		criteriaList.add(renewalFlagPredicate);
		criteriaList.add(userPredicate);
		query.where(criteriaList.toArray(new Predicate[] {}));
		query.select(from.get(VIN));
		tuples = getList(entityManager.createQuery(query));
		return tuples;
	}
	
	public Long getCommunicationData(String userName, Date date, boolean isCommunicable, boolean isDashBoardDetails) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final Expression<Date> dateExpression = from.get(STATUS_AS_ON_TIME);
		final List<Predicate> predicateList = new LinkedList<>();
		final Predicate datePredicate = isCommunicable
				? builder.greaterThanOrEqualTo(dateExpression, (builder.literal(date)))
				: builder.lessThan(dateExpression, (builder.literal(date)));
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		predicateList.add(userPredicate);
		predicateList.add(datePredicate);
		
		if (isDashBoardDetails) {
			final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
			predicateList.add(customerNotNullPredicate);
		}
		query.where(predicateList.toArray(new Predicate[] {}));
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}
	
	public List<Tuple> getCommunicatingByCustomer(String userName, Date date, boolean isCommunicable, String platform,
			int pageNumber, int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new LinkedList<>();
		if (platform != null) {
			final Predicate platformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), platform);
			criteriaList.add(platformPredicate);
		}
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		criteriaList.add(customerNotNullPredicate);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		criteriaList.add(userPredicate);
		final Expression<Date> dateExpression = from.get(STATUS_AS_ON_TIME);
		final Predicate datePredicate = isCommunicable
				? builder.greaterThanOrEqualTo(dateExpression, (builder.literal(date)))
				: builder.lessThan(dateExpression, (builder.literal(date)));
		criteriaList.add(datePredicate);
		query.where(criteriaList.toArray(new Predicate[] {}));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}
	
	public List<Tuple> getCommunicationByPlatform(String userName, Date date, boolean isCommunicable) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Expression<Date> dateExpression = from.get(STATUS_AS_ON_TIME);
		final Predicate datePredicate = isCommunicable
				? builder.greaterThanOrEqualTo(dateExpression, (builder.literal(date)))
				: builder.lessThan(dateExpression, (builder.literal(date)));
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		query.where(userPredicate, datePredicate, customerNotNullPredicate);
		return addParam(builder, from, query, PLATFORM_PARAM_NAME);
	}
	
	public List<Tuple> countMachineLocaterByPlatform(String userName, int paramValue) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		query.where(builder.equal(from.get(TRANSIT_MODE_PARAM_NAME), paramValue), userPredicate,
				customerNotNullPredicate);
		return addParam(builder, from, query, PLATFORM_PARAM_NAME);
	}
	
	public Long countByMachineLocater(String userName, int paramValue) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		query.orderBy(builder.desc(builder.count(from)));
		query.where(builder.equal(from.get(TRANSIT_MODE_PARAM_NAME), paramValue), userPredicate,
				customerNotNullPredicate);
		query.select(builder.count(from));
		return getResult(entityManager.createQuery(query));
	}
	
	public List<Tuple> getMachineLocaterByCustomer(String userName, int transitMode, String platform, int pageNumber,
			int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new LinkedList<>();
		if (platform != null) {
			final Predicate platformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), platform);
			criteriaList.add(platformPredicate);
		}
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		criteriaList.add(customerNotNullPredicate);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		criteriaList.add(userPredicate);
		final Predicate transitModePredicate = builder.equal(from.get(TRANSIT_MODE_PARAM_NAME), transitMode);
		criteriaList.add(transitModePredicate);
		query.where(criteriaList.toArray(new Predicate[] {}));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}
	
	public List<Tuple> getCustomerGroupByParamCount(String userName, String paramName, String paraInParamName,
			String customerId, int pageNumber, int pageSize) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createTupleQuery();
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> predicateList = new ArrayList<>();
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		predicateList.add(userPredicate);
		if (!"ALL".equals(paramName)) {
			final Predicate plaformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), paramName);
			predicateList.add(plaformPredicate);
			final Predicate modelPredicate = builder.equal(from.get(MODEL_PARAM_NAME), paraInParamName);
			predicateList.add(modelPredicate);
		} else {
			final Predicate plaformPredicate = builder.equal(from.get(PLATFORM_PARAM_NAME), paraInParamName);
			predicateList.add(plaformPredicate);
		}
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		predicateList.add(customerNotNullPredicate);
		query.orderBy(builder.desc(builder.count(from)));
		if (customerId != null) {
			final Predicate customerPredicate = builder.equal(from.join(CUSTOMER).<String>get(CUSTOMER_PARAM_ID),
					customerId);
			predicateList.add(customerPredicate);
		}
		query.where(predicateList.toArray(new Predicate[] {}));
		return (addSubCustParam(builder, from, query, CUSTOMER, pageNumber, pageSize));
	}
	
	public List<Tuple> countByServiceOverDueWithPlatform(String userName, String search) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createTupleQuery();
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> serviceDueDate = from.get(SERVICE_DUE_DATE);
		final Date serviceDueBefore = utilities.getDateTime(utilities.getStartDateTime(0));
		final Predicate hoursPredicate = builder.greaterThanOrEqualTo(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.lessThan(serviceDueDate, builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commomPredicate = getCommonServicePredicate(builder, from, userName);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		commomPredicate.add(customerNotNullPredicate);
		query.where(builder.and(commomPredicate.toArray(new Predicate[] {})),
				builder.or(criteriaList.toArray(new Predicate[] {})));
		return addParam(builder, from, query, PLATFORM_PARAM_NAME);
	}
	
	public List<Tuple> countByServiceDueWithPlatform(String userName, String search) {
		final Date serviceDueafter = utilities.getDateTime(utilities.getStartDateTime(0));
		final Date serviceDueBefore = utilities.getDateTime(utilities.getEndDateTime(machineApproachingServiceDays));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final Predicate hoursPredicate = builder
				.greaterThan(builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours), from.get(SERVICE_DUE_HOUR));
		final Predicate datePredicate = builder.between(dateExpression, builder.literal(serviceDueafter),
				builder.literal(serviceDueBefore));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commonCriteriaList = getCommonServicePredicate(builder, from, userName);
		final Predicate commonnHoursPredicate = builder.lessThan(from.get(TOTAL_MACHINE_HOUR),
				from.get(SERVICE_DUE_HOUR));
		final Predicate currentDatePredicate = builder.greaterThanOrEqualTo(dateExpression,
				builder.literal(serviceDueafter));
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		commonCriteriaList.add(customerNotNullPredicate);
		query.where(builder.and(commonCriteriaList.toArray(new Predicate[] {})),
				builder.or(criteriaList.toArray(new Predicate[] {})), builder.and(commonnHoursPredicate),
				builder.and(currentDatePredicate));
		return addParam(builder, from, query, PLATFORM_PARAM_NAME);
	}

	public List<Tuple> countByServiceNormalWithPlatform(String userName, String search) {
		final Date date = utilities.getDateTime(utilities.getEndDateTime(30));
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Expression<Date> dateExpression = from.get(SERVICE_DUE_DATE);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate hoursPredicate = builder.greaterThan(from.get(SERVICE_DUE_HOUR),
				builder.sum(from.get(TOTAL_MACHINE_HOUR), serviceDueMinHours));
		final Predicate datePredicate = builder.greaterThan(dateExpression, builder.literal(date));
		criteriaList.add(hoursPredicate);
		criteriaList.add(datePredicate);
		final List<Predicate> commomPredicate = getCommonServicePredicate(builder, from, userName);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		commomPredicate.add(customerNotNullPredicate);
		query.where(builder.and(commomPredicate.toArray(new Predicate[] {})),
				builder.and(criteriaList.toArray(new Predicate[] {})));
		return addParam(builder, from, query, PLATFORM_PARAM_NAME);
	}
	
	public List<Tuple> countByServiceNoDataWithPlatform(String userName, String search) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
		final Root<Machine> from = query.from(Machine.class);
		final Predicate userPredicate = getUserPredicate(builder, from, userName);
		final List<Predicate> criteriaList = new ArrayList<>();
		final Predicate datePredicate = builder.isNull(from.get(SERVICE_DUE_DATE));
		criteriaList.add(datePredicate);
		final Predicate customerNotNullPredicate = builder.isNotNull(from.get(CUSTOMER));
		query.where(userPredicate, customerNotNullPredicate, builder.and(criteriaList.toArray(new Predicate[] {})));
		return addParam(builder, from, query, PLATFORM_PARAM_NAME);
	}

}
