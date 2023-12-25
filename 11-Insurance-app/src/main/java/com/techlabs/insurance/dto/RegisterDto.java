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
public class RegisterDto {
	private String firstname;
	private String lastname;
	private int age;
	private double salary;
	private String qualification;
    private String email;
    private String mobileNo;
    private String state;
    private String city;
	private String username;
	private String password;
	private int roleid;
}
