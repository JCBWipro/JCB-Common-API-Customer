package com.wipro.jcb.livelink.app.alerts.repo;

import com.wipro.jcb.livelink.app.alerts.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:24-10-2024
 */
@Component
public interface AppServerTokenRepository extends CrudRepository<RefreshToken, Integer> {
}