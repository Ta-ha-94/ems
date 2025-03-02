//package com.codingsy.ems.security.config;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Collections;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
//import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
//import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
//import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
//import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
//import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
//import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
//import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.web.cors.CorsConfiguration;
//
//import com.codingsy.ems.exception.CustomAccessDeniedHandler;
//import com.codingsy.ems.exception.CustomBasicAuthenticationEntryPoint;
//import com.codingsy.ems.security.config.customizer.DefaultCustomizer;
//import com.codingsy.ems.security.handler.SpaCsrfTokenRequestHandler;
//
///**
// * Unit tests for the {@link SecurityProdConfig} class, which configures Spring Security
// * for the production environment of the Codingsy Employee Management System (EMS).
// * <p>
// * These tests use Mockito to mock dependencies and verify the behavior of the
// * {@link SecurityProdConfig#defaultSecurityFilterChain(HttpSecurity)} method, ensuring
// * it correctly configures the security filter chain with customizers for various security
// * aspects, such as CORS, session management, CSRF, authorization, and exception handling.
// * </p>
// *
// * @author Taha
// * @version 1.0
// * @see SecurityProdConfig
// * @see DefaultCustomizer
// * @since 1.0
// */
//@ExtendWith(value = { MockitoExtension.class })
//public class SecurityProdConfigTest {
//
//	@Mock
//	private DefaultCustomizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<CsrfConfigurer<HttpSecurity>> csrfConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<SecurityContextConfigurer<HttpSecurity>> securityContextConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> channelSecurityConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<FormLoginConfigurer<HttpSecurity>> formLoginConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<HttpBasicConfigurer<HttpSecurity>> httpBasicConfigCustomizer;
//	@Mock
//    private DefaultCustomizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigCustomizer;
//	@Mock
//	private DefaultCustomizer<CorsConfigurer<HttpSecurity>> corsConfigCustomizer;
//	@Mock
//	private SpaCsrfTokenRequestHandler csrfTokenRequestAttributeHandler;
//	
//	@InjectMocks
//    private SecurityConfig securityConfig;
//	
//	/**
//	 * Tests that the {@link SecurityProdConfig#defaultSecurityFilterChain(HttpSecurity)}
//	 * method successfully configures the security filter chain with mocked customizers.
//	 * <p>
//	 * Verifies that the method returns a non-null {@link SecurityFilterChain}, and that
//	 * all customizers are invoked exactly once with production-specific configurations,
//	 * such as stateless sessions, CORS for localhost, CSRF protection, and role-based
//	 * authorization.
//	 * </p>
//	 * <p>
//	 * Example configuration for CORS customizer:
//	 * <pre>
//	 * config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
//	 * config.setAllowedMethods(Collections.singletonList("*"));
//	 * </pre>
//	 * <b>Warning</b>: This test assumes all customizers succeed; edge cases like null
//	 * customizers or {@link HttpSecurity} should be tested separately.
//	 * </p>
//	 * @throws Exception 
//	 */
//	@Test
//	void defaultSecurityFilterChainConfiguresSuccessfully() throws Exception {
//		// Arrange
//        HttpSecurity http = mock(HttpSecurity.class);
//        DefaultSecurityFilterChain filterChain = mock(DefaultSecurityFilterChain.class);
//
//        // Mock the build method to return a mock SecurityFilterChain
//        when(http.build()).thenReturn(filterChain);
//
//        // Mock customizer behavior with realistic configurations
//        when(corsConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                CorsConfiguration config = new CorsConfiguration();
//                config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
//                config.setAllowedMethods(Collections.singletonList("*"));
//                config.setAllowCredentials(true);
//                config.setAllowedHeaders(Collections.singletonList("*"));
//                config.setExposedHeaders(List.of("Authorization"));
//                config.setMaxAge(3600L);
//                try {
//                    when(configurer.configurationSource(any())).thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock CorsConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(sessionManagementConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                try {
//                    when(configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock SessionManagementConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(csrfConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                try {
//                    when(configurer.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler))
//                        .thenReturn(configurer);
//                    when(configurer.ignoringRequestMatchers("/auth/register", "/auth/login", "/auth/invalidSession", "/error"))
//                        .thenReturn(configurer);
//                    when(configurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                        .thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock CsrfConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(authorizeHttpRequestsConfigCustomizer.customize()).thenReturn(
//            registry -> {
//                try {
//                    when(registry.requestMatchers("/auth/register", "/auth/login", "/auth/invalidSession", "/auth/logout", "/error").permitAll())
//                        .thenReturn(registry);
//                    when(registry.requestMatchers(HttpMethod.GET, "/employees").hasAnyRole("EMPLOYEE", "ADMIN"))
//                        .thenReturn(registry);
//                    when(registry.requestMatchers(HttpMethod.POST, "/employees").hasRole("ADMIN"))
//                        .thenReturn(registry);
//                    when(registry.requestMatchers(HttpMethod.PUT, "/employees").hasRole("ADMIN"))
//                        .thenReturn(registry);
//                    when(registry.requestMatchers(HttpMethod.DELETE, "/employees").hasRole("ADMIN"))
//                        .thenReturn(registry);
//                    when(registry.requestMatchers(HttpMethod.GET, "/audit/logs").hasRole("ADMIN"))
//                        .thenReturn(registry);
//                    when(registry.anyRequest().authenticated())
//                        .thenReturn(registry);
//                } catch (Exception e) {
//                    fail("Failed to mock AuthorizeHttpRequestsConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(securityContextConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                try {
//                    when(configurer.requireExplicitSave(false)).thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock SecurityContextConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(channelSecurityConfigCustomizer.customize()).thenReturn(
//            registry -> {
//                try {
//                    when(registry.anyRequest().requiresSecure()).thenReturn(registry);
//                } catch (Exception e) {
//                    fail("Failed to mock ChannelSecurityConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(formLoginConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                try {
//                    when(configurer.loginPage("/login")).thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock FormLoginConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(httpBasicConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                try {
//                    when(configurer.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())).thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock HttpBasicConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        when(exceptionHandlingConfigCustomizer.customize()).thenReturn(
//            configurer -> {
//                try {
//                    when(configurer.accessDeniedHandler(new CustomAccessDeniedHandler())).thenReturn(configurer);
//                } catch (Exception e) {
//                    fail("Failed to mock ExceptionHandlingConfigurer: " + e.getMessage());
//                }
//            }
//        );
//
//        // Act
//        SecurityFilterChain result;
//        try {
//            result = securityConfig.defaultSecurityFilterChain(http);
//        } catch (Exception e) {
//            fail("Failed to configure security filter chain: " + e.getMessage());
//            return; // Exit early if an exception occurs
//        }
//
//        // Assert
//        assertNotNull(result, "SecurityFilterChain should not be null");
//        verify(http).build();
//        // Verify each customizer is called once
//        verify(corsConfigCustomizer, times(1)).customize();
//        verify(sessionManagementConfigCustomizer, times(1)).customize();
//        verify(csrfConfigCustomizer, times(1)).customize();
//        verify(authorizeHttpRequestsConfigCustomizer, times(1)).customize();
//        verify(securityContextConfigCustomizer, times(1)).customize();
//        verify(channelSecurityConfigCustomizer, times(1)).customize();
//        verify(formLoginConfigCustomizer, times(1)).customize();
//        verify(httpBasicConfigCustomizer, times(1)).customize();
//        verify(exceptionHandlingConfigCustomizer, times(1)).customize();
//	}
//}
