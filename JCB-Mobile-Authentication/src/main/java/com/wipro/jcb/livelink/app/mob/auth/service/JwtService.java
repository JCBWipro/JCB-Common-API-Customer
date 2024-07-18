package com.wipro.jcb.livelink.app.mob.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public String generateToken(String userName, String roleName) {
        //All the combined components is called as a "Claims" in JWT
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, roleName);
    }

	private String createToken(Map<String, Object> claims, String userName, String roleName) {
		 Map<String, Object> rolesClaim = new HashMap<>();
		 rolesClaim.put("roles", roleName);
		return Jwts.builder().setClaims(claims).setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2))
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.addClaims(rolesClaim)
				.compact(); // Using HS256 Algorithm
	}

    private Key getSignKey() {
        //This will provide the SignKey based on our own Secret. We are using Base64 Secret
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
