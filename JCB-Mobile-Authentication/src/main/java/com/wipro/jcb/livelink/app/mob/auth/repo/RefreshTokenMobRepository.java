package com.wipro.jcb.livelink.app.mob.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wipro.jcb.livelink.app.mob.auth.entity.RefreshToken;


public interface RefreshTokenMobRepository extends JpaRepository<RefreshToken, Integer> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM microservices_db.refresh_token_mob where token=:token")
    Optional<RefreshToken> findByToken(String token);

}
