package com.codingsy.ems.security.config.customizer;

import java.util.Collections;
import java.util.List;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
public class CorsConfigCustomizer implements DefaultCustomizer<CorsConfigurer<HttpSecurity>> {
	
	public Customizer<CorsConfigurer<HttpSecurity>> customize(){
		return customizer;
	}
	
	private final Customizer<CorsConfigurer<HttpSecurity>> customizer = t -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
		    config.setAllowedMethods(Collections.singletonList("*"));
		    config.setAllowCredentials(true);
		    config.setAllowedHeaders(Collections.singletonList("*"));
		    config.setExposedHeaders(List.of("Authorization"));
		    config.setMaxAge(3600L);
	};
}