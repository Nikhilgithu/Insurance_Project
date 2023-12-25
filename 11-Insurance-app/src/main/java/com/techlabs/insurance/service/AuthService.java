package com.techlabs.insurance.service;

import com.techlabs.insurance.dto.LoginDto;
import com.techlabs.insurance.dto.RegisterDto;

public interface AuthService {
	String login(LoginDto loginDto);
	String adminRegister(RegisterDto registerDto);
	String employeeRegister(RegisterDto registerDto);
	String agentRegister(RegisterDto registerDto);
	String customerRegister(RegisterDto registerDto);
	String getRoleFromToken(String token);
	String getUsernameFromToken(String token);
}
