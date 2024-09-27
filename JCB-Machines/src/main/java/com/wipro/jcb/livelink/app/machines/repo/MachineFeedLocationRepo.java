
package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineFeedLocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;

public interface MachineFeedLocationRepo extends CrudRepository<MachineFeedLocation, String> {
    @Async
    public <S extends MachineFeedLocation> Iterable<S> save(Iterable<S> entities);

    @Override
    public <S extends MachineFeedLocation> S save(S entity);


    @Query("select count(m.vin) from MachineFeedLocation m  join m.machineAddress ma where m.statusAsOnTime > ma.statusAsOnTime and m.statusAsOnTime > ?1 and m.lastModifiedDate between ?2 and ?3 and m.lastModifiedDate is not null")
    public long getLastedMachines(Date todayDate, Date startDateTime, Date endDateTime);

    @Query("select m from MachineFeedLocation m  where m.statusAsOnTime > ?1 and m.lastModifiedDate between ?2 and ?3 and m.lastModifiedDate is not null")
    public List<MachineFeedLocation> getLastedMachines(Date todayDate, Date startDateTime, Date endDateTime, Pageable pageable);

    @Query(value = "select * from machine_feedparser_location_data m where m.vin =:vin and m.last_modified_date is not null",nativeQuery = true)
    MachineFeedLocation findByVin(String vin);

    @Query("select count(m.vin) from MachineFeedLocation m where m.statusAsOnTime > ?1 and m.lastModifiedDate between ?2 and ?3 and m.lastModifiedDate is not null")
    long getAllLastedMachines(Date todayDate, Date startDateTime, Date endDateTime);

}
