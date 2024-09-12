package com.wipro.jcb.livelink.app.user.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.web.dto.AccountContactRelation;
import com.wipro.jcb.livelink.app.user.web.entity.AccountContactMapping;

/**
 * Author: Vikas Singh
 * User: VI20475016
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface AccountContactMappingRepo extends JpaRepository<AccountContactMapping, AccountContactRelation> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM wise.account_contact where Contact_ID=:userName")
	AccountContactMapping findAccountAndContactID(@Param("userName") String userName);

}
