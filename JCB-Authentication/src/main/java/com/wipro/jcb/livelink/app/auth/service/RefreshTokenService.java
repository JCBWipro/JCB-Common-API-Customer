package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.commonutils.AuthCommonutils;
import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.RefreshToken;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import com.wipro.jcb.livelink.app.auth.repo.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ContactRepo contactRepo;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        List<Object[]> repoResult = contactRepo.findByContactId(username);
        refreshToken.setContactEntity(AuthCommonutils.convertObjectToDTO(repoResult));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(600000)); // 10
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        List<Object[]> repoResult = refreshTokenRepository.findByToken(token);
        RefreshToken refreshToken = new RefreshToken();
        
        for(Object[] object : repoResult) {
        	Timestamp sqlTimeStamp = Timestamp.valueOf(object[0].toString());
        	Instant instant = sqlTimeStamp.toInstant();
        	
        	refreshToken.setExpiryDate(instant);
        	refreshToken.setToken(object[1].toString());
        	String contactId = object[2].toString();
        	List<Object[]> contactRepoResult = contactRepo.findByContactId(contactId);
        	ContactEntity contactEntity = AuthCommonutils.convertObjectToDTO(contactRepoResult);
        	refreshToken.setContactEntity(contactEntity);
        }
        return Optional.of(refreshToken);
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
