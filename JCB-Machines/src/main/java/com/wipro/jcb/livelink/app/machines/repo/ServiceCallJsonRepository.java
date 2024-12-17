package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData;
import com.wipro.jcb.livelink.app.machines.entity.ServiceCallJson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 */

/**
 * performing CRUD operations on ServiceCallJson entities
 */
@Repository
public interface ServiceCallJsonRepository extends CrudRepository<ServiceCallJson, Integer> {
    @Query("SELECT new com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData (s.label, s.field, s.type,s.required,s.field_name) FROM ServiceCallJson s")
    List<ServiceCallJsonData> getJsonData();


}
