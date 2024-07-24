package com.wipro.jcb.livelink.app.auth.repo;

import com.wipro.jcb.livelink.app.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:19-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(nativeQuery = true, value = "SELECT USER_ID,password,userType,roleName,firstName,lastName,email,phoneNumber,image,thumbnail,country,smsLanguage,timeZone,language FROM microservices_db.LiveLinkUser where USER_ID=:userName")
    List<Object[]> findByUserName(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.LiveLinkUser SET password=:password WHERE USER_ID=:userName")
    void updatePasswordWithContactID(@Param("password") String password, @Param("userName") String userName);

   /* @Query(nativeQuery = true, value = "select USER_ID from wise.contact where USER_ID=:userName")
    String checkIfUserNameExists(@Param("userName") String userName);*/
}