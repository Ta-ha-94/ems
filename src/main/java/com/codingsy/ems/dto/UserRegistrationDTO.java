package com.codingsy.ems.dto;

import java.util.Set;

import com.codingsy.ems.enums.Role;

public class UserRegistrationDTO {
	private String username;
    private String password;
    private Set<Role> roles;
    
 // Default constructor
    public UserRegistrationDTO() {
    }

    // Parameterized constructor
    public UserRegistrationDTO(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
