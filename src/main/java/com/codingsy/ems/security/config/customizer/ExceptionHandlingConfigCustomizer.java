package com.codingsy.ems.security.config.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.stereotype.Component;

import com.codingsy.ems.exception.CustomAccessDeniedHandler;

@Component
public class ExceptionHandlingConfigCustomizer implements DefaultCustomizer<ExceptionHandlingConfigurer<HttpSecurity>> {
	
	public Customizer<ExceptionHandlingConfigurer<HttpSecurity>> customize() {
		return customizer;
	}
	
	private final Customizer<ExceptionHandlingConfigurer<HttpSecurity>> customizer = t -> t.accessDeniedHandler(new CustomAccessDeniedHandler());
}
