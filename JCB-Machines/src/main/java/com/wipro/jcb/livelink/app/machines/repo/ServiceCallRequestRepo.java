package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.ServiceCallRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/17/2024
 */
@Repository
public interface ServiceCallRequestRepo extends CrudRepository<ServiceCallRequest, Integer> {
}
