package com.techlabs.insurance.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class InsuranceSchemeNotFound extends RuntimeException{
	private String message;
}
