package com.wipro.jcb.livelink.app.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM wise.refresh_token where token=:token")
	RefreshToken findByToken(String token);
    
}
