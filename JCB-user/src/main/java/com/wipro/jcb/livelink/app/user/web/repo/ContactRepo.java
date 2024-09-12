package com.wipro.jcb.livelink.app.user.web.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.web.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.web.reponse.UserDetailsReponse;

import java.util.List;

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

    //check which user accounts are locked or not
    @Query(nativeQuery = true, value = "SELECT * FROM wise.contact WHERE (lockedOutTime IS NOT NULL AND errorLogCounter >= 5) OR (lockedOutTime IS NOT NULL AND reset_pass_count >= 5)")
    List<ContactEntity> findLockedUsers();

    //check which user accounts errorLogCounter and reset_pass_count is more than Zero (0)
    @Query(nativeQuery = true, value = "SELECT * FROM wise.contact WHERE (errorLogCounter > 0 OR reset_pass_count > 0)")
    List<ContactEntity> findErrLogResetCount();

    //reset the errorLogCounter and reset_pass_count to Zero
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET errorLogCounter = 0, reset_pass_count = 0 WHERE (errorLogCounter > 0 OR reset_pass_count > 0)")
    void resetToZero(@Param("userName") String userName);

    //check Specific user account is locked or not
    @Query(nativeQuery = true, value = "SELECT * FROM wise.contact WHERE Contact_ID= " +
            ":contactID AND lockedOutTime IS NOT NULL AND (errorLogCounter >= 5 OR reset_pass_count >= 5)")
    String findLockedUserByID(@Param("contactID") String contactID);

    //This query is executed as a native SQL query against the "wise.contact" table for getting the username
    @Query(nativeQuery = true, value = "SELECT * FROM wise.contact WHERE Contact_ID = :userName")
    ContactEntity findByUserContactId(@Param("userName") String userName);

    //reset the locked account to Zero/Null
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET lockedOutTime = NULL , errorLogCounter = 0, reset_pass_count = 0 WHERE Contact_ID=:userName")
    void unlockUserAccountByID(@Param("userName") String userName);

    //reset the all locked account to Zero/Null
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET lockedOutTime = NULL , errorLogCounter = 0, reset_pass_count = 0")
    void unlockAllUserAccount(@Param("userName") String userName);
    
}
