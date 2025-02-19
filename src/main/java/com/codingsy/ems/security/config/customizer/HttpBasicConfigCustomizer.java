package com.codingsy.ems.security.config.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.stereotype.Component;

import com.codingsy.ems.exception.CustomBasicAuthenticationEntryPoint;

@Component
public class HttpBasicConfigCustomizer implements DefaultCustomizer<HttpBasicConfigurer<HttpSecurity>> {
	
	public Customizer<HttpBasicConfigurer<HttpSecurity>> customize() {
		return customizer;
	}
	
	private final Customizer<HttpBasicConfigurer<HttpSecurity>> customizer = t -> t.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint());
}
