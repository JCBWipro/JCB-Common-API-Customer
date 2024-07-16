package com.wipro.jcb.livelink.app.auth.repo;

import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
	
	@Query(nativeQuery = true, value = "SELECT expiry_date, token, contact_id FROM wise.refresh_token where token=:token")
    List<Object[]> findByToken(String token);
    
}
