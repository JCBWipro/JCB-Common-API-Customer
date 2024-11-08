package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId;
import com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform;

/**
 This DealerRepository contains All the Queries We Required for Dealer Dashboard Distribution Details
 */
@Repository
public interface DealerRepository extends CrudRepository<Machine, String> {
	
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where u.userName=:userName AND lower(m.platform) = lower(:platform) AND m.customer.id is not null")
	public Long countByUsersUserNamePlatform(String userName, String platform);

    @Query("SELECT count(m.vin) FROM Machine m join m.users u where u.userName=:userName AND m.renewalDate <:today AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate")
	public Long getCountByRenewalOverDueWithCustomers(String userName, Date today,Date communicatingDate);

    @Query("SELECT count(m.vin) FROM Machine m join m.users u where u.userName=:userName AND m.renewalDate >:maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate")
	public Long getCountByRenewalApproachingWithCustomers(String userName, Date maxRenewalDate,Date communicatingDate);

    @Query("SELECT count(m.vin) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate between :today and :maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate")
	public Long getCountByRenewalImmediateWithCustomers(String userName, Date today, Date maxRenewalDate,Date communicatingDate);

    @Query("SELECT count(m.vin) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate = null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate")
	public Long getCountByRenewalNoDataWithCustomers(String userName,Date communicatingDate);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId(count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName AND m.renewalDate <:today AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalOverDue(String userName, Date today,Date communicatingDate, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId(count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate >:maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalApproaching(String userName, Date maxRenewalDate,Date communicatingDate, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId(count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName AND m.renewalDate between :today and :maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalImmediate(String userName, Date today, Date maxRenewalDate, Date communicatingDate,Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId(count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate = null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalNoData(String userName,Date communicatingDate, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform(count(m.vin) as counter,m.platform) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate <:today AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalOverDue(String userName, Date today, Date communicatingDate);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId(count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName  AND m.platform =:platform AND m.renewalDate <:today AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalOverDue(String userName, Date today, String platform,Date communicatingDate, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate >:maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalApproaching(String userName, Date maxRenewalDate,Date communicatingDate);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName AND m.platform =:platform AND m.renewalDate >:maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalApproaching(String userName, Date maxRenewalDate,
			String platform,Date communicatingDate, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where u.userName=:userName AND m.renewalDate between :today and :maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalImmediate(String userName, Date today, Date maxRenewalDate,Date communicatingDate);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName  AND m.platform =:platform AND m.renewalDate between :today and :maxRenewalDate AND m.renewalDate is not null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalImmediate(String userName, Date today,
			Date maxRenewalDate, String platform,Date communicatingDate, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where u.userName=:userName  AND m.renewalDate = null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalNoData(String userName,Date communicatingDate);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName  AND m.platform =:platform AND m.renewalDate = null AND m.customer.id is not null AND m.statusAsOnTime >:communicatingDate GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalNoData(String userName, String platform, Date communicatingDate, Pageable pageable);
    
    @Query("SELECT count(DISTINCT a.vin) FROM Alert a join a.machine.users u where u.userName=:userName  AND a.isCustomerVisible = true AND a.eventLevel =:alertLevel AND a.isOpen=true AND a.machine.customer.id is not null AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service")
	public Long countMachinesByCriticalAlertsCustomer(String userName, EventLevel alertLevel);

    @Query("SELECT count (DISTINCT a.vin) FROM Alert a join a.machine.users u WHERE  a.vin NOT In (SELECT DISTINCT a.vin FROM Alert a join a.machine.users u where u.userName=:userName AND a.machine.renewalFlag = true AND a.isCustomerVisible = true AND a.eventLevel =:alertLevel AND true = a.isOpen AND a.machine.customer.id is not null) AND a.eventLevel =:alertLevel2 AND a.machine.customer.id is not null AND u.userName=:userName AND a.isOpen=true AND a.isCustomerVisible = true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service")
	public Long countMachinesByHighAlertsCustomer(String userName, EventLevel alertLevel, EventLevel alertLevel2);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId( count(DISTINCT a.vin ) as counter, a.machine.customer.id ) FROM Alert a join a.machine.users u where u.userName=:userName  AND a.isCustomerVisible = true AND a.eventLevel=:alertLevel AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType != com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getMachinesByCriticalAlertsCustomer(String userName, EventLevel alertLevel, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform( count( DISTINCT a.vin) as counter, a.machine.platform) FROM Alert a WHERE a.machine.vin In (SELECT mc.vin FROM Machine mc join mc.users u where u.userName=:userName) AND a.isCustomerVisible = true AND a.eventLevel =:alertLevel AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getAlertCountGroupByPlatform(String userName, EventLevel alertLevel);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId( count(DISTINCT a.vin ) as counter, a.machine.customer.id ) FROM Alert a WHERE a.machine.vin In (SELECT mc.vin FROM Machine mc join mc.users u where u.userName=:userName) AND a.isCustomerVisible = true AND a.eventLevel =:eventLevel AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByCountWithCustomers(String userName, EventLevel eventLevel, Pageable pageable);

    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId( count(DISTINCT a.vin ) as counter, a.machine.customer.id ) FROM Alert a WHERE a.machine.vin In (SELECT mc.vin FROM Machine mc join mc.users u where u.userName=:userName) AND a.isCustomerVisible = true AND a.eventLevel =:eventLevel AND a.machine.platform =:platform AND a.machine.customer.id is not null AND a.isOpen=true AND a.eventType!= com.wipro.jcb.livelink.app.machines.constants.EventType.Service Group by a.machine.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByCountWithCustomers(String userName, EventLevel eventLevel, String platform, Pageable pageable);
    
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m where m.vin In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where u.userName=:userName AND mu.day >=:start AND mu.day <=:end GROUP by mu.vin HAVING (sum(mu.workingHours) >=:startRange AND sum(mu.workingHours) <=:endRage ) ) AND m.customer.id is not null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByMachineUsageForDuration(String userName, Date start, Date end,
			Double startRange, Double endRage, Pageable page);

	@Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName AND m.vin NOT In (SELECT DISTINCT(mu.vin) FROM MachineUtilizationData mu join mu.machine.users u where u.userName=:userName AND mu.day >=:start AND mu.day <=:end ) AND m.customer.id is not null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByUnusedMachineForDuration(String userName, Date start, Date end, Pageable page);

	@Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m where  m.vin In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where u.userName=:userName AND mu.day >=:start AND mu.day <=:end AND mu.platform=:platform GROUP by mu.vin HAVING (sum(mu.workingHours) >=:startRange AND sum(mu.workingHours) <=:endRage) ) AND m.customer.id is not null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByMachineUsageForDuration(String userName, Date start, Date end,
			Double startRange, Double endRage, String platform, Pageable page);

	@Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where u.userName=:userName AND m.vin NOT In (SELECT DISTINCT(mu.vin) FROM MachineUtilizationData mu join mu.machine.users u where u.userName=:userName AND mu.day >=:start AND mu.day <=:end ) AND m.platform=:platform AND m.customer.id is not null GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getCustomersByUnusedMachineForDuration(String userName, Date start, Date end, String platform, Pageable page);

	@Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m where m.renewalFlag = true AND m.vin In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where u.userName=:userName AND mu.day >=:start AND mu.day <=:end GROUP by mu.vin HAVING (sum(mu.workingHours) >=:startRange AND sum(mu.workingHours) <=:endRage ) ) AND m.customer.id is not null GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getplatformsByMachineUsageForDuration(String userName, Date start, Date end, Double startRange, Double endRage);

	@Query("SELECT new com.wipro.jcb.livelink.app.machines.service.response.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where u.userName=:userName AND m.renewalFlag = true AND m.vin NOT In (SELECT mu.vin FROM MachineUtilizationData mu join mu.machine.users u where u.userName=:userName AND mu.day >=:start AND mu.day <=:end ) AND m.customer.id is not null GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getplatformsByUnusedMachineForDuration(String userName, Date start, Date end);
	
	@Query("SELECT m FROM Machine m join m.users u where u.userName=:userName  AND lower(m.customer.id) = lower(:customerName) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerName(String userName, String customerName, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where u.userName=:userName AND lower(m.customer.id) = lower(:customerId)")
	public Long countByUsersUserNameByCustomer(String userName, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameByCustomer(String userName, String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')))")
	public Long countByUsersUserNameByCustomerWithSearch(String userName, String customerId, String search);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%'))) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameByCustomerWithSearch(String userName, String customerId, String search,
			Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?2)")
	public Long countByUsersUserNamePlatformCustomer(String userName, String tabSeparator, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?2) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNamePlatformCustomer(String userName, String tabSeparator, String customerId,
			Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?2) AND lower(m.model) = lower(?3)")
	public Long countByUsersUserNamePlatformModelCustomer(String userName, String keyParam, String tabSeparator,
			String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?2) AND lower(m.model) = lower(?3) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNamePlatformModelCustomer(String userName, String keyParam, String tabSeparator,
			String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?2")
	public Long countByUsersUserNameCommunicatingMachinesCustomer(String userName, Date commDate, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?2 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameCommunicatingMachinesCustomer(String userName, Date commDate,
			String customerId, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime < ?2 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameNonCommunicatingMachinesCustomer(String userName, Date commDate,
			String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime < ?2 ")
	public Long countByUsersUserNameNonCommunicatingMachinesCustomer(String userName, Date commDate, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?2 AND m.platform = ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameCommunicatingMachinesCustomerPlatform(String userName, Date commDate,
			String customerId, String tabSeparator, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?2 AND m.platform = ?4")
	public Long countByUsersUserNameCommunicatingMachinesCustomerPlatform(String userName, Date commDate,
			String customerId, String tabSeparator);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime < ?2 AND m.platform = ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameNonCommunicatingMachinesCustomerPlatform(String userName, Date commDate,
			String customerId, String tabSeparator, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime < ?2 AND m.platform = ?4")
	public Long countByUsersUserNameNonCommunicatingMachinesCustomerPlatform(String userName, Date commDate,
			String customerId, String tabSeparator);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.transitMode = ?2 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameTransitModeMachinesCustomer(String userName, TransitMode normal,
			String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ")
	public Long getCountByCustomeridForRenewalOverDue(String userName, Date today, String customerId,
			Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalOverDue(String userName, Date today, String customerId,
			Date communicatingDate, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5")
	public Long getCountByCustomeridPlatformForRenewalOverDue(String userName, Date today, String customerId,
			String platform, Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatformForRenewalOverDue(String userName, Date today, String customerId,
			String platform, Date communicatingDate, Pageable pageable);

	// No data
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null  AND lower(m.customer.id) = lower(?2) AND m.statusAsOnTime > ?3")
	public Long getCountByCustomeridForRenewalNoData(String userName, String customerId, Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND lower(m.customer.id) = lower(?2) AND m.statusAsOnTime > ?3 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalNoData(String userName, String customerId, Date communicatingDate,
			Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND m.statusAsOnTime > ?4")
	public Long getCountByCustomeridPlatformForRenewalNoData(String userName, String customerId, String platform,
			Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND m.statusAsOnTime > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatformForRenewalNoData(String userName, String customerId, String platform,
			Date communicatingDate, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?4) AND m.statusAsOnTime > ?5")
	public Long getCountByCustomeridForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
			String customerId, Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?4) AND m.statusAsOnTime > ?5 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
			String customerId, Date communicatingDate, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND m.statusAsOnTime > ?6")
	public Long getCountByCustomeridPlatfromForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
			String customerId, String platform, Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalDate between ?2 and ?3 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?4)AND lower(m.platform) = lower(?5) AND m.statusAsOnTime > ?6 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatfromForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
			String customerId, String platform, Date communicatingDate, Pageable pageable);

	// Approaching
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND m.renewalDate is not null  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ")
	public Long getCountByCustomeridForRenewalApproaching(String userName, Date maxRenewalDate, String customerId,
			Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND m.renewalDate is not null AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalApproaching(String userName, Date maxRenewalDate, String customerId,
			Date communicatingDate, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND m.renewalDate is not null  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5 ")
	public Long getCountByCustomeridPlatfromForRenewalApproaching(String userName, Date maxRenewalDate,
			String customerId, String platfrom, Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND m.renewalDate is not null  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatfromForRenewalApproaching(String userName, Date maxRenewalDate,
			String customerId, String platfrom, Date communicatingDate, Pageable pageable);

	@Query("SELECT DISTINCT m.site FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.site) LIKE lower(concat(?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionSite(String userName, String search);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now())")
	public Long countByUsersUserNameServiceDueCustomer(String userName, Date serviceDueMaxDays,
			Double serviceDueMinHours, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND ((m.totalMachineHours >= m.serviceDueHours)  OR (m.serviceDueDate < now())) AND NOT (m.serviceDueDate = null) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceOverDueCustomer(String userName, String customerId,
			Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND ((m.totalMachineHours >= m.serviceDueHours)  OR (m.serviceDueDate < now())) AND NOT (m.serviceDueDate = null)")
	public Long countByUsersUserNameServiceOverDueCustomer(String userName, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND (m.serviceDueHours > (m.totalMachineHours + ?3))  AND (m.serviceDueDate > ?2) AND NOT (m.serviceDueDate = NULL) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceNormalCustomer(String userName, Date lastDate,
			Double serviceDueMinHours, String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND (m.serviceDueHours > (m.totalMachineHours + ?3))  AND (m.serviceDueDate > ?2) AND NOT (m.serviceDueDate = NULL)")
	public Long countByUsersUserNameServiceNormalCustomer(String userName, Date lastDate, Double serviceDueMinHours,
			String customerId);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (m.serviceDueDate = null)")
	public Long countByUsersUserNameServiceNoDataCustomer(String userName, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (m.serviceDueDate = null) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceNoDataCustomer(String userName, String customerId, Pageable pageable);

	@Query("SELECT m FROM Machine m where m.vin In (SELECT  DISTINCT(a.vin) FROM Alert a join a.machine.users u join a.machine m WHERE  ?1 = u.userName  AND lower(m.customer.id) = lower(?2)  AND a.eventLevel = ?3 AND true = a.isOpen) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndVinList(String userName, String customerName, EventLevel alertLevel,
			Pageable pageable);

	@Query("SELECT m FROM Machine m where m.vin In (SELECT  DISTINCT(a.vin)  FROM Alert a join a.machine.users u join a.machine m WHERE  ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND a.eventLevel = ?4 AND true = a.isOpen) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndVinList(String userName, String customerName, String platform,
			EventLevel alertLevel, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now()) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceDueCustomer(String userName, Date serviceDueMaxDays,
			Double serviceDueMinHours, String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now())")
	public Long countByUsersUserNameServiceDueCustomerPlatform(String userName, Date serviceDueMaxDays,
			Double serviceDueMinHours, String customerId, String platform);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now()) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceDueCustomerPlatform(String userName, Date serviceDueMaxDays,
			Double serviceDueMinHours, String customerId, String platform, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND ((m.totalMachineHours >= m.serviceDueHours)  OR (m.serviceDueDate < now())) AND NOT (m.serviceDueDate = null) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceOverDueCustomerPlatform(String userName, String customerId,
			String platform, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND ((m.totalMachineHours >= m.serviceDueHours)  OR (m.serviceDueDate < now())) AND NOT (m.serviceDueDate = null)")
	public Long countByUsersUserNameServiceOverDueCustomerPlatform(String userName, String customerId, String platform);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND (m.serviceDueHours > (m.totalMachineHours + ?3))  AND (m.serviceDueDate > ?2) AND NOT (m.serviceDueDate = null) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceNormalCustomerPlatform(String userName, Date lastDate,
			Double serviceDueMinHours, String customerId, String platform, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND (m.serviceDueHours > (m.totalMachineHours + ?3))  AND (m.serviceDueDate > ?2) AND NOT (m.serviceDueDate = null)")
	public Long countByUsersUserNameServiceNormalCustomerPlatform(String userName, Date lastDate,
			Double serviceDueMinHours, String customerId, String platform);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND (m.serviceDueDate = null) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceNoDataCustomerPlatform(String userName, String customerId,
			String platform, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND (m.serviceDueDate = null)")
	public Long countByUsersUserNameServiceNoDataCustomerPlatform(String userName, String customerId, String platform);

	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) ORDER BY m.vin DESC")
	public List<String> getByUsersUserNameAndCustomerName(String userName, String customerName);

	@Query("SELECT m FROM Machine m where  m.vin In ?1 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndVinList(List<String> vinList, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserName(String userName, Pageable pageable);

	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true ORDER BY m.vin DESC")
	public List<String> getByUsersUserName(String userName);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.transitMode = ?2 ")
	public Long countByUsersUserNameTransitModeMachinesCustomer(String userName, TransitMode normal, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.transitMode = ?2 AND m.platform = ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameTransitModeMachinesCustomerPlatform(String userName, TransitMode normal,
			String customerId, String tabSeparator, Pageable pageable);

	@Query("SELECT  count(DISTINCT a.vin)  FROM Alert a WHERE a.machine.vin In (SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) ORDER BY m.vin DESC) AND a.isCustomerVisible = true AND a.eventLevel = ?3 AND true = a.isOpen")
	public Long findbyMachineAndEventLevel(String userName, String customerName, EventLevel alertLevel);

	@Query("SELECT  count(DISTINCT a.vin)  FROM Alert a WHERE a.machine.vin In (SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3)) AND a.isCustomerVisible = true AND a.eventLevel = ?4 AND true = a.isOpen")
	public Long findbyMachineAndEventLevel(String userName, String customerName, String platform, EventLevel alertLevel);
	
}
