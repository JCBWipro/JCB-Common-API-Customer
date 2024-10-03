package com.wipro.jcb.livelink.app.mob.auth.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.mob.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.mob.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.mob.auth.repo.RefreshTokenMobRepository;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import com.wipro.jcb.livelink.app.mob.auth.response.UserResponse;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenMobRepository refreshTokenMobRepository;
    
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        List<UserResponse> repoResult = userRepository.findByUserName(username);
        refreshToken.setUser(AuthCommonutils.convertObjectToDTO(repoResult));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(600000)); // 10
        return refreshTokenMobRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenMobRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
        	refreshTokenMobRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return refreshToken;
    }

}
