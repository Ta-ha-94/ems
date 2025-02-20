package com.codingsy.ems.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.codingsy.ems.security.config.customizer.DefaultCustomizer;
import com.codingsy.ems.security.filter.JwtGeneratorFilter;
import com.codingsy.ems.security.filter.JwtValidationFilter;

import lombok.RequiredArgsConstructor;


/**
 * 	Defines custom security rules to be applied to the incoming requests. 
 * 	Specifies which type of authentication should be performed, http-based or form-based.
 * 	Specifies custom login/logout forms.
 * 	Max sessions for a user is 3 in other environments
 * 	New login will invalidate the new session and prevents login
 * 	The /invlidSession page will be shown
 */
@Configuration
@Profile(value = "!prod")
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final DefaultCustomizer<CorsConfigurer<HttpSecurity>> corsConfigCustomizer;
    private final DefaultCustomizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigCustomizer;
    private final DefaultCustomizer<CsrfConfigurer<HttpSecurity>> csrfConfigCustomizer;
    private final DefaultCustomizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsConfigCustomizer;
    private final DefaultCustomizer<SecurityContextConfigurer<HttpSecurity>> securityContextConfigCustomizer;
    private final DefaultCustomizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> channelSecurityConfigCustomizer;
    private final DefaultCustomizer<FormLoginConfigurer<HttpSecurity>> formLoginConfigCustomizer;
    private final DefaultCustomizer<HttpBasicConfigurer<HttpSecurity>> httpBasicConfigCustomizer;
    private final DefaultCustomizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigCustomizer;	
	
	@Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
	        .securityContext(securityContextConfigCustomizer.customize())
	    	.requiresChannel(channelSecurityConfigCustomizer.customize()) //Only HTTPS requests are allowed
	    	.sessionManagement(sessionManagementConfigCustomizer.customize())
	    	.cors(corsConfigCustomizer.customize())
	        .csrf(csrfConfigCustomizer.customize())
//	        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
	        .addFilterAfter(new JwtGeneratorFilter(), BasicAuthenticationFilter.class)
            .addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class)
	        .authorizeHttpRequests(authorizeHttpRequestsConfigCustomizer.customize())
	        .formLogin(formLoginConfigCustomizer.customize())
	        .httpBasic(httpBasicConfigCustomizer.customize())
	        .exceptionHandling(exceptionHandlingConfigCustomizer.customize());
	    return http.build();
    }
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
}
