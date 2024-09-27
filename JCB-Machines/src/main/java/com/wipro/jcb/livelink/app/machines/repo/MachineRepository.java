package com.wipro.jcb.livelink.app.machines.repo;

import java.util.Date;
import java.util.List;

import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.service.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends CrudRepository<Machine, String> {
    /**
     * Find Machine by vin
     *
     * @param vin is unique identity of machine for which data to be retrieved
     * @return Machine is an instance of Machine
     */
    public Machine findByVin(String vin);

    @Query(value = "SELECT m.*, mu.user_id  FROM machine m JOIN machin_user mu ON m.vin = mu.vin WHERE mu.user_id = ?2 AND m.vin = ?1", nativeQuery = true)
    Machine findByVinAndUserName(String vin, String userName);

    @Query("SELECT DISTINCT m.model FROM Machine m join m.users u where ?1 = u.userName")
    public List<String> findDistinctModelForUsers(String userName);

    @Query("SELECT DISTINCT m.platform FROM Machine m join m.users u where ?1 = u.userName  and m.platform!='NA'")
    public List<String> findDistinctPlatformForUsers(String userName);

    @Query(value = "SELECT m.vin as vin, m.model as model, m.platform as platform, mfd.total_machine_hours as totalmachinehours,c.name as customername,c.phonenumber as customernumber,d.name as dealername,d.phonenumber as dealernumber  FROM machine m join customer c on c.id=m.customer_username join dealer d on d.id= m.dealer_username left outer join machine_feedparser_data mfd on mfd.vin = m.vin where m.vin =:vin AND m.renewal_flag =true ", nativeQuery = true)
    public List<Object[]> fetchMachineDetails(@Param("vin") String vin);

    @Query(value = "SELECT count(m.vin) FROM machine m join machin_user u where user_id=:userName",nativeQuery = true)
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

    @Query(value = "SELECT USER_ID ,password, roleName FROM microservices_db.LiveLinkUser where USER_ID=:userName",nativeQuery = true)
    UserResponse findByContactId(@Param("userName") String userName);


}