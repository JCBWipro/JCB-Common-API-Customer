package com.wipro.jcb.livelink.app.alerts.service;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AppServerToken;
import org.springframework.stereotype.Component;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Component
public interface AppServerTokenService {
    /**
     * This is to retrieve userName from token of rest query
     *
     * @param userName
     *            header accepted from request
     * @return username of logged in user
     */
    public String getTokenByUsername(String userName);

    /**
     * This sets userName value to mapped key token
     *
     * @param appServerToken
     *            is unique token generated runtime for user
     * @return userName on successful request
     */
    public String setUsernameByToken(AppServerToken appServerToken);

    /**
     * This removes token --> userNAme mapping by using key token
     *
     * @param token
     *            is unique identity of logged in user
     */
    public void removeUsernameByToken(String token);
}