package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.commonUtils.AppServerToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface AppServerTokenService {

    public String getUsernameByToken(String token);

    public String setUsernameByToken(AppServerToken appServerToken);

    public void removeUsernameByToken(String token);
}
