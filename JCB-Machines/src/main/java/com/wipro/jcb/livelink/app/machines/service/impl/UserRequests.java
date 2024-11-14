package com.wipro.jcb.livelink.app.machines.service.impl;

import java.io.IOException;

import org.json.JSONObject;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.jcb.livelink.app.machines.cache.impl.LivelinkUserTokenServiceImpl;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.LoginRequest;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.request.LoginRequestLL;
import com.wipro.jcb.livelink.app.machines.service.response.LoginResponseLL;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserRequests {
	@Autowired
	LivelinkUserTokenServiceImpl livelinkUserTokenServiceImpl;
	
	@Autowired
	AppServerTokenServiceImpl appServertoken;
	
	@Value("${livelinkserver.connectTimeout}")
	private int connectTimeout;
	
	@Value("${livelinkserver.readTimeout}")
	private int readTimeout;
	
	private ClientHttpRequestFactory clientHttpRequestFactory() {
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectionRequestTimeout(readTimeout);
		factory.setConnectTimeout(connectTimeout);
		return factory;
	}

	public LoginResponseLL userlogin(LoginRequest requestEntity) throws ProcessCustomError {
		try {
			final LoginRequestLL loginReq = new LoginRequestLL(requestEntity.getUserName(), requestEntity.getUserType(),
					requestEntity.getPassword());
			final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("OrgKey", AppServerConstants.livelinkAppServerOrgKey);
			headers.add("Content-Type", "application/json");
			final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			final HttpEntity<LoginRequestLL> request = new HttpEntity<>(loginReq, headers);
			long start = System.currentTimeMillis();
			log.info("Wipro API for "+requestEntity.getUserName()+"-"+AppServerConstants.livelinkAppServerBaseUrl + "/user/login"+"- Header "+request);
			final ResponseEntity<String> out = restTemplate.exchange(
					AppServerConstants.livelinkAppServerBaseUrl + "/user/login", HttpMethod.POST, request,
					String.class);
			log.info("Wipro API Response :" +requestEntity.getUserName()+"-"+out);
			 long end = System.currentTimeMillis();
			 long elapsedTime = end - start;
			 log.info("Wipro API Duration :"+requestEntity.getUserName()+"-"+elapsedTime);
			if (out.getStatusCode() == HttpStatus.OK) {
				final String responseentity = out.getBody();
				final ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.readValue(responseentity, LoginResponseLL.class);
				} catch (final IOException e) {
					final JSONObject jobj = new JSONObject(responseentity);
					log.error("userlogin error message: "+requestEntity.getUserName()+"-"+ jobj.getString("message"));
					if (MessagesList.WIPRO_INVALID_LOGIN_DETAILS.equalsIgnoreCase(jobj.getString("message"))) {
						throw new ProcessCustomError(MessagesList.APP_SERVER_INVALID_LOGIN_DETAILS,
								MessagesList.APP_SERVER_INVALID_LOGIN_DETAILS, HttpStatus.EXPECTATION_FAILED);
					} else {
						throw new ProcessCustomError(jobj.getString("message"), jobj.getString("message"),
								HttpStatus.EXPECTATION_FAILED);
					}
				}
			} else {
				log.error("userlogin : Unable to login user "+requestEntity.getUserName());
				throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.ACCEPTED);
			}
		} catch (final ResourceAccessException e) {		
			log.error("userlogin : wipro API not working " + requestEntity.getUserName()+"-"+e);
			throw new ProcessCustomError(MessagesList.SERVER_DOWN_MESSAGE, e.getMessage(), HttpStatus.EXPECTATION_FAILED);		
		} catch (final ProcessCustomError e) {
			throw e;
		} catch (final Exception e) {
			e.printStackTrace();
			log.error("userlogin failed with " +requestEntity.getUserName()+"-"+ e.getMessage());
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

}