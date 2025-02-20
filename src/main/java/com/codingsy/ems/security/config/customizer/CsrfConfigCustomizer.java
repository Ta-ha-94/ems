package com.codingsy.ems.security.config.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

import com.codingsy.ems.security.handler.SpaCsrfTokenRequestHandler;

@Component
public class CsrfConfigCustomizer implements DefaultCustomizer<CsrfConfigurer<HttpSecurity>> {
	private final SpaCsrfTokenRequestHandler csrfTokenRequestAttributeHandler =  new SpaCsrfTokenRequestHandler();
	
	public Customizer<CsrfConfigurer<HttpSecurity>> customize(){
		return customizer;
	}
	
	private final Customizer<CsrfConfigurer<HttpSecurity>> customizer = t ->
		t
		.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
		.ignoringRequestMatchers("/auth/register", "/auth/login", "/auth/invalidSession", "/error")
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
}
