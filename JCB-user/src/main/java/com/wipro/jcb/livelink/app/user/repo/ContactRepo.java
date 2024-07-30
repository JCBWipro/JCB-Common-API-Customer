package com.wipro.jcb.livelink.app.user.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.ContactEntity;

@Repository
public interface ContactRepo extends JpaRepository<ContactEntity, String> {

    @Query(nativeQuery = true, value = "SELECT Contact_ID,First_Name,Last_Name,Is_Tenancy_Admin,sys_gen_password,Role_ID FROM wise.contact where Contact_ID=:userName")
    List<Object[]> findContactByContactId(@Param("userName") String userName);

    @Query(nativeQuery = true, value = "SELECT Account_ID FROM wise.account_contact where Contact_ID=:userName")
    int getAccountObjByUsername(@Param("userName") String userName);
    
}
