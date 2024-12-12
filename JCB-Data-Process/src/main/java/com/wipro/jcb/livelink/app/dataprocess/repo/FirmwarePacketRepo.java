package com.wipro.jcb.livelink.app.dataprocess.repo;

import com.wipro.jcb.livelink.app.dataprocess.entity.FirmwareData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Repository
public interface FirmwarePacketRepo extends CrudRepository<FirmwareData, Date> {

    @Query(value = "select * from firmware_data where firmware=?1 and day=?2", nativeQuery = true)
    FirmwareData findByFirmware(String firmware, Date day);

    @Query(value = "select count(*) from firmware_data where firmware=?1 and day=?2", nativeQuery = true)
    Long countByFirmwareAndDay(String firmware, Date day);

}
