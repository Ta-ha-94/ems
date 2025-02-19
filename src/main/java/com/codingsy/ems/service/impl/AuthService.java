package com.codingsy.ems.service.impl;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codingsy.ems.dto.UserDTO;
import com.codingsy.ems.enums.Role;
import com.codingsy.ems.exception.UsernameUnavailableException;
import com.codingsy.ems.mapper.UserMapper;
import com.codingsy.ems.model.User;
import com.codingsy.ems.repository.UserRepository;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
//	Encrypts passwords before saving users.
	public String register(UserDTO userDTO) {
//		Check if username already exists, if so, then throw the exception.
		userRepository.findByUsername(userDTO.getUsername())
						.ifPresent((u) -> new UsernameUnavailableException("User with username: " + u.getUsername() + " is not availble. Try a different username."));
		
//		Set the default role for every new user
		if(userDTO.getRoles() == null || userDTO.getRoles().isEmpty())
			userDTO.setRoles(Set.of(Role.EMPLOYEE));
		
//		Map the UserDTO to User model
		User user = UserMapper.toUserModel(userDTO);
		
//		Hash the plain-text password 
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userRepository.save(user);
		return "User with username:" + user.getUsername() + " is registered successfully!";
	}

//	Validates credentials and generates JWT on successful login.
//	public String authenticate(String username, String password) {
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
//            return jwtTokenGenerator.generateToken(username);
//        }
//        throw new RuntimeException("Invalid credentials");
//    }
}