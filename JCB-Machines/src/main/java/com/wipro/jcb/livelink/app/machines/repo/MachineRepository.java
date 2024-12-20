package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.StakeHolder;
import com.wipro.jcb.livelink.app.machines.service.response.RdMachineDetails;
import com.wipro.jcb.livelink.app.machines.service.response.RdVinImeiResponse;
import com.wipro.jcb.livelink.app.machines.service.response.UserResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public interface MachineRepository extends CrudRepository<Machine, String> {
    /**
     * Find Machine by vin
     *
     * @param vin is unique identity of machine for which data to be retrieved
     * @return Machine is an instance of Machine
     */
    Machine findByVin(String vin);

    @Query(value = "SELECT m.*, mu.user_id  FROM machine m JOIN machin_user mu ON m.vin = mu.vin WHERE mu.user_id = ?2 AND m.vin = ?1", nativeQuery = true)
    Machine findByVinAndUserName(String vin, String userName);

    @Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName")
    public List<String> findDistinctModelForUsers(String userName);

    @Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  and m.platform!='NA'")
    public List<String> findDistinctPlatformForUsers(String userName);

    @Query(value = "SELECT m.vin as vin, m.model as model, m.platform as platform, mfd.total_machine_hours as totalmachinehours,c.name as customername,c.phonenumber as customernumber,d.name as dealername,d.phonenumber as dealernumber  FROM machine m join customer c on c.id=m.customer_username join dealer d on d.id= m.dealer_username left outer join machine_feedparser_data mfd on mfd.vin = m.vin where m.vin =:vin AND m.renewal_flag =true ", nativeQuery = true)
    public List<Object[]> fetchMachineDetails(@Param("vin") String vin);

    @Query(value = "SELECT count(m.vin) FROM machine m join machin_user u where user_id=:userName", nativeQuery = true)
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

    @Query(value = "SELECT USER_ID ,password, roleName FROM microservices_db.LiveLinkUser where USER_ID=:userName", nativeQuery = true)
    UserResponse findByContactId(@Param("userName") String userName);

    @Query(value = "SELECT m.vin FROM machine m join machin_user mu ON m.vin = mu.vin where mu.user_id = ?1", nativeQuery = true)
    List<String> findVinByUsersUserName(String userName);

    @Modifying
    @Query(value = "delete from machin_user where vin = :vin and user_id = :userName", nativeQuery = true)
    void deleteUserMapping(@Param("vin") String vin, @Param("userName") String userName);

    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName")
    Long countByUsersUserName(String userName);

    @Modifying
    @Query(value = "insert into machin_user (vin,user_id) select vin ,:userName from machine", nativeQuery = true)
    void mapAllMachineTouser(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query(value = "insert into machin_user (vin,user_id) select m.vin ,:userName from machine m left join (select u.vin from machin_user u where u.user_id=:userName) x on m.vin=x.vin where x.vin is null", nativeQuery = true)
    void mapMachineToUser(@Param("userName") String userName);

    @Query("SELECT DISTINCT a.model FROM Machine a")
    List<String> findDistinctModel();

    @Query("SELECT DISTINCT m.tag FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.tag) LIKE lower(concat(?2,'%'))")
    public List<String> getByUsersUserNameAndSuggestionTag(String userName, String search);

    @Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.vin) LIKE lower(concat('%',?2,'%'))")
    public List<String> getByUsersUserNameAndSuggestionVin(String userName, String search);

    @Query("SELECT DISTINCT m.vin FROM Machine m join m.users u where ?1 = u.userName ")
    public List<String> getDistinctUsersUserNameAndSuggestionVin(String userName);

    @Query("SELECT DISTINCT m.site FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.site) LIKE lower(concat(?2,'%'))")
    public List<String> getByUsersUserNameAndSuggestionSite(String userName, String search);

    @Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.model) LIKE lower(concat(?2,'%'))")
    public List<String> getByUsersUserNameAndSuggestionModel(String userName, String search);

    @Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.platform) LIKE lower(concat(?2,'%'))")
    public List<String> getByUsersUserNameAndSuggestionPlatform(String userName, String search);

    @Query("SELECT DISTINCT m.location FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.location) LIKE lower(concat(?2,'%'))")
    public List<String> getByUsersUserNameAndSuggestionLocation(String userName, String search);

    @Query("SELECT DISTINCT m.customer.name FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.name) LIKE lower(concat(?2,'%'))")
    public List<String> getByUserNameAndSuggestionCustomerName(String userName, String search);

    @Query("SELECT DISTINCT m.customer.phonenumber FROM Machine m join m.users u where ?1 = u.userName  AND lower(m.customer.phonenumber) LIKE lower(concat(?2,'%'))")
    public List<String> getByUserPhoneNumberAndSuggestionCustomerPhone(String userName, String search);

    @Query("SELECT DISTINCT new com.wipro.jcb.livelink.app.machines.entity.StakeHolder(m.customer,m.dealer,count(m.vin) as counter) FROM Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.customer.id,m.dealer.id ORDER BY counter DESC")
    public List<StakeHolder> getStakeHoldersByUsersUserNameWithoutPagination(String userName);

    /**
     * This query selects customers associated with a given username and counts the number of machines (VINs)
     */
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.entity.StakeHolder(m.customer,count(m.vin) as counter) FROM" +
            " Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true GROUP BY m.customer.id ORDER BY counter DESC")
    List<StakeHolder> getCustomersByUsersUserNameWithAllMachines(String userName, Pageable pageable);

    /**
     * Retrieves a list of StakeHolder objects representing customers and their machine counts for a specific user,
     * with an additional search filter applied to various fields
     */
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.entity.StakeHolder(m.customer,count(m.vin) as counter) FROM" +
            " Machine m join m.users u where ?1 = u.userName AND m.renewalFlag = true AND (lower(m.customer.name) LIKE " +
            "lower(concat('%', ?2,'%')) OR lower(m.model) LIKE lower(concat('%', ?2,'%')) OR lower(m.customer.phonenumber) LIKE" +
            " lower(concat('%', ?2,'%')) OR lower(m.location) LIKE lower(concat('%', ?2,'%')) OR lower(m.vin) LIKE" +
            " lower(concat('%', ?2,'%')) OR lower(m.tag) LIKE lower(concat('%', ?2,'%'))) GROUP BY m.customer.id ORDER BY counter DESC")
    List<StakeHolder> getCustomersByUsersUserNameWithAllMachinesWithSearch(String userName, String search,
                                                                           PageRequest pageRequest);

    /**
     * Counts the number of machines associated with a given username.
     *
     * @param userName The username for which to count machines.
     * @return The number of machines associated with the username.
     */
    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName ")
    Long getCountByUsersUserName(String userName);

    /**
     * Counts the number of machines with overdue renewals for a given username.
     * A renewal is considered overdue if the renewal date is before today and the machine's last status update time
     * is after the specified communicating date.
     *
     * @param userName           The username for which to count machines.
     * @param today              The current date.
     * @param communicatingDate The date after which a machine is considered communicating.
     * @return The number of machines with overdue renewals.
     */
    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate < ?2 AND NOT m.renewalDate = null AND m.statusAsOnTime > ?3")
    Long getCountByRenewalOverDue(String userName, Date today, Date communicatingDate);

    /**
     * Counts the number of machines with renewals approaching for a given username.
     * A renewal is considered approaching if the renewal date is after the specified max renewal date and the machine's
     * last status update time is after the specified communicating date.
     *
     * @param userName           The username for which to count machines.
     * @param maxRenewalDate    The date after which a renewal is considered approaching.
     * @param communicatingDate The date after which a machine is considered communicating.
     * @return The number of machines with renewals approaching.
     */
    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate > ?2 AND NOT m.renewalDate = null AND m.statusAsOnTime > ?3")
    Long getCountByRenewalApproaching(String userName, Date maxRenewalDate, Date communicatingDate);

    /**
     * Counts the number of machines with no renewal data for a given username.
     * A machine is considered to have no renewal data if its renewal date is null.
     *
     * @param userName The username for which to count machines.
     * @return The number of machines with no renewal data.
     */
    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate = null")
    Long getCountByRenewalNoData(String userName);

    /**
     * Counts the number of machines with immediate renewals for a given username.
     * A renewal is considered immediate if the renewal date is between today and the specified max renewal date and
     * the machine's last status update time is after the specified communicating date.
     *
     * @param userName           The username for which to count machines.
     * @param today              The current date.
     * @param maxRenewalDate    The date after which a renewal is no longer considered immediate.
     * @param communicatingDate The date after which a machine is considered communicating.
     * @return The number of machines with immediate renewals.
     */
    @Query("SELECT count(m.vin) FROM Machine m join m.users u where ?1 = u.userName  AND m.renewalDate between ?2 and ?3 AND NOT m.renewalDate = null AND m.statusAsOnTime > ?4")
    Long getCountByRenewalImmediate(String userName, Date today, Date maxRenewalDate, Date communicatingDate);
}