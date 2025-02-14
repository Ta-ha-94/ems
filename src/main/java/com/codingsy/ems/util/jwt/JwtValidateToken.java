package com.codingsy.ems.util.jwt;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;

@Component
public class JwtValidateToken {
	
	private JwtGetSignKey jwtGetSignKey;
	
	public JwtValidateToken(JwtGetSignKey jwtGetSignKey) {
		this.jwtGetSignKey = jwtGetSignKey;
	}
	
	public String extractUsername(String token) {
		//return extractClaim(token, Claims::getSubject);
		return null;
	}
	
	public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }
	
	private boolean isTokenExpired(String token) {
        //return extractClaim(token, Claims::getExpiration).before(new Date());
		return false;
    }
	
//	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//		final Claims claims = Jwts.parserBuilder()
//								.setSigningKey(jwtGetSignKey.getSignKey())
//								.build()
//								.parseClaimsJws(token)
//								.getBody();
//		return claimResolver.apply(claims);
//		return null;
//	}
}
