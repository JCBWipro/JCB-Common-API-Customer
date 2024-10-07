package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.config.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:05-10-2024
 * project: JCB-Common-API-Customer
 */


@Service
public class AppConfigService {

    @Autowired
    AppConfiguration config;

    public AppConfiguration getAppConfig() {
        return config;
    }
}