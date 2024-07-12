package com.wipro.jcb.livelink.app.auth.repo;

import com.wipro.jcb.livelink.app.auth.entity.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:10-07-2024
 * project: JCB-New-Common-API
 */
@Repository
public interface UserRepository extends JpaRepository<UserRegistration, Integer> {
    Optional<UserRegistration> findByFirstName(String username);
}
