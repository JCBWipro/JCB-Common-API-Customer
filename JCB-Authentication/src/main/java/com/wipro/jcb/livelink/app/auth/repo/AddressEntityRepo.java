package com.wipro.jcb.livelink.app.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.auth.entity.AddressEntity;

@Repository
public interface AddressEntityRepo extends JpaRepository<AddressEntity, Integer>{

}
