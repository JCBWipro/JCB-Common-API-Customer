package com.wipro.jcb.livelink.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{

}
