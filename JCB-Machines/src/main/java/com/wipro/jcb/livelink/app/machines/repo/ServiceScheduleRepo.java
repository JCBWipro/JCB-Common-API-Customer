package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.ServiceSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-09-2024
 */
@Repository
public interface ServiceScheduleRepo  extends CrudRepository<ServiceSchedule, Integer> {

    @Async
    public <S extends ServiceSchedule> Iterable<S> save(Iterable<S> entities);

    public <S extends ServiceSchedule> S save(S entity);

    @Query("SELECT ms from ServiceSchedule ms where ?1 = ms.vin")
    ServiceSchedule findById(String id);

}
