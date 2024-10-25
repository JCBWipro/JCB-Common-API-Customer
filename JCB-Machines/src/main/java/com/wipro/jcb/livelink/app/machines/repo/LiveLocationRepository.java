package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineLiveLocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-10-2024
 */
@Repository
public interface LiveLocationRepository extends CrudRepository<MachineLiveLocation, Integer> {


    @Query("SELECT m FROM MachineLiveLocation m where m.slot=?1 AND m.vin=?2")
    MachineLiveLocation getLocationBySlotAndVin(String slot, String vin);

    @Query("SELECT m FROM MachineLiveLocation m where m.uniqueId=?1")
    MachineLiveLocation getLocationDetailsByLocationId(String locationId);

}