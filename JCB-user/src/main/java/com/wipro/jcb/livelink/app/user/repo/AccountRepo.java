package com.wipro.jcb.livelink.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.AccountEntity;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-07-2024
 * project: JCB-Common-API-New
 */
@Repository
public interface AccountRepo extends JpaRepository<AccountEntity,Integer> {
	
	@Query(nativeQuery = true, value = "SELECT country_name FROM wise.country_codes where country_code=:countryCode")
    String findCountryNameByCode(@Param("countryCode") String countryCode);
	
	@Query(nativeQuery = true, value = "SELECT LL_Code FROM wise.account_mapping where ECC_Code=:accountCode or CRM_Code=:accountCode")
    String findLLCodeByAccountCode(@Param("accountCode") String accountCode);
}
