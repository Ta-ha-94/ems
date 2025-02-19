package com.codingsy.ems.security.config.customizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ChannelSecurityConfigCustomizer implements DefaultCustomizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> {
	
	@Value("${security.channel.requires-https:false}")
	private boolean requiresHttps;
	
	public Customizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> customize() {
		return customizer;
	}
	
	private final Customizer<ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry> customizer = t -> {
		if(requiresHttps)	
			t.anyRequest().requiresSecure();
		else
			t.anyRequest().requiresInsecure();
	};
}
