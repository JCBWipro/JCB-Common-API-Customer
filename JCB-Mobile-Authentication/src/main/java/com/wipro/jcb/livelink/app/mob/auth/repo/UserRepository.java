package com.wipro.jcb.livelink.app.mob.auth.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String>{
	
//	 @Query(nativeQuery = true, value = "SELECT USER_ID, password, userType, roleName FROM microservices_db.LiveLinkUser where USER_ID=:userName")
	@Query(nativeQuery = true, value = "SELECT USER_ID,password,userType,roleName,firstName,lastName,email,phoneNumber,image,thumbnail,country,smsLanguage,timeZone,language FROM microservices_db.LiveLinkUser where USER_ID=:userName")
	 List<Object[]> findByUserName(@Param("userName") String userName);
	 
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE microservices_db.LiveLinkUser SET password=:password WHERE USER_ID=:userName")
	void updatePasswordWithContactID(@Param("password") String password, @Param("userName") String userName);
	 
	 @Query(nativeQuery = true, value = "select USER_ID from microservices_db.LiveLinkUser where USER_ID=:userName")
	 String checkIfUserNameExists(@Param("userName") String userName);
}