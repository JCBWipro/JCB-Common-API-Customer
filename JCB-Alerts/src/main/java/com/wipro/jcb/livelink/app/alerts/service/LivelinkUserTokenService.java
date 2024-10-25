package com.wipro.jcb.livelink.app.alerts.service;

import com.wipro.jcb.livelink.app.alerts.commonUtils.UserToken;
import org.springframework.stereotype.Component;

/*
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Component
public interface LivelinkUserTokenService {
    /**
     *
     * @param username
     *            is unique identity of user
     * @return instance of class UserToken
     */
    public UserToken getUserTokenByUsername(String username);

    /**
     *
     * @param userToken
     *            is instance of class UserToken set with key as UserName and values
     *            is UesrToken instance
     * @return instance of userToken
     */
    public UserToken setUserTokenByUsername(UserToken userToken);

    /**
     * This is to remove usersession from cache
     *
     * @param username
     *            is to decide which user has to remove from cache
     */
    public void removeUserTokenByUsername(String username);

}
