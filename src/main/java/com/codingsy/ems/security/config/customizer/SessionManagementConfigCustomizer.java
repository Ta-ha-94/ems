package com.codingsy.ems.security.config.customizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
public class SessionManagementConfigCustomizer implements DefaultCustomizer<SessionManagementConfigurer<HttpSecurity>> {
	
	@Value("${security.sessions.max-user:3}")
	private int maxSessionsForUser;
	
	@Value("${security.sessions.max-prevents-login:true}")
	private boolean maxSessionsPreventsLogin;
	
	private final Customizer<SessionManagementConfigurer<HttpSecurity>> customizer = t -> 
		t
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Since we are using JWT, Spring will never create an HTTP session.
//		.invalidSessionUrl("/auth/invalidSession")
//		.maximumSessions(maxSessionsForUser)
//		.maxSessionsPreventsLogin(maxSessionsPreventsLogin);
		
	public Customizer<SessionManagementConfigurer<HttpSecurity>> customize() {
		return customizer;
	}
}
