package com.wipro.jcb.livelink.app.apigateway.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class BlackList {

	private Set<String> blackListTokenSet = new HashSet<>();

    public void blacKListToken(String token){
        blackListTokenSet.add(token);
    }
    public boolean isBlackListed(String token){
        return blackListTokenSet.contains(token);
    }
    
}