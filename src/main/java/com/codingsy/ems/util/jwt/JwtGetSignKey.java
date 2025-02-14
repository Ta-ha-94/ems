package com.codingsy.ems.util.jwt;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import io.jsonwebtoken.security.Keys;

@Component
public class JwtGetSignKey {
	
	@Value("jwt.secret")
	private String secretKey;
	
	public Key getSignKey() {
//		return Keys.hmacShaKeyFor(secretKey.getBytes());
		return null;
	}
}
