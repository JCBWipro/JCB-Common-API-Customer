package com.wipro.jcb.livelink.app.dataprocess.service;

import java.util.LinkedHashMap;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
public interface LivelinkUserTokenService {
    /**
     *
     * @param username
     *            is unique identity of user
     * @return instance of class UserToken
     */
    public LinkedHashMap<?, ?> getUserTokenByUsername(String username);
}
