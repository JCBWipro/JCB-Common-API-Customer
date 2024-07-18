package com.wipro.jcb.livelink.app.mob.auth.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	 @Query(nativeQuery = true, value = "SELECT USER_ID, password, userType, roleName FROM microservices_db.LiveLinkUser where USER_ID=:userName")
	 List<Object[]> findByUserName(@Param("userName") String userName);
}