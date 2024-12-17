package com.wipro.jcb.livelink.app.machines.cache.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.wipro.jcb.livelink.app.machines.cache.LivelinkUserTokenService;
import com.wipro.jcb.livelink.app.machines.commonUtils.UserToken;

@Service
@PropertySource("application.properties")
public class LivelinkUserTokenServiceImpl implements LivelinkUserTokenService {
	
	@Override
	@Cacheable(value = "LivelinkUserToken", key = "#username")
	public UserToken getUserTokenByUsername(String username) {
		return null;
	}

	@Override
	@CachePut(value = "LivelinkUserToken", key = "#userToken.userName")
	public UserToken setUserTokenByUsername(UserToken userToken) {
		return userToken;
	}

	@Override
	@CacheEvict(value = "LivelinkUserToken", key = "#username")
	public void removeUserTokenByUsername(String username) {
	}

}