package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.Widgets;
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
public interface WidgetRepo  extends CrudRepository<Widgets, Integer> {

    @Query(value = "SELECT w.machine_type,w.value FROM widgets w",nativeQuery = true)
    List<Widgets> getWidgetDetails();

    @Query(value = "SELECT w.machine_type,w.value FROM widgets w where w.machine_type='Widgets'",nativeQuery = true)
    List<Widgets> getJsonData();

    @Query(value = "SELECT w.machine_type,w.value FROM widgets w where w.machine_type='LiveLocation'",nativeQuery = true)
    List<Widgets> getLiveLocationJsonData();

}
