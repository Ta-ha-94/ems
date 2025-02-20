package com.codingsy.ems.security.config.customizer;

import java.time.Duration;
import java.util.function.Consumer;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;

import com.codingsy.ems.security.handler.SpaCsrfTokenRequestHandler;

@Component
public class CsrfConfigCustomizer implements DefaultCustomizer<CsrfConfigurer<HttpSecurity>> {
	private final CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler =  new CsrfTokenRequestAttributeHandler();
	
	public Customizer<CsrfConfigurer<HttpSecurity>> customize(){
		return customizer;
	}
	
	private final Customizer<CsrfConfigurer<HttpSecurity>> customizer = t ->
		t
		.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
		.ignoringRequestMatchers("/auth/register", "/auth/login", "/auth/invalidSession", "/error")
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		
//		private static CookieCsrfTokenRepository customCsrfTokenRepository() {
//	        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//	        repository.setCookieCustomizer(cookie ->
//	            cookie
//	                .secure(false)               // Change to true in production
//	                .sameSite("Strict")          // Protects against CSRF attacks
//	                .maxAge(Duration.ofHours(1)) // Token valid for 1 hour
//	        );
//	        return repository;
//	    }
}
