package com.wipro.jcb.livelink.app.reports.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.reports.entity.Customer;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, String>{

}
