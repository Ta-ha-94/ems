package com.codingsy.ems.security.authentication;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This implementation class will not authenticate password
 * This is suitable for other than prod environment
 */
@Component
@Profile("!prod")
public class CodingsyUsernamePasswordAuthenticationProdvider implements AuthenticationProvider{
	
	private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

	public CodingsyUsernamePasswordAuthenticationProdvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

//	Since this is not prod environment, we are not matching passwords
//	passwordEncoder.matches(pwd, userDetails.getPassword())
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if(passwordEncoder.matches(pwd, userDetails.getPassword()))
        	return new UsernamePasswordAuthenticationToken(username,pwd,userDetails.getAuthorities());
        else
        	throw new BadCredentialsException("Invalid password!");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
