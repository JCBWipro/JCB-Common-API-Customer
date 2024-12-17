package com.wipro.jcb.livelink.app.machines.commonUtils;

import com.wipro.jcb.livelink.app.machines.cache.impl.LivelinkUserTokenServiceImpl;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.dto.LoginRequest;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.enums.FilterSearchType;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.AlertService;
import com.wipro.jcb.livelink.app.machines.service.MachineService;
import com.wipro.jcb.livelink.app.machines.service.UserService;
import com.wipro.jcb.livelink.app.machines.service.impl.AppServerTokenServiceImpl;
import com.wipro.jcb.livelink.app.machines.service.impl.UserRequests;
import com.wipro.jcb.livelink.app.machines.service.response.AddressResponse;
import com.wipro.jcb.livelink.app.machines.service.response.Filter;
import com.wipro.jcb.livelink.app.machines.service.response.LoginResponseLL;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.wipro.jcb.livelink.app.machines.constants.AppServerConstants.timezone;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 */
@Slf4j
@Component
public class Utilities {

    @Value("${openstreetmap.basepath}")
    String openstreetmapBasepath;

    @Value("${livelinkserver.connectTimeout}")
    int connectTimeout;
    
    @Value("${livelinkserver.readTimeout}")
    int readTimeout;
    
    @Value("${user.tokenRenewTime}")
	private Long tokenRenewTime;

    @Value("${user.secret}")
	private String secret;

    @Value("${userUri}")
    public String userUri;

    @Autowired
    AlertService alertService;
    
    @Autowired
    @Lazy
    MachineService machineService;
    
    @Autowired
    UserService userService;
    
    @Autowired
	LivelinkUserTokenServiceImpl livelinkUserTokenService;

    @Autowired
   	AppServerTokenServiceImpl appServerTokenService;

    @Autowired
    UserRequests userRequests;

    @Autowired
    private WebClient.Builder webClientBuilder;

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

