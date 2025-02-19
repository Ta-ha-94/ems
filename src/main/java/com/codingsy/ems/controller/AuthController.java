package com.codingsy.ems.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingsy.ems.dto.UserDTO;
import com.codingsy.ems.service.impl.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

//	Allows user registration
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
		String registerd = authService.register(userDTO);
		URI location = URI.create("");
		return ResponseEntity.created(location).body(registerd);
	}
//	 Authenticates users & returns JWT

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(Authentication authentication) {
		return ResponseEntity.ok("User logged in with username: " + authentication.getName());
	}
	
	 @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // In a stateless JWT setup, the client handles JWT removal.
        return ResponseEntity.ok("Logout successful");
    }

	@GetMapping("/invalidSession")
	public ResponseEntity<String> invalidSession() {
		return ResponseEntity.ok("Session invalidated.");
	}

}