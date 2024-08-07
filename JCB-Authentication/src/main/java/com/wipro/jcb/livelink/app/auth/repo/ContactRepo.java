package com.wipro.jcb.livelink.app.auth.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.reponse.ContactResponse;

import jakarta.transaction.Transactional;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-07-2024
 * project: JCB-Common-API-New
 */
@Repository
public interface ContactRepo extends JpaRepository<ContactEntity, String> {

    @Query(nativeQuery = true, value = "SELECT Contact_ID as ContactId, Password, Role_ID as RoleId FROM wise.contact where Contact_ID=:userName")
    ContactResponse findByContactId(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET Password=:password, sys_gen_password=1 WHERE Contact_ID=:userName")
    void updatePasswordWithContactID(@Param("password") String password, @Param("userName") String userName);

    // Retrieves the primary mobile number associated with a given user ID.
    // This query is executed as a native SQL query against the "wise.contact" table.
    @Query(nativeQuery = true, value = "SELECT Primary_Moblie_Number FROM wise.contact WHERE Contact_ID=:userName")
    String findMobileNumberByUserID(@Param("userName") String userName);

    // Retrieves the contact ID associated with a given primary mobile number.
    // This query is executed as a native SQL query against the "wise.contact" table.
    @Query(nativeQuery = true, value = "SELECT Contact_ID FROM wise.contact WHERE Primary_Moblie_Number=:mobileNumber")
    String findByMobileNumber(@Param("mobileNumber") String mobileNumber);

    // Retrieves the contact ID associated with a given Primary_Email_ID .
    // This query is executed as a native SQL query against the "wise.contact" table.
    @Query(nativeQuery = true, value = "SELECT  Contact_ID FROM wise.contact WHERE Primary_Email_ID=:emailId")
    String findByEmailId(@Param("emailId") String emailId);

    // Retrieves the First_Name associated with a given Primary_Email_ID .
    // This query is executed as a native SQL query against the "wise.contact" table.
    @Query(nativeQuery = true, value = "SELECT  First_Name FROM wise.contact WHERE Primary_Email_ID=:emailId")
    String findFirstNameFromID(@Param("emailId") String emailId);

   /* For Reset Password attempts
      Retrieves the count associated with a given user ID .
      This query is executed as a native SQL query against the "wise.contact" table.*/
    @Query(nativeQuery = true, value = "SELECT CAST(reset_pass_count AS UNSIGNED) FROM wise.contact WHERE Contact_ID=:userName")
    int resetPasswordGetAttempts(@Param("userName") String userName);

    //Increment the password reset attempt count for a user
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET reset_pass_count = reset_pass_count + 1 WHERE Contact_ID =:userName")
    int resetPasswordIncrementAttempts(@Param("userName") String userName);

    /* For Login attempts
       Retrieves the count associated with a given user ID .
       This query is executed as a native SQL query against the "wise.contact" table.*/
    @Query(nativeQuery = true, value = "SELECT CAST(errorLogCounter AS UNSIGNED) FROM wise.contact WHERE Contact_ID=:userName")
    int userLoginGetAttempts(@Param("userName") String userName);

    //Increment the password reset attempt count for a user
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET errorLogCounter = errorLogCounter + 1 WHERE Contact_ID =:userName")
    void userLoginIncrementAttempts(@Param("userName") String userName);

    //Reset the attempts count to Zero
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET errorLogCounter = 0 WHERE Contact_ID = :userName")
    void userLoginResetAttempts(@Param("userName") String userName);

    //check the sys_gen_password column value w.r.t contact_ID
    @Query(nativeQuery = true, value = "SELECT sys_gen_password FROM wise.contact WHERE Contact_ID = :userName")
    int checkSysGenPassByContactID(@Param("userName") String userName);


    @Query(nativeQuery = true, value = "SELECT * FROM wise.contact WHERE Contact_ID = :username")
    ContactEntity findByUserContactId(@Param("username") String username);

}
