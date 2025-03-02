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
import com.codingsy.ems.security.filter.CsrfCookieFilter;

/**
* Configuration class for securing the Codingsy Employee Management System (EMS) in a production
* environment. This class defines the security filter chain, authentication mechanisms, and
* security-related beans specific to the production profile.
* <p>
* This configuration enforces HTTPS, limits sessions to one per user, integrates CSRF protection
* with a custom filter, and configures authentication methods (form login and HTTP basic).
* It is activated only when the {@code prod} profile is active.
* </p>
* <p>
* The configuration relies on customizers ({@link DefaultCustomizer}) to modularize and extend
* security settings for various aspects such as session management, CORS, CSRF, authorization,
* and exception handling.
* </p>
*
* @author Taha
* @version 1.0
* @see SecurityFilterChain
* @see DefaultCustomizer
* @see CsrfCookieFilter
* @since 1.0
*/
@Profile(value = "prod")
@Configuration
public class SecurityProdConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityProdConfig.class);

	private final DefaultCustomizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigCustomizer;
    private final DefaultCustomizer<CsrfConfigurer<HttpSecurity>> csrfConfigCustomizer;
    private final DefaultCustomizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsConfigCustomizer;
    private final DefaultCustomizer<SecurityContextConfigurer<HttpSecurity>> securityContextConfigCustomizer;
    private final DefaultCustomizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> channelSecurityConfigCustomizer;
    private final DefaultCustomizer<FormLoginConfigurer<HttpSecurity>> formLoginConfigCustomizer;
    private final DefaultCustomizer<HttpBasicConfigurer<HttpSecurity>> httpBasicConfigCustomizer;
    private final DefaultCustomizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigCustomizer;
	private final DefaultCustomizer<CorsConfigurer<HttpSecurity>> corsConfigCustomizer;
	
	/**
     * Constructs a new {@code SecurityProdConfig} with the required customizers for
     * configuring various aspects of the security filter chain.
     *
     * @param corsConfigCustomizer the customizer for CORS configuration
     * @param sessionManagementConfigCustomizer the customizer for session management
     * @param csrfConfigCustomizer the customizer for CSRF protection
     * @param authorizeHttpRequestsConfigCustomizer the customizer for authorization rules
     * @param securityContextConfigCustomizer the customizer for security context
     * @param channelSecurityConfigCustomizer the customizer for channel security (e.g., HTTPS)
     * @param formLoginConfigCustomizer the customizer for form login
     * @param httpBasicConfigCustomizer the customizer for HTTP basic authentication
     * @param exceptionHandlingConfigCustomizer the customizer for exception handling
     */
    public SecurityProdConfig(DefaultCustomizer<CorsConfigurer<HttpSecurity>> corsConfigCustomizer,
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
     * Defines the default security filter chain for production environment.
     * <p>
     * Configures the security settings including session management (max one session per user),
     * channel security (enforces HTTPS), CORS, CSRF protection with a custom {@link CsrfCookieFilter},
     * authorization rules, form login, HTTP basic authentication, and exception handling.
     * If a new login is attempted, the existing session is invalidated, and the user is redirected
     * to the {@code /invalidSession} page.
     * </p>
     *
     * @param http the {@link HttpSecurity} object to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
	@Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		try {
			http
			    .securityContext(securityContextConfigCustomizer.customize())
				.requiresChannel(channelSecurityConfigCustomizer.customize()) //Only HTTPS requests are allowed
				.sessionManagement(sessionManagementConfigCustomizer.customize())
				.cors(corsConfigCustomizer.customize())
			    .csrf(csrfConfigCustomizer.customize())
			    .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
			    .authorizeHttpRequests(authorizeHttpRequestsConfigCustomizer.customize())
			    .formLogin(formLoginConfigCustomizer.customize())
			    .httpBasic(httpBasicConfigCustomizer.customize())
			    .exceptionHandling(exceptionHandlingConfigCustomizer.customize());
		} catch (Exception e) {
			log.error("Failed to configure security filter chain", e);
			e.printStackTrace();
	        throw e; // Re-throw to fail bean creation if critical
			
		}
		return http.build();
    }
	
	/**
     * Provides a password encoder bean using Spring Security's delegating password encoder.
     * <p>
     * This encoder supports multiple password encoding formats (e.g., bcrypt, pbkdf2) and
     * automatically detects the encoding used for a given password.
     * </p>
     *
     * @return a {@link PasswordEncoder} instance
     */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	/**
     * Provides a compromised password checker bean using the HaveIBeenPwned REST API.
     * <p>
     * This checker verifies if a password has been exposed in known data breaches,
     * enhancing security by rejecting compromised passwords.
     * </p>
     *
     * @return a {@link CompromisedPasswordChecker} instance
     */
	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
}
