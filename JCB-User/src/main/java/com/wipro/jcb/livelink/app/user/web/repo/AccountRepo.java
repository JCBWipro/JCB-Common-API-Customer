package com.wipro.jcb.livelink.app.user.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.web.entity.AccountEntity;

/**
 * Author: Vikas Singh
 * User: VI20475016
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface AccountRepo extends JpaRepository<AccountEntity,Integer> {
	
	@Query(nativeQuery = true, value = "SELECT country_name FROM wise.country_codes where country_code=:countryCode")
    String findCountryNameByCode(@Param("countryCode") String countryCode);
	
}
