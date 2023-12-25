package com.techlabs.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.LoginDto;
import com.techlabs.insurance.dto.RegisterDto;
import com.techlabs.insurance.payload.JwtAuthResponse;
import com.techlabs.insurance.service.AuthService;
import com.techlabs.insurance.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = {"/login", "/signin"})
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
		String token = authService.login(loginDto);
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setAccesstoken(token);
		jwtAuthResponse.setUsername(loginDto.getUsername());
		jwtAuthResponse.setRole(userService.getUser(loginDto.getUsername()).getRole().getRoleName());
		return ResponseEntity.ok(jwtAuthResponse);
	}
	
	@PostMapping("/adminregister")
	public ResponseEntity<String> adminRegister(@RequestBody RegisterDto registerDto){
		String response = authService.adminRegister(registerDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/employeeregister")
	public ResponseEntity<String> employeeRegister(@RequestBody RegisterDto registerDto){
		String response = authService.employeeRegister(registerDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	
	@PostMapping("/agentregister")
	public ResponseEntity<String> agentRegister(@RequestBody RegisterDto registerDto){
		String response = authService.agentRegister(registerDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@PostMapping("/customerregister")
	public ResponseEntity<String> customerRegister(@RequestBody RegisterDto registerDto){
		String response = authService.customerRegister(registerDto);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	
	@PostMapping("/getrole")
	public ResponseEntity<?> getRole(@RequestParam(name = "token") String token){
		String role = authService.getRoleFromToken(token);
		return new ResponseEntity<String>(role, HttpStatus.OK);
	}
	
	@PostMapping("/getusername")
	public ResponseEntity<?> getUsername(@RequestParam(name = "token") String token){
		String username = authService.getUsernameFromToken(token);
		return new ResponseEntity<String>(username, HttpStatus.OK);
	}
}
