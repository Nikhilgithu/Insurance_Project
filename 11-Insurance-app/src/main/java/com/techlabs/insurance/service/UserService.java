package com.techlabs.insurance.service;

import com.techlabs.insurance.dto.UserDto;
import com.techlabs.insurance.entities.User;

public interface UserService {
	public User getUser(String username);
	public UserDto getUserDto(String username);
}
