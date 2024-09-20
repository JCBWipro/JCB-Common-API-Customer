package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.commonUtils.AppServerToken;
import com.wipro.jcb.livelink.app.machines.service.AppServerTokenService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class AppServerTokenServiceImpl implements AppServerTokenService {
    @Override
    @Cacheable(value = "AppServerToken", key = "#token")
    public String getUsernameByToken(String token) {
        return null;
    }

    @Override
    @CachePut(value = "AppServerToken", key = "#appServerToken.liveLinkToken")
    public String setUsernameByToken(AppServerToken appServerToken) {
        return appServerToken.getUserName();
    }

    @Override
    @CacheEvict(value = "AppServerToken", key = "#token")
    public void removeUsernameByToken(String token) {
    }
}
