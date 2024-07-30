package com.wipro.jcb.livelink.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.AddressEntity;

/**
 * Author: Vikas Singh
 * User: VI20475016
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Repository
public interface AddressEntityRepo extends JpaRepository<AddressEntity, Integer> {

}
