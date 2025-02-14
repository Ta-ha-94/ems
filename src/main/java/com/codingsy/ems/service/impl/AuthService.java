//package com.codingsy.ems.service.impl;
//
//import java.util.Optional;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.codingsy.ems.model.User;
//import com.codingsy.ems.repository.UserRepository;
//import com.codingsy.ems.util.jwt.JwtTokenGenerator;
//
//@Service
//public class AuthService {
//	private final UserRepository userRepository;
//	private final PasswordEncoder passwordEncoder;
//	private final JwtTokenGenerator jwtTokenGenerator;
//	
//	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenGenerator jwtTokenGenerator) {
//		this.userRepository = userRepository;
//		this.passwordEncoder = passwordEncoder;
//		this.jwtTokenGenerator = jwtTokenGenerator;
//	}
//	
////	Encrypts passwords before saving users.
//	public String register(User user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		userRepository.save(user);
//		return "User registered successfully!";
//	}
//	
////	Validates credentials and generates JWT on successful login.
//	public String authenticate(String username, String password) {
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
//            return jwtTokenGenerator.generateToken(username);
//        }
//        throw new RuntimeException("Invalid credentials");
//    }
//}
