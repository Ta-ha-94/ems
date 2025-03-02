package com.codingsy.ems.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Configures custom security rules for incoming requests in non-production environments.
 * This class defines the security behavior for the application, including authentication mechanisms,
 * session management, CORS, CSRF, and exception handling. It also integrates JWT-based authentication
 * filters for token generation and validation.
 * 
 * <p>This configuration is applied only when the "prod" profile is not active.</p>
 * 
 * @see SecurityFilterChain
 * @see PasswordEncoder
 * @see CompromisedPasswordChecker
 */
@Configuration
@Profile(value = "!prod")
public class SecurityConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
	private final DefaultCustomizer<CorsConfigurer<HttpSecurity>> corsConfigCustomizer;
    private final DefaultCustomizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigCustomizer;
    private final DefaultCustomizer<CsrfConfigurer<HttpSecurity>> csrfConfigCustomizer;
    private final DefaultCustomizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsConfigCustomizer;
    private final DefaultCustomizer<SecurityContextConfigurer<HttpSecurity>> securityContextConfigCustomizer;
    private final DefaultCustomizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> channelSecurityConfigCustomizer;
    private final DefaultCustomizer<FormLoginConfigurer<HttpSecurity>> formLoginConfigCustomizer;
    private final DefaultCustomizer<HttpBasicConfigurer<HttpSecurity>> httpBasicConfigCustomizer;
    private final DefaultCustomizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigCustomizer;	
	
    
    /**
     * Constructs a new {@code SecurityConfig} with the provided customizers for various security configurations.
     * 
     * @param corsConfigCustomizer Customizer for CORS configuration.
     * @param sessionManagementConfigCustomizer Customizer for session management configuration.
     * @param csrfConfigCustomizer Customizer for CSRF configuration.
     * @param authorizeHttpRequestsConfigCustomizer Customizer for request authorization configuration.
     * @param securityContextConfigCustomizer Customizer for security context configuration.
     * @param channelSecurityConfigCustomizer Customizer for channel security configuration.
     * @param formLoginConfigCustomizer Customizer for form-based login configuration.
     * @param httpBasicConfigCustomizer Customizer for HTTP Basic authentication configuration.
     * @param exceptionHandlingConfigCustomizer Customizer for exception handling configuration.
     */
	public SecurityConfig(DefaultCustomizer<CorsConfigurer<HttpSecurity>> corsConfigCustomizer,
			DefaultCustomizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigCustomizer,
			DefaultCustomizer<CsrfConfigurer<HttpSecurity>> csrfConfigCustomizer,
			DefaultCustomizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsConfigCustomizer,
			DefaultCustomizer<SecurityContextConfigurer<HttpSecurity>> securityContextConfigCustomizer,
			DefaultCustomizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> channelSecurityConfigCustomizer,
			DefaultCustomizer<FormLoginConfigurer<HttpSecurity>> formLoginConfigCustomizer,
			DefaultCustomizer<HttpBasicConfigurer<HttpSecurity>> httpBasicConfigCustomizer,
			DefaultCustomizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigCustomizer) {
		this.corsConfigCustomizer = corsConfigCustomizer;
		this.sessionManagementConfigCustomizer = sessionManagementConfigCustomizer;
		this.csrfConfigCustomizer = csrfConfigCustomizer;
		this.authorizeHttpRequestsConfigCustomizer = authorizeHttpRequestsConfigCustomizer;
		this.securityContextConfigCustomizer = securityContextConfigCustomizer;
		this.channelSecurityConfigCustomizer = channelSecurityConfigCustomizer;
		this.formLoginConfigCustomizer = formLoginConfigCustomizer;
		this.httpBasicConfigCustomizer = httpBasicConfigCustomizer;
		this.exceptionHandlingConfigCustomizer = exceptionHandlingConfigCustomizer;
	}


	/**
	 * Configures the default security filter chain for the application.
	 * This method sets up various security configurations, including CORS, CSRF, session management,
	 * request authorization, form-based login, HTTP Basic authentication, and exception handling.
	 * It also integrates JWT-based filters for token generation and validation.
	 * 
	 * @param http The {@link HttpSecurity} object used to configure the security filter chain.
	 * @return The configured {@link SecurityFilterChain}.
	 * @throws Exception If an error occurs during configuration.
	 */
	@Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
	        .securityContext(securityContextConfigCustomizer.customize())
	    	.requiresChannel(channelSecurityConfigCustomizer.customize()) //Only HTTPS requests are allowed
	    	.sessionManagement(sessionManagementConfigCustomizer.customize())
	    	.cors(corsConfigCustomizer.customize())
	        .csrf(csrfConfigCustomizer.customize())
	        .addFilterAfter(new JwtGeneratorFilter(), BasicAuthenticationFilter.class)
            .addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class)
	        .authorizeHttpRequests(authorizeHttpRequestsConfigCustomizer.customize())
	        .formLogin(formLoginConfigCustomizer.customize())
	        .httpBasic(httpBasicConfigCustomizer.customize())
	        .exceptionHandling(exceptionHandlingConfigCustomizer.customize());
	    return http.build();
    }
	
	/**
	 * Provides a delegating password encoder that supports multiple encoding algorithms.
	 * This encoder is used to securely encode and verify user passwords.
	 * 
	 * @return A {@link PasswordEncoder} instance.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	/**
	 * Provides a password checker that verifies if a password has been compromised
	 * using the "Have I Been Pwned" REST API.
	 * 
	 * @return A {@link CompromisedPasswordChecker} instance.
	 */
	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
}
