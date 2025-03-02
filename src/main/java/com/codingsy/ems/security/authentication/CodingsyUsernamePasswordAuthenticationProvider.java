package com.codingsy.ems.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link AuthenticationProvider} that handles username/password authentication
 * for the Codingsy Employee Management System (EMS). This provider is designed to authenticate
 * users based on credentials provided via a {@link UsernamePasswordAuthenticationToken}.
 * <p>
 * The authentication process involves loading user details using the provided
 * {@link UserDetailsService} and comparing the provided password with the stored encoded password
 * using the {@link PasswordEncoder}. If the credentials are invalid, a
 * {@link BadCredentialsException} is thrown.
 * </p>
 *
 * @author Taha
 * @version 1.0
 * @see AuthenticationProvider
 * @see UsernamePasswordAuthenticationToken
 * @see UserDetailsService
 * @see PasswordEncoder
 * @since 1.0
 */
@Component
@Profile("!prod")
public class CodingsyUsernamePasswordAuthenticationProvider implements AuthenticationProvider{
	private static final Logger log = LoggerFactory.getLogger(CodingsyUsernamePasswordAuthenticationProvider.class);

	private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code CodingsyUsernamePasswordAuthenticationProvider} with the required
     * dependencies.
     *
     * @param userDetailsService the service to load user details by username
     * @param passwordEncoder the encoder to match provided passwords with stored ones
     * @throws IllegalArgumentException if either dependency is null
     */
	public CodingsyUsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	/**
     * Authenticates the provided {@link Authentication} object.
     * <p>
     * If authentication succeeds, a new {@link UsernamePasswordAuthenticationToken} is
     * returned with the user's authorities. If the password is invalid or the user is
     * not found, a {@link BadCredentialsException} is thrown.
     * </p>
     *
     * @param authentication the authentication request containing username and password
     * @return a fully authenticated {@link UsernamePasswordAuthenticationToken} if successful
     * @throws AuthenticationException if authentication fails (e.g., invalid credentials)
     */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		if (authentication == null) {
			log.error("The authentcation object is null.");
	        throw new BadCredentialsException("Authentication object cannot be null");
	    }
		
	    String username = authentication.getName();
	    if (username == null || username.trim().isEmpty()) {
	    	log.error("Username is null.");
	        throw new BadCredentialsException("Username cannot be null or empty");
	    }
	    
	    String pwd = authentication.getCredentials().toString();
	    if (pwd == null || pwd.trim().isEmpty()) {
	    	log.error("Password is null or empty for user: {}", username);
	        throw new BadCredentialsException("Password cannot be null or empty");
	    }

	    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
				usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
				log.debug("UsernamePasswordAuthenticationToken set for user: {}", username);
            } else {
            	log.error("Password is invalid for user: {}", username);
                throw new BadCredentialsException("Invalid password!");
            }
		} catch (UsernameNotFoundException e) {
			log.error("Error occurred: {}", e );
			throw new BadCredentialsException("User not found: " + username, e);
		} catch (Exception e) {
			log.error("Error occurred: {}", e );
			throw new BadCredentialsException("User not found: " + username, e);
		}
        return usernamePasswordAuthenticationToken;
	}

	/**
     * Indicates whether this provider supports the given {@link Authentication} class.
     * <p>
     * This method returns {@code true} if the authentication class is assignable from
     * {@link UsernamePasswordAuthenticationToken}, indicating that this provider can
     * handle such authentication requests.
     * </p>
     *
     * @param authentication the class of the authentication object to check
     * @return {@code true} if the class is supported, {@code false} otherwise
     */
	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
