package com.wipro.jcb.livelink.app.machines.cache;

import org.springframework.stereotype.Component;

import com.wipro.jcb.livelink.app.machines.commonUtils.UserToken;

@Component
public interface LivelinkUserTokenService {

	public UserToken getUserTokenByUsername(String username);

	public UserToken setUserTokenByUsername(UserToken userToken);

	public void removeUserTokenByUsername(String username);

}
