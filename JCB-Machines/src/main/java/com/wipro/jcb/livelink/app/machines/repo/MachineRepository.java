package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import com.wipro.jcb.livelink.app.machines.constants.EventLevel;
import com.wipro.jcb.livelink.app.machines.constants.EventType;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.MachineSummary;
import com.wipro.jcb.livelink.app.machines.entity.StakeHolder;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.enums.TransitMode;
import com.wipro.jcb.livelink.app.machines.service.reports.UserMachines;
import com.wipro.jcb.livelink.app.machines.service.response.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, String> {
    /**
     * Find Machine by vin
     *
     * @param vin is unique identity of machine for which data to be retrieved
     * @return Machine is an instance of Machine
     */
    public Machine findByVin(String vin);

    /**
     * Find Machine by vinlike
     *
     * @param vin
     *            is unique identity of machine for which data to be retrieved
     * @return Machine is an instance of Machine
     */
	/*@Query("SELECT m.vin FROM Machine m where m.renewalFlag = true AND (lower(m.vin) LIKE lower(concat('%', ?1)))")
	public String getVinDetail(String vin);

	@Query("SELECT count(m.vin) FROM Machine m where m.renewalFlag = true AND (lower(m.vin) LIKE lower(concat('%', ?1)))")
	public Integer getVinCount(String vin);


	public List<Machine> findAll();

	@Query("SELECT m FROM Machine m join m.users u where ?2 = u.userName  AND m.vin=?1")
	public Machine findByVinAndUserName(String vin, String userName);

    *//*
     * @Query("SELECT m FROM Machine m where m.user.userName = ?1 AND m.model IN ?2 AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')))"
     * ) public List<Machine> getByUserUserNameAndModelAndSearchCriteria(String
     * userName, List<String> models, String search, Pageable pageable);
     *//*
     *//**
     * Find all machines by customer-username with search criteria
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare across platform,model,location,tag and
     *            vin
     * @param pageable
     *            is a PageRequest with page-number and page-size
     * @return List<Machine> is list of instances of machine
     *//*
	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (lower(m.platform) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.site) LIKE lower(concat('%', ?2,'%')))  ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndSearchCriteria(String userName, String search, Pageable pageable);

	*//**
     * Find all Machines where dealer-username is userName , model in set of models
     * and with search criteria
     *
     * @param userName
     *            is unique identifier for dealer
     * @param filters
     *            is set of strings representing model of Machine
     * @param search
     *            is search word to compare across platform,model,location,tag and
     *            vin
     * @param pageable
     *            is a PageRequest with page-number and page-size
     * @return List<Machine> is list of instances of machine
     *//*
	//@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND (m.model IN ?2 OR m.platform IN ?2 OR m.site IN ?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%') OR lower(m.site) LIKE lower(concat('%', ?3,'%'))))  ORDER by m.statusAsOnTime DESC")
	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.model IN ?2 OR m.platform IN ?2) AND ((lower(m.location) LIKE lower(concat('%', ?3,'%'))) OR (lower(m.tag) LIKE lower(concat('%', ?3,'%'))) OR (lower(m.vin) LIKE lower(concat('%', ?3,'%'))) OR (lower(m.site) LIKE lower(concat('%', ?3,'%'))))  ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndModelAndSearchCriteria(String userName, List<String> filters,
																	 String search, Pageable pageable);

	*//**
     * Find list of distinct model form all machine
     *
     * @return List<String> is list of models
     *//*
	@Query("SELECT DISTINCT a.model FROM Machine a")
	public List<String> findDistinctModel();

	*//**
     * Find list of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on model
     * @return List<String> is list of models
     *//*
	@Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.model) LIKE lower(concat(?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionModel(String userName, String search);

	*//**
     * Find List of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on platform
     * @return List<String> is list of platform
     *//*
	@Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.platform) LIKE lower(concat(?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionPlatform(String userName, String search);

	*//**
     * Find List of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on location
     * @return List<String> is list of location
     *//*
	@Query("SELECT DISTINCT m.location FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.location) LIKE lower(concat(?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionLocation(String userName, String search);

	*//**
     * Find List of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on tag
     * @return List<String> is list of tag
     *//*
	@Query("SELECT DISTINCT m.tag FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.tag) LIKE lower(concat(?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionTag(String userName, String search);

	*//**
     * Find List of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on vin
     * @return List<String> is list of vin
     *//*
	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.vin) LIKE lower(concat('%',?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionVin(String userName, String search);*/
    /**
     * Find List of suggestion based on the machines of the dealer
     *
     * @param userName
     *            is unique identifier for dealer
     * @param search
     *            is search word to compare on model
     * @return List<String> is list of model
     */
    /*
     * @Query("SELECT DISTINCT m.model FROM Machine m where m.user.userName = ?1 AND lower(m.model) LIKE lower(concat(?2,'%'))"
     * ) public List<String> getByUserUserNameAndSuggetionModel(String userName,
     * String search);
     */
    /**
     * Find List of suggestion based on the machines of the dealer
     *
     * @param userName
     *            is unique identifier for dealer
     * @param search
     *            is search word to compare on platform
     * @return List<String> is list of platform
     */
    /*
     * @Query("SELECT DISTINCT m.platform FROM Machine m where m.user.userName = ?1 AND lower(m.platform) LIKE lower(concat(?2,'%'))"
     * ) public List<String> getByUserUserNameAndSuggetionPlatform(String userName,
     * String search);
     */
    /**
     * Find List of suggestion based on the machines of the dealer
     *
     * @param userName
     *            is unique identifier for dealer
     * @param search
     *            is search word to compare on location
     * @return List<String> is list of location
     */
    /*
     * @Query("SELECT DISTINCT m.location FROM Machine m where m.user.userName = ?1 AND lower(m.location) LIKE lower(concat(?2,'%'))"
     * ) public List<String> getByUserUserNameAndSuggetionLocation(String userName,
     * String search);
     */
    /**
     * Find List of suggestion based on the machines of the dealer
     *
     * @param userName
     *            is unique identifier for dealer
     * @param search
     *            is search word to compare on tag
     * @return List<String> is list of tag
     */
    /*
     * @Query("SELECT DISTINCT m.tag FROM Machine m where m.user.userName = ?1 AND lower(m.tag) LIKE lower(concat(?2,'%'))"
     * ) public List<String> getByUserUserNameAndSuggetionTag(String userName,
     * String search);
     */

    /**
     * Find List of suggestion based on the machines of the dealer
     *
     * @param userName
     *            is unique identifier for dealer
     * @param search
     *            is search word to compare on vin
     * @return List<String> is list of vin
     */
    /*
     * @Query("SELECT DISTINCT m.vin FROM Machine m where m.user.userName = ?1 AND lower(m.vin) LIKE lower(concat(?2,'%'))"
     * ) public List<String> getByUserUserNameAndSuggetionVin(String userName,
     * String search);
     */
    /**
     * Find all machines for a customer identified by username
     *
     * @param userName
     *            is unique identifier for customer
     * @param pageable
     *            is a PageRequest with page-number and page-size
     * @return List<Machine> is list of instances of machine
     */
	/*@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserName(String userName, Pageable pageable);

    *//*
     * @Query("SELECT m FROM Machine m where m.user.userName = ?1 ") public
     * List<Machine> getByUserUserName(String userName, Pageable pageable);
     *//*
     *//**
     * Find all machines for a customer identified by username
     *
     * @param customer
     *            is username of customer
     * @return List<Machine> is list of instances of machine
     *//*
	public List<Machine> findByUsersUserName(String customer);

	@Query("SELECT m.vin FROM Machine m join m.users u where ?1 = u.userName ")
	public List<String> findVinByUsersUserName(String userName);

    *//* public List<Machine> findByUserUserName(String delear); *//*
     *//**
     * Find all machines for customer identified by username and whose model in
     * models
     *
     * @param customer
     *            is username of customer
     * @param filters
     *            is list of filters
     * @return List<Machine> is list of instances of machine
     *//*
	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2)")
	public List<Machine> findByUsersUserNameAndModelIn(String customer, List<String> filters);

    *//*
     * public List<Machine> findByUserUserNameAndModelIn(String dealer, List<String>
     * models);
     *//*
     *//**
     * Find all machines for customer identified by username and search criteria
     *
     * @param customer
     *            is username of customer
     * @param search
     *            is search word to compare on tag and vin
     * @return List<Machine> is list of instances of machine
     *//*
	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ((lower(m.tag) LIKE lower(concat(?2,'%')) ) OR (lower(m.vin) LIKE lower(concat(?2,'%')) ))")
	public List<Machine> getByUsersUserNameAndTagOrVin(String customer, String search);

    *//*
     * @Query("SELECT m FROM Machine m where m.user.userName = ?1 AND (lower(m.tag) LIKE lower(concat('%',?2,'%')))"
     * ) public List<Machine> getByUserUserNameAndTag(String delear, String search);
     *//*
     *//**
     * Find all machines for delear identified by username and whose model in models
     *
     * @param userName
     *            is unique identifier for delear
     * @param filters
     *            is list of models
     * @param pageable
     *            is a PageRequest with page-number and page-size
     * @return List<Machine> is list of instances of machine
     *//*
	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.platform IN ?2 OR m.model IN ?2)  ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndModel(String userName, List<String> filters, Pageable pageable);

    *//*
     * @Query("SELECT m FROM Machine m where m.user.userName = ?1 AND m.model IN ?2"
     * ) public List<Machine> getByUserUserNameAndModel(String userName,
     * List<String> models, Pageable pageable);
     *//*
     *//**
     * Find all list of vins for machines belonging to a particular customer
     * identified by username
     *
     * @param userName
     *            is unique identifier for customer
     * @return List<String> is list of machines
     *//*
	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName ")
	public List<String> getDistinctUsersUserNameAndSuggetionVin(String userName);

    *//*
     * @Query("SELECT DISTINCT m.vin FROM Machine m where m.user.userName = ?1")
     * public List<String> getDistinctUserUserNameAndSuggetionVin(String userName);
     *//*
     *//**
     * Find all list of models for machines belonging to a particular customer
     * identified by username
     *
     * @param userName
     *            is unique identifier for customer
     * @return List<String> is list of machines
     *//*
	@Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName")
	public List<String> findDistinctModelForUsers(String userName);

	@Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  and m.platform!='NA'")
	public List<String> findDistinctPlatformForUsers(String userName);

	*//**
     * Find all list of models for machines belonging to a particular dealer
     * identified by username
     *
     * @param userName
     *            is unique identifier for dealer
     * @return List<String> is list of machines
     *//*
     *//*
     * @Query("SELECT DISTINCT m.model FROM Machine m where m.user.userName = ?1")
     * public List<String> findDistinctModelForUser(String userName);
     *//*
	@Query("SELECT DISTINCT m.users FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public List<User> getCustomersByUsersUserName(String userName, Pageable pageable);

	*//**
     * Find List of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on vin
     * @return List<String> is list of vin
     *//*
	@Query("SELECT DISTINCT m.customer.name FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.name) LIKE lower(concat(?2,'%')))")
	public List<String> getByUserNameAndSuggetionCustomerName(String userName, String search);

	*//**
     * Find List of suggestion based on the machines of the customer
     *
     * @param userName
     *            is unique identifier for customer
     * @param search
     *            is search word to compare on vin
     * @return List<String> is list of vin
     *//*
	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.api.response.CustomerResponse( m.customer.id, m.customer.name) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.name) LIKE lower(concat(?2,'%'))")
	public List<CustomerResponse> getByUserNameAndCustomerName(String userName, String search);


	@Query("SELECT DISTINCT m.customer.phonenumber FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.phonenumber) LIKE lower(concat(?2,'%')))")
	public List<String> getByUserPhoneNumberAndSuggetionCustomerPhone(String userName, String search);*/

    /**
     * Get all the PlatformDetails of the user
     *
     * @param userName
     *            is unique identifier for user
     * @return List<PlatformDetails> is the list of platform and count machine
     */
	/*@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.machinelocator.PlatformDetails(m.platform,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND m.transitMode = ?2 GROUP BY m.platform ORDER BY counter DESC")
	public List<PlatformDetails> getPlatformDetailsByUsersUserNameAndTrasitionModeWithoutPagination(String userName,
																									TransitMode transitMode);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName ")
	public Long getCountByUsersUserName(String userName);*/

    /**
     * Find all machines for a customer identified by username
     *
     * @param userName
     *            is unique identifier for customer
     * @param pageable
     *            is a PageRequest with page-number and page-size
     * @return List<Machine> is list of instances of machine
     */
	/*@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserName(String userName, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?3) AND m.vin = mu.vin) > ?2 GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithMachineRunningHours(String userName,
																				   Double machineRunning, String yesterday, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND m.vin IN (SELECT a.vin FROM Alert a Where a.eventType = ?2 AND a.eventGeneratedTime >= ?3 AND a.eventGeneratedTime <= ?4) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserName(String userName, EventType eventType, Date start, Date end,
															Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithoutPagination(String userName);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountry(String userName, List<String> filters,
																			 Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (SELECT mud.workingHours FROM MachineUtilizationData mud Where date(mud.day) = date(?4)  AND mc.vin = mud.vin) > ?3 ) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) AND (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?4)  AND m.vin = mu.vin) > ?3 GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryWithMachineRunningHours(String userName,
																									List<String> filters, Double machineRunning, String yesterday, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer, (SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND mc.vin IN (SELECT aa.vin FROM Alert aa Where aa.eventType = ?3 AND aa.eventGeneratedTime >= ?4 AND aa.eventGeneratedTime <= ?5) ) as counter) FROM Machine m join m.users u where ?1 = u.userName AND ( m.platform IN ?2 OR m.model IN ?2)  AND m.vin IN(SELECT a.vin FROM Alert a Where a.eventType = ?3 AND a.eventGeneratedTime >= ?4 AND a.eventGeneratedTime <= ?5) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountry(String userName, List<String> filters,
																			 EventType eventType, Date start, Date end, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) AND (lower(m.customer.name) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.site) LIKE lower(concat('%', ?3,'%')))  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryAndSearchCustomerName(String userName,
																								  List<String> filters, String search, Pageable pageable);

	*//*@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND  (SELECT mua.workingHours FROM MachineUtilizationData mua Where date(mua.day) = date(?5)  AND mc.vin = mua.vin) > ?4) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) AND  (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?5)  AND m.vin = mu.vin) > ?4 AND (lower(m.customer.name) LIKE lower(concat('%', ?3,'%')) OR  lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.site) LIKE lower(concat('%', ?3,'%')))  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryAndSearchCustomerNameWithMachineRunningHours(
			String userName, List<String> filters, String search, Double machineRunning, String yesterday,
			Pageable pageable);*//*

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND mc.vin IN (SELECT aa.vin FROM Alert aa Where aa.eventType = ?4 AND aa.eventGeneratedTime >= ?5 AND aa.eventGeneratedTime <= ?6)) as counter) FROM Machine m join m.users u where ?1 = u.userName AND ( m.platform IN ?2 OR m.model IN ?2) AND (lower(m.customer.name) LIKE lower(concat('%', ?3,'%'))) AND m.vin IN(SELECT a.vin FROM Alert a Where a.eventType = ?4 AND a.eventGeneratedTime >= ?5 AND a.eventGeneratedTime <= ?6) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryAndSearchCustomerName(String userName,
																								  List<String> filters, String customerName, EventType eventType, Date start, Date end, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')) OR lower(m.site) LIKE lower(concat('%', ?2,'%')))   GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithSearchCustomerName(String userName, String search,
																				  Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (SELECT mua.workingHours FROM MachineUtilizationData mua Where date(mua.day) = date(?4)  AND mc.vin = mua.vin) > ?3 ) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?4)  AND m.vin = mu.vin) > ?3 AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')) OR lower(m.site) LIKE lower(concat('%', ?2,'%'))) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithSearchCustomerNameWithMachineRunningHours(
			String userName, String search, Double machineRunning, String yesterday, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND mc.vin IN(SELECT aa.vin FROM Alert aa Where aa.eventType = ?3 AND aa.eventGeneratedTime >= ?4 AND aa.eventGeneratedTime <= ?5)) as counter) FROM Machine m join m.users u where ?1 = u.userName AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%'))) AND m.vin IN(SELECT a.vin FROM Alert a Where a.eventType = ?3 AND a.eventGeneratedTime >= ?4 AND a.eventGeneratedTime <= ?5) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithSearchCustomerName(String userName, String search,
																				  EventType eventType, Date start, Date end, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerName(String userName, String customerName,
																   Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ?3 < (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?4)  AND m.vin = mu.vin) ORDER by m.statusAsOnTime DESC ")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameWithMachineRunningHours(String userName,
																						  String customerName, Double machineRunning, String yesterday, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModel(String userName, String customerName,
																				 List<String> filters, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (SELECT  mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?5)  AND m.vin = mu.vin) > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelWithMachineRunningHours(String userName,
																										String customerName, List<String> filters, Double machineRunning, String yesterday, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%'))) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteria(String userName,
																								  String customerName, List<String> filters, String search, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (SELECT  mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?6)  AND m.vin = mu.vin) > ?5 AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%'))) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteriaWithMachineRunningHours(
			String userName, String customerName, List<String> filters, String search, Double machineRunning,
			String yesterday, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%'))) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndSearchCriteria(String userName,
																					String customerName, String search, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?5)  AND m.vin = mu.vin) > ?4 AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%'))) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndSearchCriteriaWithMachineRunningHours(
			String userName, String customerName, String search, Double machineRunning, String yesterday,
			Pageable pageable);

	*/

    /**
     * Find List of machines
     *
     * @param entities is unique identifier for customer
     * @return List<String> is list of vin
     *//*
	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) ORDER BY m.vin DESC")
	public List<String> getByUsersUserNameAndCustomerName(String userName, String customerName);

	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true ORDER BY m.vin DESC")
	public List<String> getByUsersUserName(String userName);


	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) ORDER BY m.vin DESC")
	public List<String> getByUsersUserNameAndCustomerNameAndModelIn(String userName, String customerName,
																	List<String> filters);

	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (lower(m.tag) LIKE lower(concat('%',?3,'%'))) ORDER BY m.vin DESC")
	public List<String> getByUsersUserNameAndCustomerNameAndTag(String userName, String customerName, String search);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.api.response.dealer.machinelocator.PlatformDetails(m.platform,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.platform ORDER BY m.platform DESC")
	public List<PlatformDetails> getPlatformDetailsByUsersUserNameWithoutPagination(String userName);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.api.response.dealer.machinelocator.PlatformDetails(m.platform,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.statusAsOnTime <= ?2) GROUP BY m.platform ORDER BY m.platform DESC")
	public List<PlatformDetails> getPlatformDetailsByUsersUserNameAndNotConnectingWithoutPagination(String userName,
																									Date previousDate);*/

    //@Override
    @Async
    public <S extends Machine> Iterable<S> save(Iterable<S> entities);

    @Override
    public <S extends Machine> S save(S entity);

	/*@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND m.vin IN(SELECT a.vin FROM Alert a Where a.eventType = ?2 AND a.eventGeneratedTime >= ?3 AND a.eventGeneratedTime <= ?4)  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> test(String userName, EventType eventType, Date start, Date end, Pageable pageable);
*/
	/*@Query("SELECT DISTINCT m.customer.name FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.phonenumber) LIKE lower(concat(?2,'%')))")
	public List<String> getByUserNameAndSuggetionCustomerPhoneNumber(String userName, String search);

	// Dealer home API queries
	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now())   GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameServiceOverdueMachines(String userName, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?3 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?2 ) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameServiceDueMachines(String userName, Date endDate,
																			  Double minDueHours, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now())")
	public Integer getCountByUsersUserNameAndServiceOverdue(String userName);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?3 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?2 )")
	public Integer getCountByUsersUserNameAndServiceDue(String userName, Date endDate, Double minDueHours);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND ( m.platform IN ?3 OR m.model IN ?3) AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%'))) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteriaServiceOverDue(
			String userName, String customerName, List<String> filters, String search, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelServiceOverDue(String userName,
																							   String customerName, List<String> filters, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND lower(m.customer.id) = lower(?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%'))) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndSearchCriteriaServiceOverDue(String userName,
																								  String customerName, String search, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND lower(m.customer.id) = lower(?2) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameServiceOverDue(String userName, String customerName,
																				 Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (m.serviceDueHours - ?6 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?5 ) AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%'))) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteriaServiceDue(
			String userName, String customerName, List<String> filters, String search, Date endDate, Double minDueHours,
			Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (m.serviceDueHours - ?5 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?4 ) AND ( m.platform IN ?3 OR m.model IN ?3) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndFilterModelServiceDue(String userName,
																						   String customerName, List<String> filters, Date endDate, Double minDueHours, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?5 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?4 ) AND lower(m.customer.id) = lower(?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%'))) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameAndSearchCriteriaServiceDue(String userName,
																							  String customerName, String search, Date endDate, Double minDueHours, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?4 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?3 ) AND lower(m.customer.id) = lower(?2) ORDER BY m.vin DESC")
	public List<Machine> getMachinesByUsersUserNameAndCustomerNameServiceDue(String userName, String customerName,
																			 Date endDate, Double minDueHours, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND ( m.platform IN ?3 OR m.model IN ?3) AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%')))")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteriaServiceOverDue(
			String userName, String customerName, List<String> filters, String search);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now())")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndFilterModelServiceOverDue(String userName,
																							  String customerName, List<String> filters);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND lower(m.customer.id) = lower(?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')))")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndSearchCriteriaServiceOverDue(String userName,
																								 String customerName, String search);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND lower(m.customer.id) = lower(?2)")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameServiceOverDue(String userName, String customerName);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (m.serviceDueHours - ?6 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?5 ) AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%')))")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteriaServiceDue(
			String userName, String customerName, List<String> filters, String search, Date endDate,
			Double minDueHours);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (m.serviceDueHours - ?5 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?4 ) AND ( m.platform IN ?3 OR m.model IN ?3)")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndFilterModelServiceDue(String userName,
																						  String customerName, List<String> filters, Date endDate, Double minDueHours);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?5 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?4 ) AND lower(m.customer.id) = lower(?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')))")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndSearchCriteriaServiceDue(String userName,
																							 String customerName, String search, Date endDate, Double minDueHours);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?4 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?3 ) AND lower(m.customer.id) = lower(?2)")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameServiceDue(String userName, String customerName,
																			Date endDate, Double minDueHours);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3) AND (lower(m.platform) LIKE lower(concat('%', ?4,'%')) OR lower(m.model) LIKE lower(concat('%', ?4,'%')) OR lower(m.location) LIKE lower(concat('%', ?4,'%')) OR lower(m.tag) LIKE lower(concat('%', ?4,'%')) OR lower(m.vin) LIKE lower(concat('%', ?4,'%')))")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndFilterModelAndSearchCriteria(String userName,
																								 String customerName, List<String> filters, String search);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND ( m.platform IN ?3 OR m.model IN ?3)")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndFilterModel(String userName, String customerName,
																				List<String> filters);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND (lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')))")
	public Integer getCountMachinesByUsersUserNameAndCustomerNameAndSearchCriteria(String userName, String customerName,
																				   String search);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2)")
	public Integer getCountMachinesByUsersUserNameAndCustomerName(String userName, String customerName);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2)  AND (lower(m.customer.name) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')))  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryAndSearchCustomerNameAllMachines(
			String userName, List<String> filters, String search, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (m.serviceDueHours - ?5 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?4 ) ) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2)   AND (m.serviceDueHours > m.totalMachineHours) AND (m.serviceDueDate between now() and ?4 ) AND (lower(m.customer.name) LIKE lower(concat('%', ?3,'%')) OR lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')))  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryAndSearchCustomerNameServiceDue(
			String userName, List<String> filters, String search, Date endDate, Double minDueHours, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (mc.serviceOverDueHours < 0.0 OR mc.serviceDueDate < now()) ) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND (lower(m.customer.name) LIKE lower(concat('%', ?3,'%')) OR  lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')))  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryAndSearchCustomerNameServiceOverDue(
			String userName, List<String> filters, String search, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2)  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryWithAllMachines(String userName,
																							List<String> filters, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (mc.serviceOverDueHours < 0.0 OR mc.serviceDueDate < now()) ) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND ( m.platform IN ?2 OR m.model IN ?2)  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryWithServiceOverDue(String userName,
																							   List<String> filters, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (m.serviceDueHours - ?4 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?3 )) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours > m.totalMachineHours) AND (m.serviceDueDate between now() and ?3 ) AND ( m.platform IN ?2 OR m.model IN ?2)  GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithFilterCountryWithServiceDue(String userName,
																						   List<String> filters, Date endDate, Double minDueHours, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')))   GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithSearchCustomerNameWithAllMachine(String userName,
																								String search, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (mc.serviceOverDueHours < 0.0 OR mc.serviceDueDate < now())) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR  lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')))   GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithSearchCustomerNameWithServiceOverDue(String userName,
																									String search, Pageable pageable);

	@Query("SELECT DISTINCT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,(SELECT count(mc.vin) as c FROM Machine mc Where mc.customer = m.customer AND mc.dealer = m.dealer AND (m.serviceDueHours - ?4 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?3 )) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours > m.totalMachineHours) AND (m.serviceDueDate between now() and ?3 ) AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR  lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')))   GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithSearchCustomerNameWithServiceDue(String userName,
																								String search, Date endDate, Double minDueHours, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithAllMachines(String userName, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.serviceDueHours - ?3 > m.totalMachineHours) AND (m.serviceDueDate between now() and ?2 ) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithServiceDue(String userName, Date endDate,
																		  Double minDueHours, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (m.totalMachineHours >= m.serviceDueHours OR m.serviceDueDate < now()) GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
	public List<StakeHolder> getStakeHoldersByUsersUserNameWithServiceOverDue(String userName, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (SELECT mu.workingHours FROM MachineUtilizationData mu Where date(mu.day) = date(?3)  AND m.vin = mu.vin) > ?2 ")
	public List<Machine> test(String userName, Double machineRunning, String yesterday);

	// Customer report queries
	@Query("SELECT sum(m.totalMachineHours) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public Double getTotalMachineHoursForAllMachines(String userName);

	@Query("SELECT sum(m.totalMachineHours) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND m.vin IN ?2")
	public Double getTotalMachineHoursForListedMachines(String userName, List<String> vinList);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.UserMachines(m.vin,m.model,m.platform,m.tag,m.thumbnail,m.location) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public List<UserMachines> getByUsersUserNameMachines(String userName, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.UserMachines(m.vin,m.model,m.platform,m.tag,m.thumbnail,m.location) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.platform) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')))")
	public List<UserMachines> getByUsersUserNameAndSearchOption(String userName, String search, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.UserMachines(m.vin,m.model,m.platform,m.tag,m.thumbnail,m.location) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) AND (lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')))")
	public List<UserMachines> getByUsersUserNameMachinesFileterAndSearch(String userName, List<String> filterList,
																		 String search, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.customer.reports.UserMachines(m.vin,m.model,m.platform,m.tag,m.thumbnail,m.location) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND ( m.platform IN ?2 OR m.model IN ?2) ")
	public List<UserMachines> getByUsersUserNameMachinesFileterOption(String userName, List<String> filter,
																	  Pageable pageable);

	// Dealer Dashboard queries
	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.customer.id ORDER BY counter DESC")
	public List<StakeHolder> getCustomersByUsersUserNameWithAllMachines(String userName, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.common.StakeHolder(m.customer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (lower(m.customer.name) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%'))) GROUP BY m.customer.id ORDER BY counter DESC")
	public List<StakeHolder> getCustomersByUsersUserNameWithAllMachinesWithSearch(String userName, String search,
																				  PageRequest pageRequest);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?2) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNamePlatformCustomer(String userName, String tabSeparator, String customerId,
															Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?2)")
	public Long countByUsersUserNamePlatformCustomer(String userName, String tabSeparator, String customerId);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.platform) = lower(?2) AND NOT m.customer.id = null")
	public Long countByUsersUserNamePlatform(String userName, String platform);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?2) AND lower(m.model) = lower(?3) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNamePlatformModelCustomer(String userName, String keyParam, String tabSeparator,
																 String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?2) AND lower(m.model) = lower(?3)")
	public Long countByUsersUserNamePlatformModelCustomer(String userName, String keyParam, String tabSeparator,
														  String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now()) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceDueCustomer(String userName, Date serviceDueMaxDays,
															  Double serviceDueMinHours, String customerId, Pageable pageable);

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

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (m.serviceDueDate = null) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceNoDataCustomer(String userName, String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (m.serviceDueDate = null)")
	public Long countByUsersUserNameServiceNoDataCustomer(String userName, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now()) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameServiceDueCustomerPlatform(String userName, Date serviceDueMaxDays,
																	  Double serviceDueMinHours, String customerId, String platform, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?4) AND lower(m.platform) = lower(?5) AND ((m.serviceDueHours < (m.totalMachineHours+ ?3))  OR (m.serviceDueDate between now() and ?2 )) AND NOT (m.serviceDueDate = null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= now())")
	public Long countByUsersUserNameServiceDueCustomerPlatform(String userName, Date serviceDueMaxDays,
															   Double serviceDueMinHours, String customerId, String platform);

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

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameByCustomer(String userName, String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2)")
	public Long countByUsersUserNameByCustomer(String userName, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%'))) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameByCustomerWithSearch(String userName, String customerId, String search,
																Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND (lower(m.model) LIKE lower(concat('%', ?3,'%')) OR lower(m.platform) LIKE lower(concat('%', ?3,'%')) OR lower(m.location) LIKE lower(concat('%', ?3,'%')) OR lower(m.vin) LIKE lower(concat('%', ?3,'%')) OR lower(m.tag) LIKE lower(concat('%', ?3,'%')))")
	public Long countByUsersUserNameByCustomerWithSearch(String userName, String customerId, String search);

	@Query("SELECT m FROM Machine m where  m.vin In ?1 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndVinList(List<String> vinList, Pageable pageable);

	@Query("SELECT m FROM Machine m where m.renewalFlag = true AND m.vin In ?1 ")
	public List<Machine> getMachinesByVinList(List<String> vinList, Pageable pageable);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND m.vin NOT In ?2 ")
	public List<Machine> getMachinesNotByVinList(String userName, List<String> vinList, Pageable pageable);

	@Query(value="SELECT m.vin FROM machine m JOIN machin_user u ON m.vin = u.vin JOIN machine_feedparser_data mf ON m.vin = mf.vin WHERE u.user_id = ?1 AND m.renewal_flag =true AND m.vin NOT IN ?2 ORDER BY mf.total_machine_hours desc",nativeQuery = true)
	public List<String> getMachinesNotINByVinListOrderDesc(String userName, List<String> vinList);

	@Query(value="SELECT m.vin FROM machine m JOIN machin_user u ON m.vin = u.vin JOIN machine_feedparser_data mf ON m.vin = mf.vin WHERE u.user_id = ?1 AND m.renewal_flag =true AND m.vin NOT IN ?2 ORDER BY mf.total_machine_hours asc",nativeQuery = true)
	public List<String> getMachinesNotINByVinListOrderAsc(String userName, List<String> vinList);

	@Query(value="SELECT m.vin FROM machine m JOIN machin_user u ON m.vin = u.vin JOIN machine_feedparser_data mf ON m.vin = mf.vin WHERE u.user_id = ?1 AND m.renewal_flag =true ORDER BY mf.total_machine_hours desc",nativeQuery = true)
	public List<String> getMachinesByUsernameOrderDesc(String userName);

	@Query(value="SELECT m.vin FROM machine m JOIN machin_user u ON m.vin = u.vin JOIN machine_feedparser_data mf ON m.vin = mf.vin WHERE u.user_id = ?1 AND m.renewal_flag =true ORDER BY mf.total_machine_hours asc",nativeQuery = true)
	public List<String> getMachinesByUsernameOrderAsc(String userName);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public List<Machine> getMachinesByUsername(String userName, Pageable pageable);

	@Query("SELECT m FROM Machine m where m.vin In (SELECT  DISTINCT(a.vin)  FROM Alert a join a.machine.users u join a.machine m WHERE  ?1 = u.userName  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND a.eventLevel = ?4 AND true = a.isOpen) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndVinList(String userName, String customerName, String platform,
													  EventLevel alertLevel, Pageable pageable);

	@Query("SELECT m FROM Machine m where m.vin In (SELECT  DISTINCT(a.vin)  FROM Alert a join a.machine.users u join a.machine m WHERE  ?1 = u.userName  AND lower(m.customer.id) = lower(?2)  AND a.eventLevel = ?3 AND true = a.isOpen) ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameAndVinList(String userName, String customerName, EventLevel alertLevel,
													  Pageable pageable);

	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3)")
	public List<String> getByUsersUserNameAndCustomerNameAndPlatform(String userName, String customerName,
																	 String platform);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?2 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameCommunicatingMachinesCustomer(String userName, Date commDate,
																		 String customerId, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?2")
	public Long countByUsersUserNameCommunicatingMachinesCustomer(String userName, Date commDate, String customerId);

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

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.transitMode = ?2 ")
	public Long countByUsersUserNameTransitModeMachinesCustomer(String userName, TransitMode normal, String customerId);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.id) = lower(?3) AND m.transitMode = ?2 AND m.platform = ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByUsersUserNameTransitModeMachinesCustomerPlatform(String userName, TransitMode normal,
																			   String customerId, String tabSeparator, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND m.statusAsOnTime > ?3")
	public Long getCountByRenewalOverDue(String userName, Date today,Date communicatingDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND m.statusAsOnTime > ?3")
	public Long getCountByRenewalApproaching(String userName, Date maxRenewalDate,Date communicatingDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null")
	public Long getCountByRenewalNoData(String userName);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND m.statusAsOnTime > ?4")
	public Long getCountByRenewalImmediate(String userName, Date today, Date maxRenewalDate,Date communicatingDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 ")
	public Long getCountByRenewalOverDueWithCustomers(String userName, Date today,Date communicatingDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 ")
	public Long getCountByRenewalApproachingWithCustomers(String userName, Date maxRenewalDate,Date communicatingDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?2")
	public Long getCountByRenewalNoDataWithCustomers(String userName,Date communicatingDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?4")
	public Long getCountByRenewalImmediateWithCustomers(String userName, Date today, Date maxRenewalDate,Date communicatingDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalOverDue(String userName, Date today, Date communicatingDate,
																		 Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalApproaching(String userName, Date maxRenewalDate,Date communicatingDate,
																			 Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?2 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalNoData(String userName,Date communicatingDate, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?4 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalImmediate(String userName, Date today,
																		   Date maxRenewalDate, Date communicatingDate,Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalOverDue(String userName, Date today, Date communicatingDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.platform = ?3 AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?4 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalOverDue(String userName, Date today, String platform,Date communicatingDate,
																		 Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalApproaching(String userName, Date maxRenewalDate,Date communicatingDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.platform = ?3 AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?4 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalApproaching(String userName, Date maxRenewalDate,
																			 String platform,Date communicatingDate, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?2 GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalNoData(String userName,Date communicatingDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.platform = ?2 AND m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?3 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalNoData(String userName, String platform,Date communicatingDate,
																		Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithPlatform (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?4 GROUP BY m.platform ORDER BY counter DESC")
	public List<MachineWithPlatform> getByPlatformForRenewalImmediate(String userName, Date today, Date maxRenewalDate,Date communicatingDate);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.details.MachineWithCustomerId (count(m.vin) as counter,m.customer.id) FROM Machine m join m.users u where ?1 = u.userName  AND m.platform = ?4 AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND NOT m.customer.id = null AND m.statusAsOnTime > ?5 GROUP BY m.customer.id ORDER BY counter DESC")
	public List<MachineWithCustomerId> getByAllCustomerForRenewalImmediate(String userName, Date today,
																		   Date maxRenewalDate, String platform,Date communicatingDate, Pageable pageable);

	// OverDue
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ")
	public Long getCountByCustomeridForRenewalOverDue(String userName, Date today, String customerId,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalOverDue(String userName, Date today, String customerId,Date communicatingDate,
														  Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5")
	public Long getCountByCustomeridPlatformForRenewalOverDue(String userName, Date today, String customerId,
															  String platform,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatformForRenewalOverDue(String userName, Date today, String customerId,
																  String platform,Date communicatingDate, Pageable pageable);

	// No data
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null  AND lower(m.customer.id) = lower(?2) AND m.statusAsOnTime > ?3")
	public Long getCountByCustomeridForRenewalNoData(String userName, String customerId,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND lower(m.customer.id) = lower(?2) AND m.statusAsOnTime > ?3 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalNoData(String userName, String customerId,Date communicatingDate,Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null  AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND m.statusAsOnTime > ?4")
	public Long getCountByCustomeridPlatformForRenewalNoData(String userName, String customerId, String platform,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null AND lower(m.customer.id) = lower(?2) AND lower(m.platform) = lower(?3) AND m.statusAsOnTime > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatformForRenewalNoData(String userName, String customerId, String platform,Date communicatingDate,
																 Pageable pageable);

	// immidiate
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?4) AND m.statusAsOnTime > ?5")
	public Long getCountByCustomeridForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
														String customerId,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?4) AND m.statusAsOnTime > ?5 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
															String customerId,Date communicatingDate, Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?4)AND lower(m.platform) = lower(?5) AND m.statusAsOnTime > ?6")
	public Long getCountByCustomeridPlatfromForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
																String customerId, String platform,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?4)AND lower(m.platform) = lower(?5) AND m.statusAsOnTime > ?6 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatfromForRenewalImmediate(String userName, Date today, Date maxRenewalDate,
																	String customerId, String platform,Date communicatingDate, Pageable pageable);
	// Approaching
	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null  AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ")
	public Long getCountByCustomeridForRenewalApproaching(String userName, Date maxRenewalDate, String customerId,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND lower(m.customer.id) = lower(?3) AND m.statusAsOnTime > ?4 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridForRenewalApproaching(String userName, Date maxRenewalDate, String customerId,Date communicatingDate,
															  Pageable pageable);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5 ")
	public Long getCountByCustomeridPlatfromForRenewalApproaching(String userName, Date maxRenewalDate,
																  String customerId, String platfrom,Date communicatingDate);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null  AND lower(m.customer.id) = lower(?3) AND lower(m.platform) = lower(?4) AND m.statusAsOnTime > ?5 ORDER by m.statusAsOnTime DESC")
	public List<Machine> getByCustomeridPlatfromForRenewalApproaching(String userName, Date maxRenewalDate,
																	  String customerId, String platfrom,Date communicatingDate, Pageable pageable);

	@Query("SELECT DISTINCT m.site FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.site) LIKE lower(concat(?2,'%'))")
	public List<String> getByUsersUserNameAndSuggetionSite(String userName, String search);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND (((m.totalMachineHours+50) > m.serviceDueHours) OR (m.serviceDueDate BETWEEN ?2 AND ?3)) AND (m.serviceDueDate is not null) AND (m.totalMachineHours < m.serviceDueHours) AND (m.serviceDueDate >= ?2)")
	public Long getCountByserviceDue(String userName, Date today,Date maxDueDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND( (m.totalMachineHours >= m.serviceDueHours) OR (m.serviceDueDate < ?2)) AND (m.serviceDueDate is not null)")
	public Long getCountByserviceOverDue(String userName, Date today);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND (m.serviceDueHours >(m.totalMachineHours+50)) AND (m.serviceDueDate > ?2) AND (m.serviceDueDate is not null)")
	public Long getCountByserviceNormal(String userName, Date maxDueDate);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND (m.serviceDueDate is null)")
	public Long getCountByserviceNoData(String userName);
	//@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.machinelocator.PlatformDetails(m.platform,count(m) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.transitMode = ?2 GROUP BY m.platform ORDER BY counter DESC")
	*//*@Query("SELECT new com.jcb.livelinkappserver.api.response.dealer.dashboard.MachineCountWIthCategory (count(m.vin) as counter,m.platform) FROM Machine m join m.users u where ?1 = u.userName group by m.platform order by counter desc")
	public List<MachineCountWIthCategory> getCountByPlatform(String userName);*//*


	@Query(value="SELECT count(vin) FROM machin_user where user_id = :userName",nativeQuery=true)
	public Long getMachineCountByUserName(@Param("userName") String userName);


	@Transactional
	@Modifying
	@Query(value="insert into machin_user (vin,user_id) select vin ,:userName from machine",nativeQuery=true)
	public void mapAllMachineTouser(@Param("userName") String userName);

	@Transactional
	@Modifying
	@Query(value="insert into machin_user (vin,user_id) select m.vin ,:userName from machine m left join (select u.vin from machin_user u where u.user_id=:userName) x on m.vin=x.vin where x.vin is null",nativeQuery=true)
	public void mapMachineTouser(@Param("userName") String userName);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName")
	public Long countByUsersUserName(String userName);

	public Long countByRenewalFlag(boolean renewalFlag);

	@Query(value = "SELECT m.vin from machine m left join (SELECT vin from alert where ?1=event_name) res on res.vin=m.vin where (m.start_time like '%AM' "
			+ "or m.start_time like '%PM') AND (m.end_time like '%AM' or m.end_time like '%PM') and res.vin is null order by m.vin", nativeQuery = true)
	public List<String> findTimeFenceMachines(String eventName);

	@Query(value = "SELECT m.vin from machine m left join (SELECT vin from alert where ?1=event_name) res on res.vin=m.vin where m.radius > 0.0 "
			+ "AND m.center_lat > 0.0 and m.center_long > 0.0 and res.vin is null order by m.vin", nativeQuery = true)
	public List<String> findGeoFenceMachines(String eventName);*/

    @Query(value = "SELECT m FROM Machine m join m.users u where ?2 = u.userName  AND m.vin=?1", nativeQuery = true)
    public Machine findByVinAndUserName(String vin, String userName);

    @Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName")
    public List<String> findDistinctModelForUsers(String userName);

    @Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  and m.platform!='NA'")
    public List<String> findDistinctPlatformForUsers(String userName);

    @Query(value = "SELECT m.vin as vin, m.model as model, m.platform as platform, mfd.total_machine_hours as totalmachinehours,c.name as customername,c.phonenumber as customernumber,d.name as dealername,d.phonenumber as dealernumber  FROM machine m join customer c on c.id=m.customer_username join dealer d on d.id= m.dealer_username left outer join machine_feedparser_data mfd on mfd.vin = m.vin where m.vin =:vin AND m.renewal_flag =true ", nativeQuery = true)
    public List<Object[]> fetchMachineDetails(@Param("vin") String vin);

    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName")
    public Long getCountByUserName(String userName);

    @Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND  (m.platform IN ?2 OR m.model IN ?2)  ORDER by m.machineFeedParserData.statusAsOnTime DESC")
    public List<Machine> getByUsersUserNameAndModelByCustomer(String userName, List<String> filters, Pageable pageable);

    @Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND  (m.model IN ?2 OR m.platform IN ?2) AND ((lower(m.location) LIKE lower(concat('%', ?3,'%'))) OR (lower(m.tag) LIKE lower(concat('%', ?3,'%'))) OR (lower(m.vin) LIKE lower(concat('%', ?3,'%'))) OR (lower(m.site) LIKE lower(concat('%', ?3,'%'))))  ORDER by m.machineFeedParserData.statusAsOnTime DESC")
    public List<Machine> getByUsersUserNameAndModelAndSearchCriteriaByCustomer(String userName, List<String> filters,
                                                                               String search, Pageable pageable);

    @Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  ORDER by m.machineFeedParserData.statusAsOnTime DESC")
    public List<Machine> getByUsersUserNameByCustomer(String userName, Pageable pageable);

    @Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName  AND (lower(m.platform) LIKE lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE lower(concat('%', ?2,'%')) OR lower(m.site) LIKE lower(concat('%', ?2,'%')))  ORDER by m.machineFeedParserData.statusAsOnTime DESC")
    public List<Machine> getByUsersUserNameAndSearchCriteriaByCustomer(String userName, String search, Pageable pageable);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.RdMachineDetails(m.vin, substring(m.imeiNumber,5),m.customerNumber,m.customerName,m.dealerName,m.dealerNumber) FROM Machine m WHERE m.renewalFlag = true AND m.vin = ?1", nativeQuery = true)
    public RdMachineDetails getMachinesDetails(String vin);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.RdVinImeiResponse(m.vin, substring(m.imeiNumber,5)) FROM Machine m WHERE m.renewalFlag = true and m.rolloffDate>= ?1 and m.rolloffDate <= ?2", nativeQuery = true)
    public List<RdVinImeiResponse> getNewMachines(Date startDate, Date endDate);

    @Query(value = "SELECT firmware_version FROM machine where vin = :vin", nativeQuery = true)
    public String getFirmwareVersionByVin(@Param("vin") String vin);


	/*@Query(value="SELECT firmware_version FROM machine where vin in ( :vinList) limit 1",nativeQuery=true)
	public String getFirmwareVersionByVinUsingIn(@Param("vinList") List<String> vinList);

	@Transactional
	@Modifying
	@Query(value = "delete from machin_user where vin = :vin and user_id = :userName", nativeQuery = true)
	public void deleteUserMapping(@Param("vin") String vin,@Param("userName") String userName);

	@Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public Long getCountByUsersUserNameForCustomer(String userName);

	@Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  and m.platform!='NA' AND m.renewalFlag = true")
	public List<String> findDistinctPlatformForUsersForCustomer(String userName);

	@Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public List<String> findDistinctModelForCusomter(String userName);


	@Query(value = "select vin from (select max(cast(NULLIF(split_part(firmware_version, '.',1), '') AS int)),vin  from Machine group by vin) s where max>=7 and max<30", nativeQuery = true)
	public List<String> getVinList();

	@Query("select m.vin FROM Machine m join m.users u where ?1 = u.userName and m.machineType='premium'")
	public List<String> findPremiumMachineByUser(@Param("userName") String userName);

	@Query("select m.vin FROM Machine m  where m.vin In ?1  and m.premiumFlag='Premium'")
	public List<String> findVinByMachine(List<String> vinList);

	@Query("select m.vin FROM Machine m  where m.vin In ?1  and m.premiumFlag='Standard'")
	public List<String> getStandardMachine(List<String> vinList);

	@Query("select m.vin FROM Machine m join m.users u where ?1 = u.userName and m.machineType='Standard'")
	public List<String> findStandardMachineByUser(@Param("userName") String userName);


	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true")
	public List<Machine> getMachineDetailsByUserName(String userName);

	@Query("SELECT new com.jcb.livelinkappserver.api.response.RdMachineDetails(m.vin, substring(m.imeiNumber,5),m.customerNumber,m.customerName,m.dealerName,m.dealerNumber) FROM Machine m WHERE m.renewalFlag = true AND m.vin = ?1")
	public RdMachineDetails getMachinesDetails(String vin);

	@Modifying
	@Query(value="update machine set premium_flag =?1 where vin =?2",nativeQuery = true)
	public void updatePremiumFlag(String premiumFlag, String vin);

	@Query(value="select count(distinct (customer_username)) from machine where vin in (select vin from machin_user where user_id=?1) and renewal_flag=true",nativeQuery = true)
	public Long getCustomersByUsersUserNameWithAllMachinesCount(String userName);

	@Query("SELECT m FROM Machine m where (lower(m.vin) LIKE lower(concat('%', ?1)))")
	public Machine getMachineDetail(String vin);

	@Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where  lower(m.vin) LIKE lower(concat('%',?1,'%'))")
	public List<String> getSuggetionVin(String word);

	@Query("SELECT m FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true ORDER by m.statusAsOnTime DESC")
	public List<Machine> getMachineDetailsByUserName(String userName, Pageable pageable);

	@Query("SELECT new com.jcb.livelinkappserver.domain.MachineSummary(fuelLevel,latitude,longitude,location,model,platform,statusAsOnTime,totalMachineHours,vin,image,thumbnail,tag,site) FROM Machine  WHERE  vin = ?1")
	public MachineSummary getMachineDetailForPush(String vin);*/


}