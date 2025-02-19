package com.codingsy.ems.security.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {
	
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
		System.out.println("Login successful for user: " + authenticationSuccessEvent.getAuthentication().getName() + " at " + authenticationSuccessEvent.getTimestamp());
	}
	
	@EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
		System.out.println("Login failed for the user : " + failureEvent.getAuthentication().getName() + " due to : {}" + failureEvent.getException().getMessage() + "at " + failureEvent.getTimestamp());
    }
}
