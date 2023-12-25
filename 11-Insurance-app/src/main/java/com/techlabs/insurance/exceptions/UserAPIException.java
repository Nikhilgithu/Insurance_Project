package com.techlabs.insurance.exceptions;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserAPIException extends RuntimeException{
	private String message;
	
	public UserAPIException(String message) {
		super();
		this.message = message;
	}
}
