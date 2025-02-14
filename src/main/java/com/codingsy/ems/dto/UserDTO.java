package com.codingsy.ems.dto;

import java.util.Set;

import com.codingsy.ems.enums.Role;

public class UserDTO {
	private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
    
    // Default constructor
    public UserDTO() {
    }

    // Parameterized constructor
    public UserDTO(Long id, String username, String password, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
    
 // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    // toString method
    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", username=" + username + ", roles=" + roles + "]";
    }
}