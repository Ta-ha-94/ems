package com.codingsy.ems.mapper;

import org.springframework.stereotype.Component;

import com.codingsy.ems.dto.UserDTO;
import com.codingsy.ems.model.User;

@Component
public class UserMapper {
	
	public static User toUserModel(UserDTO userDTO) {
		return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getRoles());
	}
	
	public static UserDTO toUserDTO(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
	}
}