    public AddressResponse getLocationDetails(String lat, String lng) throws ProcessCustomError {
        String address = "";
        try {
            long start = System.currentTimeMillis();

            //final String url = "https://" + openstreetmapBasepath + "/reverse.php";
           // final String url = "https://" + openstreetmapBasepath + "/reverse.php";
            //final String url = "https://nominatim.openstreetmap.org/ui/reverse.html";
            final String url = "https://nominatim.openstreetmap.org/ui/search.html";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("format", "jsonv2") // Note: Using jsonv2 as per your request
                    .queryParam("lat", lat)
                    .queryParam("lon", lng)
                    .queryParam("zoom", 12); // Adding zoom parameter

            final ResponseEntity<String> out = getRestTemplate().exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    getHttpRequest(),
                    String.class
            );

            if (out.getStatusCode() == HttpStatus.OK) {
                final String responseEntity = out.getBody();
                final JSONObject jsonObj = new JSONObject(responseEntity);
                if (jsonObj.has("display_name")) {
                    address = jsonObj.getString("display_name");
                }
                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                log.info("LocationV3 API Duration :{}", elapsedTime);
                return new AddressResponse(address);
            } else {
                log.error("getLocationDetails : Unable to fetch address. Status Code: {}", out.getStatusCode());
                throw new ProcessCustomError("Unable to fetch address from OpenStreetMap", HttpStatus.BAD_REQUEST);
            }
        } catch (final Exception ex) {
            log.error("getLocationDetails : Unable to fetch address. Error: {}", ex.getMessage());
            ex.printStackTrace();
            log.info("Exception occurred for LocationV3 API :Param-{}-{} Exception - {}", lat, lng, ex.getMessage());
            return new AddressResponse(address);
        }
    }

    public List<String> getSuggestions(String word, String userName, FilterSearchType type) {
        if (FilterSearchType.ALERT.equals(type)) {
            return alertService.getSuggestions(word, userName);
        } else if (FilterSearchType.MACHINE.equals(type)) {
            return machineService.getSuggestions(word, userName);
        } else if (FilterSearchType.CUSTOMER.equals(type)) {
            return userService.getSuggestions(word, userName);
        }
        return new ArrayList<>();
    }

    public List<Filter> getFilters(String userName, FilterSearchType type) {
        if (FilterSearchType.ALERT.equals(type)) {
            return alertService.getFilters(userName);
        } else if (FilterSearchType.MACHINE.equals(type)) {
            return machineService.getFilters(userName);
        } else if (FilterSearchType.CUSTOMER.equals(type)) {
            return userService.getFiltersCustomer(userName);
        }
        return new ArrayList<>();
    }

    public String getUniqueID() {
        final UUID idOne = UUID.randomUUID();
        return idOne.toString();
    }
    
    public String updateLivelinkServerToken(User user, Boolean isForcefullUpdate) throws Exception {
		try {
			final Long now = LocalDate.now(DateTimeZone.forID(timezone)).toDate().getTime();
			final UserToken userToken = livelinkUserTokenService.getUserTokenByUsername(user.getUserName());
			if (userToken != null) {
				synchronized (userToken) {
					long issueDate;
					long expiryDate;
					if (isForcefullUpdate || userToken.getExpiresAt() != 0) {
						if (userToken.getExpiresAt() < now + tokenRenewTime) {
							log.info("Login api Calling for " + user.getUserName() + " ");
							ResponseEntity<String> response = webClientBuilder.build().get()
									.uri(userUri + "/api/user/getPwdByUserName/"+user.getUserName())
					        		.retrieve().toEntity(String.class).block();
							String decryptedPassword = response.getBody();
							final LoginRequest requestEntity = new LoginRequest(user.getUserName(),
									String.valueOf(user.getUserType()),
									decryptedPassword,
									"", "", false, false, null);
							final LoginResponseLL responseLL = userRequests.userlogin(requestEntity);
							String someDate = responseLL.getTokenIssuedDate();
							if (someDate != null) {
								if (!"NA".equals(someDate)) {
									final Date date = getDateTime(someDate);
									issueDate = date.getTime();
								} else {
									issueDate = Long.valueOf(0);
								}
							} else {
								issueDate = Long.valueOf(0);
							}
							someDate = responseLL.getTokenExpiryDate();
							if (someDate != null) {
								if (!"NA".equals(someDate)) {
									final Date date = getDateTime(someDate);
									expiryDate = date.getTime();
								} else {
									expiryDate = Long.valueOf(0);
								}
							} else {
								expiryDate = Long.valueOf(0);
							}
							userToken.setExpiresAt(expiryDate);
							userToken.setIssuedAt(issueDate);
							userToken.setLivelinkToken(responseLL.getTokenID());
							livelinkUserTokenService.setUserTokenByUsername(userToken);
							return responseLL.getTokenID();
						}
					}
				}
			}
		} catch (final ProcessCustomError exception) {

			log.info("Token remove for the user :"+user.getUserName());
			removeAllSessionForUser(user.getUserName());
			exception.printStackTrace();
			log.error("userlogin failed with " + exception.getMessage());
			throw exception;

		} catch (final Exception e) {
			e.printStackTrace();
			log.error("userlogin failed with " + e.getMessage());
			throw e;
		}
		return null;
	}
    
    public void removeAllSessionForUser(String userName) {
		try {
			final UserToken userToken = livelinkUserTokenService.getUserTokenByUsername(userName);
			if (userToken != null) {
				synchronized (userToken) {
					final ConcurrentHashMap<String, AppToken> appTokens = userToken.getAccessToken();
					for (final String token : appTokens.keySet()) {
						appServerTokenService.removeUsernameByToken(token);
					}
				}
			}
			livelinkUserTokenService.removeUserTokenByUsername(userName);
			log.info("Redis token removed for the user "+userName);
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error("Failed to remove all session for user " + ex.getMessage());
		}
	}
}

