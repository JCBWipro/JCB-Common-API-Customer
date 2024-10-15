package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/30/2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    /**
     * Find User by username
     *
     * @param userName is the unique identifier
     * @return User is the instance of user
     */

    @Query(value = "select * from live_link_user where USER_ID=:userName", nativeQuery = true)
    User findByUserName(String userName);

    @Modifying
    @Query(value = "update live_link_user set user_app_version =?1 where user_id =?2", nativeQuery = true)
    void versionUpdate(String version, String userName);

}