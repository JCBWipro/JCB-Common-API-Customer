
package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineFeedLocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;

public interface MachineFeedLocationRepo extends CrudRepository<MachineFeedLocation, String> {
    //@Override
    @Async
    public <S extends MachineFeedLocation> Iterable<S> save(Iterable<S> entities);

    @Override
    public <S extends MachineFeedLocation> S save(S entity);


    @Query("select count(m.vin) from MachineFeedLocation m  join m.machineAddress ma where m.statusAsOnTime > ma.statusAsOnTime and m.statusAsOnTime > ?1 and m.lastModifiedDate between ?2 and ?3 and m.lastModifiedDate is not null")
    public long getLastedMachines(Date todayDate, Date startDateTime, Date endDateTime);

    @Query("select m from MachineFeedLocation m  where m.statusAsOnTime > ?1 and m.lastModifiedDate between ?2 and ?3 and m.lastModifiedDate is not null")
    public List<MachineFeedLocation> getLastedMachines(Date todayDate, Date startDateTime, Date endDateTime, Pageable pageable);

    @Query("select m from MachineFeedLocation m where m.vin = ?1 and m.lastModifiedDate is not null")
    public MachineFeedLocation findByVin(String vin);

    @Query("select count(m.vin) from MachineFeedLocation m where m.statusAsOnTime > ?1 and m.lastModifiedDate between ?2 and ?3 and m.lastModifiedDate is not null")
    public long getAllLastedMachines(Date todayDate, Date startDateTime, Date endDateTime);

   /* @Query(value = "select round(6371 * acos(cos(radians(machine.center_lat)) * cos(radians(mfl.latitude)) * cos(radians(mfl.longitude) - radians(machine.center_long)) "
            + "+ sin(radians(machine.center_lat)) * sin(radians(mfl.latitude)))) as distance, machine.radius, machin_user.user_id, machine.vin FROM machine "
            + "join machine_feedparser_location_data mfl on mfl.vin=machine.vin join machin_user ON machine.vin = machin_user.vin join live_link_user u on "
            + "u.user_id=machin_user.user_id where u.user_type = ?4 and machine.radius > 0.0 and machine.vin in ?1 and mfl.last_modified_date between ?2 and ?3 "
            + "and machine.center_lat > 0.0 and machine.center_long > 0.0 and (round(6371 * acos(cos(radians(machine.center_lat)) * cos(radians(mfl.latitude)) "
            + "* cos(radians(mfl.longitude) - radians(machine.center_long)) + sin(radians(machine.center_lat)) * sin(radians(mfl.latitude))))) > machine.radius"
            , nativeQuery = true)
    public List<GeoFenceMachines> findGeoFenceMachines(List<String> vins, Date startTime, Date endTime, int userType);*/

    //MachineFeedLocation findOne(String vin);
}
