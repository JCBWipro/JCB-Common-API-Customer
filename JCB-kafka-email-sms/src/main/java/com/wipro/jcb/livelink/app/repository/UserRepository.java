package com.wipro.jcb.livelink.app.repository;

import com.wipro.jcb.livelink.app.entity.Contact;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:21-06-2024
 * project: JCB_NewRepo
 */
@Repository
public interface UserRepository extends JpaRepository<Contact, Long> {

    @Query(nativeQuery = true, value = "SELECT mobile_number FROM microservices_db.user_registration where mobile_number=:mobileNumber")
    String existsByMobileNumber(String mobileNumber);

    Contact findByEmailId(String emailId);
    //List<UserRegistrationRequest> findByMobileNumberAndVerified(String mobileNumber, boolean verified);

    @Query(nativeQuery = true, value = "SELECT mobile_number FROM microservices_db.user_registration where email_id=?1")
    String findMobileNumber(@Param("emailId") String emailId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE microservices_db.user_registration SET password=:password WHERE mobile_number=:mobileNumber")
    void updatePasswordWithMobile(@Param("password") String password, @Param("mobileNumber") String mobileNumber);

}
