package com.wipro.jcb.livelink.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.dto.AccountContactRelation;
import com.wipro.jcb.livelink.app.user.entity.AccountContactMapping;

@Repository
public interface AccountContactMappingRepo extends JpaRepository<AccountContactMapping, AccountContactRelation> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM wise.account_contact where Contact_ID=:userName")
	AccountContactMapping findAccountAndContactID(@Param("userName") String userName);

}
