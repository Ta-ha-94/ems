package com.codingsy.ems.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codingsy.ems.model.User;
import com.codingsy.ems.repository.UserRepository;

@Service
public class CodingsyUserDetailsService implements UserDetailsService {
	
	private	final UserRepository userRepository;
	
	public CodingsyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)		
									.orElseThrow(() -> new UsernameNotFoundException("User details not found for the user: " + username));
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString())));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
}
