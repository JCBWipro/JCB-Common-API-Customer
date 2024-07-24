package com.wipro.jcb.livelink.app.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:22-07-2024
 * project: JCB_NewRepo
 */
@Data
@Configuration
public class UnicelConfig {

    @Value("${unicel.account.url}")
    private String accountUrl;
}
