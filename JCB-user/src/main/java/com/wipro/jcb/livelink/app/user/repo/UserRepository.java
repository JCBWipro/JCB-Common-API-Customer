package com.wipro.jcb.livelink.app.user.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query(nativeQuery = true, value = "select id, age, first_name, last_name from microservices_db.user where id=:id")
	Optional<User> findUserById(@Param("id") int id);

	@Query(nativeQuery = true, value = "select id, age, first_name, last_name from microservices_db.user")
	List<User> findAllUsers();

	@Query(nativeQuery = true, value = "SELECT microservices_db.user.*, microservices_db.contact_details.* FROM microservices_db.contact_details\n"
			+ "INNER JOIN microservices_db.user ON microservices_db.contact_details.user_id=microservices_db.user.id\n"
			+ "where microservices_db.user.id=:id")
	List<Object[]> findUserDetailsByUserId(@Param("id") int id);
}
