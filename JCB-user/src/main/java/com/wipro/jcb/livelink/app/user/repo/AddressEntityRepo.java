package com.wipro.jcb.livelink.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.AddressEntity;

@Repository
public interface AddressEntityRepo extends JpaRepository<AddressEntity, Integer> {

}
