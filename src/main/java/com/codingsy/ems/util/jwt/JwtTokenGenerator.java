package com.codingsy.ems.util.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenGenerator {
	
	private JwtGetSignKey jwtGetSignKey;
	
	public JwtTokenGenerator(JwtGetSignKey jwtGetSignKey) {
		this.jwtGetSignKey = jwtGetSignKey;
	}
	
	public String generateToken(String username) {
//		return Jwts.builder()
//			.setSubject(username)
//			.setIssuedAt(new Date())
//			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //	1 hour
//			.signWith(jwtGetSignKey.getSignKey(), SignatureAlgorithm.HS256)
//			.compact();
		return null;
	}
}
