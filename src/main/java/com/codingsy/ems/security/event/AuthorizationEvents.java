package com.codingsy.ems.security.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEvents {
	@EventListener
	public void onFailure(AuthorizationDeniedEvent<Object> deniedEvent) { // Parameterized type and getAuthorizationResult()
        System.out.println("Authorization failed for the user : " + deniedEvent.getAuthentication().get().getName() + " due to : " +
                deniedEvent.getAuthorizationResult().toString()); // Use getAuthorizationResult()
    }
}
