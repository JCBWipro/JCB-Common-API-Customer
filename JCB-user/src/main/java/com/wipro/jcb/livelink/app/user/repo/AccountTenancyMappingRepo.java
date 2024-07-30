package com.wipro.jcb.livelink.app.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.dto.AccountTenancyRelation;
import com.wipro.jcb.livelink.app.user.entity.AccountTenancyMapping;

/**
 * Author: Vikas Singh
 * User: VI20475016
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface AccountTenancyMappingRepo extends JpaRepository<AccountTenancyMapping, AccountTenancyRelation>{
	
	@Query(nativeQuery = true, value = "select a.Account_ID,a.Tenancy_ID from account_tenancy a, account b where b.status=true and a.Account_ID=b.Account_ID and b.mapping_code ="
			+ "(select mapping_code from account where status=true and account_id=:accountId)")
    List<AccountTenancyMapping> findAccountAndTenancyID(@Param("accountId") int accountId);

}
