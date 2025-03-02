package com.codingsy.ems.security;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.codingsy.ems.constants.ApplicationConstants.ROLE_PREFIX;
import com.codingsy.ems.model.User;
import com.codingsy.ems.repository.UserRepository;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService} to load user-specific data.
 * This service retrieves user details from the database and constructs a {@link UserDetails} object
 * for authentication and authorization purposes.
 *
 * <p>It uses the {@link UserRepository} to fetch user details and maps the user's roles to
 * Spring Security's {@link GrantedAuthority} for role-based access control.</p>
 *
 * @author Taha
 * @version 1.0
 * @see UserDetailsService
 * @see UserRepository
 * @see UserDetails
 */
@Service
public class CodingsyUserDetailsService implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(CodingsyUserDetailsService.class);
	private	final UserRepository userRepository;
	
	/**
	 * Constructs a new {@code CodingsyUserDetailsService} with the specified {@link UserRepository}.
	 *
	 * @param userRepository the repository used to fetch user details from the database.
	 */
	public CodingsyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
     * Loads the user details by username for authentication and authorization.
     *
     * <p>This method retrieves the user from the database using the provided username.
     * If the user is not found, it throws a {@link UsernameNotFoundException}.</p>
     *
     * <p>The user's roles are mapped to Spring Security's {@link GrantedAuthority} objects
     * and included in the returned {@link UserDetails} object.</p>
     *
     * @param username the username of the user to be loaded.
     * @return a {@link UserDetails} object containing the user's details and authorities.
     * @throws UsernameNotFoundException if the user with the specified username is not found.
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if (username == null || username.trim().isEmpty()) {
			log.error("username is null");
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }
		
		log.debug("Loading user by username: {}", username);
		User user = userRepository.findByUsername(username)		
									.orElseThrow(() -> {
										log.error("User not found: {}", username);
										return new UsernameNotFoundException("User details not found for the user: " + username);
									});
		List<GrantedAuthority> authorities = user.getRoles()
													.stream()
													.map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toString()))
													.collect(Collectors.toList());

		log.debug("User '{}' loaded successfully with roles: {}", username, authorities);
		
		return new CodingsyUser(user.getUsername(), user.getPassword(), authorities);
	}
}