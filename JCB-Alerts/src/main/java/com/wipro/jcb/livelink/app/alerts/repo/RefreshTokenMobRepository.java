package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.entity.RefreshToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:24-10-2024
 */
@Component
public interface RefreshTokenMobRepository extends CrudRepository<RefreshToken, Integer> {

    @Query("SELECT token from RefreshToken where expiryDate = (SELECT MAX(expiryDate) as m from RefreshToken r WHERE r.user.userName = :userName)")
    String findTokenByUserName(String userName);
}
