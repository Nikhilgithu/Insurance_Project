package com.techlabs.insurance.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtAuthResponse {
	private String accesstoken;
	private String tokenType = "Bearer";
	private String username;
	private String role;
}
