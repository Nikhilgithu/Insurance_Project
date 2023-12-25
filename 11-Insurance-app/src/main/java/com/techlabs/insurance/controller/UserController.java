package com.techlabs.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.UserDto;
import com.techlabs.insurance.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE') || hasRole('CUSTOMER') || hasRole('AGENT')")
	@GetMapping("/detail/{username}")
	public ResponseEntity<?> getUser(@PathVariable String username){
		UserDto userDto = userService.getUserDto(username);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
}
