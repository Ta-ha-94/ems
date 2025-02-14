package com.codingsy.ems.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
public class SecurityConfig {
//	Defines custom security rules to be applied to the incoming requests. 
//	Specifies which type of authentication should be performed, http-based or form-based.
//	Specifies custom login/logout forms.
	@Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll() // ✅ Public access
                .anyRequest().authenticated() // Protect other APIs
            )
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
	
//	No longer required as we have our own implementation of the UserDetailsService
//	@Bean
//	public UserDetailsService userDetailsService() {
//		//UserDetails user = User.builder().username("user").password("{noop}pass1234").authorities("read").build();
//		UserDetails user1 = new User("user", "{noop}pass12345", List.of(new SimpleGrantedAuthority("read")));
//		UserDetails admin = new User("admin", "{bcrypt}$2a$12$0RjudVausL/Sl4DSn8BmH.nAK7K15cmaxSsPytHU5l8eGlAXvINGm", List.of(new SimpleGrantedAuthority("admin")));
//		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//		inMemoryUserDetailsManager.createUser(user1);
//		inMemoryUserDetailsManager.createUser(admin);
//		
//		UserDetails user = User.builder().username("admin1").password(passwordEncoder().encode("Codingsy@12345")).authorities("ADMIN").build();
//		inMemoryUserDetailsManager.createUser(user);
//		
//		UserDetails user2 = User.withUsername("user2").password(passwordEncoder().encode("Codingsy@12345")).authorities("ADMIN").build();
//		inMemoryUserDetailsManager.createUser(user2);
//		
//		UserDetails user3 = User.withUserDetails(new User("user3", "{bcrypt}$2a$12$0RjudVausL/Sl4DSn8BmH.nAK7K15cmaxSsPytHU5l8eGlAXvINGm", List.of(new SimpleGrantedAuthority("ADMIN")))).build();
//		inMemoryUserDetailsManager.createUser(user3);
//		
//		return inMemoryUserDetailsManager;
//	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
}
