package com.wipro.jcb.livelink.app.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.TenancyEntity;

@Repository
public interface TenancyRepo extends JpaRepository<TenancyEntity, Integer> {
	
	@Query(nativeQuery = true, value = "select acc.Account_ID, acc.Account_Name, acc.Account_Code, acc.mapping_code,at.Tenancy_ID,ten.Tenancy_Name from wise.account acc, account_tenancy at,tenancy ten where acc.mapping_code in (select mapping_code from wise.account where MAFlag=1 ) and acc.account_id = at.account_id and at.Tenancy_ID = ten.Tenancy_ID")
    List<Object[]> getPseudoTenancyList();
}
