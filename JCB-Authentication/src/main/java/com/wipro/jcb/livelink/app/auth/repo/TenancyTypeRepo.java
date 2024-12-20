package com.wipro.jcb.livelink.app.auth.repo;

import com.wipro.jcb.livelink.app.auth.entity.TenancyTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-07-2024
 * project: JCB-Common-API-New
 */
@Repository
public interface TenancyTypeRepo extends JpaRepository<TenancyTypeEntity, Integer> {
}
