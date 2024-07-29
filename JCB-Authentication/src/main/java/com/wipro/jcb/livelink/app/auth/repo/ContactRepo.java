package com.wipro.jcb.livelink.app.auth.repo;


import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-07-2024
 * project: JCB-Common-API-New
 */
@Repository
public interface ContactRepo extends JpaRepository<ContactEntity, String> {

    @Query(nativeQuery = true, value = "SELECT Contact_ID, Password, Role_ID FROM wise.contact where Contact_ID=:userName")
    List<Object[]> findByContactId(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET Password=:password WHERE Contact_ID=:userName")
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

    @Query(nativeQuery = true, value = "SELECT  First_Name FROM wise.contact WHERE Primary_Email_ID=:emailId")
    String findFirstNameFromID(@Param("emailId") String emailId);
}
