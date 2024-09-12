package com.wipro.jcb.livelink.app.user.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
