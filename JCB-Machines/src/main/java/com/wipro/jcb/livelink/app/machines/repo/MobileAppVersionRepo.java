package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MobileAppVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface MobileAppVersionRepo extends CrudRepository<MobileAppVersion, String> {

    MobileAppVersion findByOs(String os);

    //@Query("SELECT os, blocked_version FROM mobile_app_version")
    @Query(value = "SELECT os, blocked_version FROM mobile_app_version",nativeQuery = true)
    List<MobileAppVersion> getBlockedVersionDetails();

    @Query(value = "SELECT os, current_version FROM mobile_app_version", nativeQuery = true)
    List<MobileAppVersion> getCurrentVersionDetails();

}
