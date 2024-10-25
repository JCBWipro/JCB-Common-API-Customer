package com.wipro.jcb.livelink.app.alerts.service.impl;

import com.wipro.jcb.livelink.app.alerts.commonUtils.AppToken;
import com.wipro.jcb.livelink.app.alerts.commonUtils.UserToken;
import com.wipro.jcb.livelink.app.alerts.entity.UserNotificationDetail;
import com.wipro.jcb.livelink.app.alerts.exception.UsernameNotFoundException;
import com.wipro.jcb.livelink.app.alerts.repo.UserNotificationDetailRepo;
import com.wipro.jcb.livelink.app.alerts.service.LivelinkUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Slf4j
@Service
@PropertySource("application.properties")
public class LivelinkUserTokenServiceImpl implements LivelinkUserTokenService {

    @Autowired
    UserNotificationDetailRepo userNotificationDetailRepo;

    @Override
    public UserToken getUserTokenByUsername(String username) {
        log.debug("getUserTokenByUsername: Trying to fetch UserToken details for username: {}", username);

        UserNotificationDetail detail = userNotificationDetailRepo.findByUserNameToken(username);
        if (detail == null) {
            log.warn("getUserTokenByUsername: No UserNotificationDetail found for username: {}", username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        } else {
            String accessToken = detail.getAccessToken();
            UserToken userToken = new UserToken();
            userToken.setUserName(username);

            AppToken appToken = new AppToken();
            appToken.setAppFCMKey(accessToken);
            userToken.getAccessToken().put(username, appToken);

            return userToken;
        }
    }

    @Override
    public UserToken setUserTokenByUsername(UserToken userToken) {
        log.debug("setUserTokenByUsername: Setting UserToken details for username: {}", userToken.getUserName());

        UserNotificationDetail detail = userNotificationDetailRepo.findByUserNameToken(userToken.getUserName());

        if (detail == null) {
            detail = new UserNotificationDetail();
            detail.setUserName(userToken.getUserName());
        }

        AppToken appToken = userToken.getAccessToken().get(userToken.getUserName());
        if (appToken != null) {
            detail.setAccessToken(appToken.getAppFCMKey());
            userNotificationDetailRepo.save(detail);
        } else {
            log.warn("setUserTokenByUsername: AppToken not found for username: {}", userToken.getUserName());
        }

        return userToken;
    }

    // Helper method to construct UserToken from UserNotificationDetail
    private UserToken buildUserTokenFromDetails(UserNotificationDetail detail) {
        UserToken userToken = new UserToken();
        userToken.setUserName(detail.getUserName());
        AppToken appToken = new AppToken();
        appToken.setAppFCMKey(detail.getAccessToken());
        userToken.getAccessToken().put(detail.getUserName(), appToken);
        return userToken;
    }


    @Override
    @CacheEvict(value = "LivelinkUserToken", key = "#username")
    public void removeUserTokenByUsername(String username) {
    }
}