package com.wipro.jcb.livelink.app.mob.auth.repo;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(nativeQuery = true, value = "SELECT USER_ID,password,userType,roleName,firstName,lastName,email,phoneNumber,image,thumbnail,country,smsLanguage,timeZone,language FROM microservices_db.LiveLinkUser where USER_ID=:userName")
    List<Object[]> findByUserName(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE microservices_db.LiveLinkUser SET password=:password WHERE USER_ID=:userName")
    void updatePasswordWithContactID(@Param("password") String password, @Param("userName") String userName);

    @Query(nativeQuery = true, value = "SELECT phoneNumber FROM microservices_db.LiveLinkUser WHERE USER_ID=:userName")
    String findMobileNumberByUserID(@Param("userName") String userName);

    // Retrieves the User ID associated with a given phone number.
    // This query is executed as a native SQL query against the "microservices_db.LiveLinkUser" table.
    @Query(nativeQuery = true, value = "SELECT USER_ID FROM microservices_db.LiveLinkUser WHERE phoneNumber=:mobileNumber")
    String findByMobileNumber(@Param("mobileNumber") String mobileNumber);

    // Retrieves the contact ID associated with a given Primary_Email_ID .
    // This query is executed as a native SQL query against the "wise.contact" table.
    @Query(nativeQuery = true, value = "SELECT  Contact_ID FROM wise.contact WHERE Primary_Email_ID=:emailId")
    String findByEmailId(@Param("emailId") String emailId);

    @Query(nativeQuery = true, value = "SELECT firstName FROM microservices_db.LiveLinkUser WHERE email=:emailId")
    String findFirstNameFromID(@Param("emailId") String emailId);
}