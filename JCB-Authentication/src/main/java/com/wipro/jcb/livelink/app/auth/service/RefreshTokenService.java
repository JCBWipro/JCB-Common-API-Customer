package com.wipro.jcb.livelink.app.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.repo.RefreshTokenRepository;
import com.wipro.jcb.livelink.app.auth.reponse.ContactResponse;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ContactRepo contactRepo;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        ContactResponse contactRepoResult = contactRepo.findByContactId(username);
        refreshToken.setContactEntity(AuthCommonutils.convertObjectToDTO(contactRepoResult));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(600000)); // 10
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
    	RefreshToken repoResult = refreshTokenRepository.findByToken(token);
        return Optional.of(repoResult);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        System.out.println("Token:- " + token + "  " + Instant.now());
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

}
