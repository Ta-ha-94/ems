package com.codingsy.ems.security.config.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

import com.codingsy.ems.security.handler.SpaCsrfTokenRequestHandler;

/**
 * Customizer for CSRF configuration in Spring Security.
 * <p>
 * This class configures CSRF protection, setting up a custom CSRF token request handler
 * and specifying endpoints to ignore CSRF protection.
 * </p>
 */
@Component
public class CsrfConfigCustomizer implements DefaultCustomizer<CsrfConfigurer<HttpSecurity>> {
	SpaCsrfTokenRequestHandler csrfTokenRequestHandler = new SpaCsrfTokenRequestHandler();
	
	public Customizer<CsrfConfigurer<HttpSecurity>> customize(){
		return customizer;
	}
	
	/**
     * Customizes the CSRF configuration.
     * <p>
     * - Uses a custom CSRF token request handler.<br>
     * - Disables CSRF for authentication endpoints.<br>
     * - Configures CSRF token repository using cookies (not HTTP-only).
     * </p>
     *
     * @return The configured CSRF customizer.
     */
	private final Customizer<CsrfConfigurer<HttpSecurity>> customizer = t ->
		t
		.csrfTokenRequestHandler(csrfTokenRequestHandler)
		.ignoringRequestMatchers("/auth/register", "/auth/login", "/auth/invalidSession", "/error")
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
}
