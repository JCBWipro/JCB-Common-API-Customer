package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AppServerToken;
import com.wipro.jcb.livelink.app.alerts.entity.RefreshToken;
import com.wipro.jcb.livelink.app.alerts.repo.AppServerTokenRepository;
import com.wipro.jcb.livelink.app.alerts.repo.RefreshTokenMobRepository;
import com.wipro.jcb.livelink.app.alerts.service.AppServerTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppServerTokenServiceImpl implements AppServerTokenService {

    @Autowired
    RefreshTokenMobRepository refreshTokenMobRepository;

    @Autowired
    AppServerTokenRepository appServerTokenRepository;

    @Override
    public String getTokenByUsername(String userName) {
        String refreshToken = refreshTokenMobRepository.findTokenByUserName(userName);
        return refreshToken;
    }

    @Override
    public String setUsernameByToken(AppServerToken appServerToken) {
        return "";
    }

    //@CachePut(value = "AppServerToken", key = "#appServerToken.liveLinkToken")
    public String setUsernameByToken(RefreshToken refreshToken) {
        log.debug("setUsernameByToken: Setting username: {} for token: {}",
                refreshToken.getUser(), refreshToken.getToken());
        appServerTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    @Override
    //@CacheEvict(value = "AppServerToken", key = "#token")
    public void removeUsernameByToken(String token) {
    }
}