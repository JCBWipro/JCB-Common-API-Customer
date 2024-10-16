package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.dto.ServiceCallJsonData;
import com.wipro.jcb.livelink.app.machines.entity.ServiceCallJson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface ServiceCallJsonRepository extends CrudRepository<ServiceCallJson, Integer> {
    @Query(value = "SELECT label, field, type,required,field_name,value FROM service_call_json", nativeQuery = true)
    List<ServiceCallJsonData> getJsonData();


}
