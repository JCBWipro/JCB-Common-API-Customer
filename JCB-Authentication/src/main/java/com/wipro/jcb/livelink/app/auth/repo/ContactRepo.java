package com.wipro.jcb.livelink.app.auth.repo;


import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-07-2024
 * project: JCB-Common-API-New
 */
@Repository
public interface ContactRepo extends JpaRepository<ContactEntity, String> {

    @Query(nativeQuery = true, value = "SELECT Contact_ID, Password, Role_ID FROM wise.contact where Contact_ID=:userName")
    List<Object[]> findByContactId(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wise.contact SET Password=:password WHERE Contact_ID=:userName")
    void updatePasswordWithContactID(@Param("password") String password, @Param("userName") String userName);
}
