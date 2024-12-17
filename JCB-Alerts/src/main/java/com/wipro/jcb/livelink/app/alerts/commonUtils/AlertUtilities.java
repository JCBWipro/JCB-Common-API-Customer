package com.wipro.jcb.livelink.app.alerts.commonUtils;

import com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.alerts.constants.MessagesList;
import com.wipro.jcb.livelink.app.alerts.entity.UserNotificationDetail;
import com.wipro.jcb.livelink.app.alerts.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.alerts.repo.UserNotificationDetailRepo;
import com.wipro.jcb.livelink.app.alerts.service.AppServerTokenService;
import com.wipro.jcb.livelink.app.alerts.service.LivelinkUserTokenService;
import com.wipro.jcb.livelink.app.alerts.service.response.Message;
import com.wipro.jcb.livelink.app.alerts.service.response.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.google.auth.oauth2.GoogleCredentials;

import static com.wipro.jcb.livelink.app.alerts.constants.AppServerConstants.timezone;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 */
@Slf4j
@Component
public class AlertUtilities {

    @Value("${openstreetmap.basepath}")
    String openstreetmapBasepath;

    @Value("${livelinkserver.connectTimeout}")
    int connectTimeout;
    @Value("${livelinkserver.readTimeout}")
    int readTimeout;

    @Value("${fcmv1.url}")
    String fcmUrlV1;

    @Value("${fcm1.json}")
    String fcmJson;

    @Autowired
    LivelinkUserTokenService livelinkUserTokenService;

    @Autowired
    UserNotificationDetailRepo userNotificationDetailRepo;

    @Autowired
    AppServerTokenService appServerTokenService;

    private static RestTemplate restTemplate;
    private static HttpEntity<?> request;


    public Date getDate(String date) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateFormat);
        Date dt = null;
        try {
            dt = parseFormat.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public Date getDateTime(String date) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateTimeFormat);
        Date dt = null;
        try {
            dt = parseFormat.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public String getStartDate(int daysBefore) {
        return LocalDate.now().minusDays(daysBefore).toString();
    }

    public String getStartDateTime(int daysBefore) {
        return getDateTimeInString(LocalDateTime.now().minusDays(daysBefore).toDate());
    }

    public Long getCurrentTime() {
        return LocalDateTime.now(DateTimeZone.forID(timezone)).toDate().getTime();
    }

    public String getDateTimeInString(Date date) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateTimeFormat);
        return parseFormat.format(date);
    }

    public String getEndDate(int daysAfter) {
        return getDateInString(LocalDateTime.now(DateTimeZone.forID(timezone)).plusDays(daysAfter).toDate());
    }

    public String getEndDateTime(int daysAfter) {
        return getDateTimeInString(LocalDateTime.now(DateTimeZone.forID(timezone)).plusDays(daysAfter).toDate());
    }

    public String getDateInString(Date date) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateFormat);
        return parseFormat.format(date);
    }

    public Date getStartDateTimeInDateFormat(int daysBefore) {
        return LocalDateTime.now(DateTimeZone.forID(timezone)).minusDays(daysBefore).toDate();
    }

    public Date getStartDateTimeWithMinutes(int minutes) {
        return LocalDateTime.now(DateTimeZone.forID(timezone)).minusMinutes(minutes).toDate();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        return factory;
    }

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate(clientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return restTemplate;
    }

    private HttpEntity<?> getHttpRequest() {
        if (request == null) {
            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Accept", "text/html");
            headers.add("Authorization", "Basic amNiX2xvY2F0aW9uOmwwY0B0fDBe");
            request = new HttpEntity<>(headers);
        }
        return request;
    }

    public List<Date> getDateMap(Date startDate, Date endDate) {
        List<Date> list = new ArrayList<>();
        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.time.LocalDate s = java.time.LocalDate.parse(format.format(startDate));
            java.time.LocalDate e = java.time.LocalDate.parse(format.format(endDate));
            while (!s.isAfter(e)) {
                list.add(getDate(format.format(Date.from(s.atStartOfDay(ZoneId.of(timezone)).toInstant()))));
                s = s.plusDays(1);
            }
            return list;
        }
        return null;
    }
    public Boolean enableDisableNotification(String userName, Boolean enable) throws ProcessCustomError {
        try {
            if (appServerTokenService.getTokenByUsername(userName) != null) {
                if (userName != null) {
                    UserToken userToken = livelinkUserTokenService.getUserTokenByUsername(userName);
                    ConcurrentHashMap<String, AppToken> accessTokenMap = userToken.getAccessToken();
                    AppToken appToken = accessTokenMap.get(userName);

                    if (appToken != null) {
                        appToken.setEnable(enable);
                        userToken.setAccessToken(accessTokenMap);
                        livelinkUserTokenService.setUserTokenByUsername(userToken);

                        String appFCMKey = appToken.getAppFCMKey();
                        log.info(" app FCM Key value is: {}", appFCMKey);
                        if (appFCMKey != null) {
                            Optional<UserNotificationDetail> existingDetail = userNotificationDetailRepo.findById(appFCMKey);
                            existingDetail.ifPresent(userNotificationDetail -> {
                                userNotificationDetail.setEnableNotification(enable);
                                userNotificationDetailRepo.save(userNotificationDetail);
                            });
                        } else {
                            log.warn("enableDisableNotification: appFCMKey is null for token: {}", userName);
                        }

                        return enable;
                    }
                } else {
                    log.warn("enableDisableNotification: AppToken not found for username: {}", userName);
                    throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                log.warn("enableDisableNotification: Username not found for token: {}", userName);
                throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.EXPECTATION_FAILED.value(),
                        HttpStatus.EXPECTATION_FAILED.getReasonPhrase());
            }
        } catch (final Exception e) {
            log.error("enableDisableNotification: Failed while processing for token: {}", userName, e);
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return enable;
    }

    public Date getPreviousDay(Date day) {
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(day));
        return date.minusDays(1).toDate();

    }

    public String getDDMMYY(int daysBefore) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateFormatForMachineUpdate);
        return parseFormat.format(LocalDateTime.now(DateTimeZone.forID(timezone)).minusDays(daysBefore).toDate());
    }

    public void sendNotificationUsingV1(MessageContent pushNotification) {


        try {
            // Firebase Authorization Token Creation/Update
            String fireBaseToken = getAccessToken();

            // Payload for firebase notification
            Message pushData = new Message();
            pushData.setMessage(pushNotification);

            // Header
            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Authorization", "Bearer " + fireBaseToken);
            headers.add("Content-Type", "application/json");

            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<Message> request = new HttpEntity<>(pushData, headers);
            final ResponseEntity<String> out = restTemplate.exchange(fcmUrlV1, HttpMethod.POST, request,
                    String.class);

            if (out.getStatusCode() == HttpStatus.OK) {
                log.info("PUSH NOTIFICATION SEND {}", out);
            } else {
                log.info("PUSH NOTIFICATION FAULTED{}RESPONSE{}", pushNotification, out.getBody());
            }

        } catch (Exception e) {
            log.error("Exception occurred in push message {}", e.getMessage());
        }

    }

    private String getAccessToken() {
        String token = "";
        try {

            String[] scope = new String[]{"https://www.googleapis.com/auth/firebase.messaging"};
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new FileInputStream(fcmJson))
                    .createScoped(List.of(scope));
            googleCredentials.refreshIfExpired();
            googleCredentials.refreshAccessToken();
            log.info("googleCredentials : {}", googleCredentials);
            token = googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            log.info("Exception in firebase accessToken {}", e.getMessage());
        }
        return token;
    }

}

