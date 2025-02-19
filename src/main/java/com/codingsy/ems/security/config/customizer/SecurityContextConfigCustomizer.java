package com.codingsy.ems.security.config.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextConfigCustomizer implements DefaultCustomizer<SecurityContextConfigurer<HttpSecurity>> {
	
	public Customizer<SecurityContextConfigurer<HttpSecurity>> customize() {
		return customizer;
	}
	
	private final Customizer<SecurityContextConfigurer<HttpSecurity>> customizer = t -> t.requireExplicitSave(false);
}
