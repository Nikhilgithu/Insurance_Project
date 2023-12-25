package com.techlabs.insurance.exceptions;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class InsurancePlanNotFoundException extends RuntimeException {
	public InsurancePlanNotFoundException(String message) {
		super(message);
	}
}
