package com.wipro.jcb.livelink.app.user.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.web.entity.TenancyEntity;
import com.wipro.jcb.livelink.app.user.web.reponse.AccountTenancyReponse;

/**
 * Author: Vikas Singh
 * User: VI20475016
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface TenancyRepo extends JpaRepository<TenancyEntity, Integer> {
	
	@Query(nativeQuery = true, value = "select acc.Account_ID as AccountId, acc.Account_Name as AccountName, acc.Account_Code as AccountCode, acc.mapping_code as MappingCode,at.Tenancy_ID as TenancyId,ten.Tenancy_Name as TenancyName from wise.account acc, account_tenancy at,tenancy ten where acc.mapping_code in (select mapping_code from wise.account where MAFlag=1 ) and acc.account_id = at.account_id and at.Tenancy_ID = ten.Tenancy_ID")
    List<AccountTenancyReponse> getPseudoTenancyList();
	
	@Query(nativeQuery = true, value = "SELECT Tenancy_ID FROM wise.account_tenancy where Account_ID in (SELECT account_id FROM wise.account_contact where Contact_ID=:userName)")
	public List<Integer> getTenancyIdByUserName(@Param("userName") String userName);
	
	@Query(nativeQuery = true, value = "select Serial_Number from asset where Machine_Number=:machineNumber")
	public String getMachineNumber(@Param("machineNumber") String machineNumber);
	
	@Query(nativeQuery = true, value = "select aos.serial_number from asset_owner_snapshot aos where aos.account_id in(select account_id from account where mapping_code in(select mapping_code from account where account_id in(select account_id from account_tenancy where tenancy_id in(:tenancyId)))) and aos.serial_number=:serialNumber")
	public String getSerialNumberByTenancyId(@Param("tenancyId") Integer tenancyId, @Param("serialNumber") String serialNumber);
	
	@Query(nativeQuery = true, value = "select a.tenancy_id from account_tenancy a, account b where b.status=true and a.account_id=b.account_id and b.mapping_code in (select c.mapping_code from account c where c.status=true and c.account_id in (select account_id from account_tenancy where tenancy_id in :tenancyId))")
	public List<Integer> getLinkedTenancyListForTheTenancy(@Param("tenancyId") List<Integer> tenancyId);
	
	@Query(nativeQuery = true, value = "SELECT Tenancy_ID FROM wise.tenancy where Tenancy_ID in :tenancyId")
	public List<Integer> getTenancyDetailsByTenancyId(@Param("tenancyId") List<Integer> tenancyId);
	
}
