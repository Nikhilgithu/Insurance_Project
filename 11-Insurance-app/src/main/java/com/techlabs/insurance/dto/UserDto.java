package com.techlabs.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
	private int id;
	private String firstname;
	private String lastname;
	private int age;
	private int userid;
	private String username;
	private String password;
	private int roleid;
	private String role;
}
