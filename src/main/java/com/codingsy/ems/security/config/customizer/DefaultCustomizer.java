package com.codingsy.ems.security.config.customizer;

import org.springframework.stereotype.Component;

@Component
public interface DefaultCustomizer<T> {
	org.springframework.security.config.Customizer<T> customize();
}
