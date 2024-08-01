package com.wipro.jcb.livelink.app.user.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.reponse.UserDetailsReponse;

/**
 * Author: Vikas Singh
 * User: VI20475016
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface ContactRepo extends JpaRepository<ContactEntity, String> {

	@Query(nativeQuery = true, value = "SELECT Contact_ID as ContactId,First_Name as Firstname,Last_Name as Lastname,Is_Tenancy_Admin as IsTenancyAdmin,sys_gen_password as SysGeneratedPassword,Role_ID as RoleId FROM wise.contact where Contact_ID=:userName")
	UserDetailsReponse findContactByContactId(@Param("userName") String userName);

    @Query(nativeQuery = true, value = "SELECT Account_ID as AccountId FROM wise.account_contact where Contact_ID=:userName")
    int getAccountDetailsByUsername(@Param("userName") String userName);
    
}
