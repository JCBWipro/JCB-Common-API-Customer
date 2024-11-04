package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
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
	
}
