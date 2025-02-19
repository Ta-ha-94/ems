package com.codingsy.ems.security.config.customizer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeHttpRequestsConfigCustomizer implements DefaultCustomizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {
	
	public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customize(){
		return customizer;
	}
	
	private final Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer = t -> {
			t.requestMatchers("/auth/register", "/auth/login", "/auth/invalidSession", "/auth/logout", "/error").permitAll(); // ✅ Public access
			t.requestMatchers(HttpMethod.GET, "/employees").hasAnyRole("EMPLOYEE", "ADMIN"); // GET requests
	        t.requestMatchers(HttpMethod.POST, "/employees").hasRole("ADMIN"); // POST requests
	        t.requestMatchers(HttpMethod.PUT, "/employees").hasRole("ADMIN"); // PUT requests
	        t.requestMatchers(HttpMethod.DELETE, "/employees").hasRole("ADMIN"); // DELETE requests
            t.anyRequest().authenticated(); // Protect other APIs
	};
}