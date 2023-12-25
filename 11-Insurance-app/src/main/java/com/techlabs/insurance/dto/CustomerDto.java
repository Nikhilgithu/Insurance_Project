package com.techlabs.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerDto {
	private int customerId;
	private String firstname;
	private String lastname;
	private String email;
	private String mobileNo;
	private String state;
	private String city;
    private int numberOfPolicies;
}
