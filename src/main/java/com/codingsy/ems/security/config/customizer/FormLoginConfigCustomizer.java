package com.codingsy.ems.security.config.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.stereotype.Component;

@Component
public class FormLoginConfigCustomizer implements DefaultCustomizer<FormLoginConfigurer<HttpSecurity>> {
	
	public Customizer<FormLoginConfigurer<HttpSecurity>> customize() {
		return customizer;
	}
	
	private final Customizer<FormLoginConfigurer<HttpSecurity>> customizer = t -> {
		Customizer.withDefaults();
	};
}
