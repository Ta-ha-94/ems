//package com.codingsy.ems.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.codingsy.ems.model.User;
//import com.codingsy.ems.service.impl.AuthService;

//@RestController
//@RequestMapping("/auto")
//public class AuthController {
//	private final AuthService authService;
//	
//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}
//	
//	Allows user registration
//	@PostMapping("/register")
//	public ResponseEntity<String> registerUser(@RequestBody User user) {
//		return ResponseEntity.ok(authService.register(user));
//	}
//	 Authenticates users & returns JWT
//	@PostMapping("/login")
//	public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
//		return ResponseEntity.ok(authService.authenticate(username, password));
//	}
//	
//}
//