package com.wipro.jcb.livelink.app.util;

import java.security.Key;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
String role;
	
	public void validateToken(final String token){
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}
	
	private Key getSignKey() {
		//This will provide the SignKey based on our own Secret. We are using Base64 Secret
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractRole(String token) {
//		extractClaim(token, Claims::getIssuer);
//		return role;
		return extractClaim(token, Claims::getIssuer);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		//role = claims.get("roles", String.class);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

}
