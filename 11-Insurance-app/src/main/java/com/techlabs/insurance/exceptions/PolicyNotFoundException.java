package com.techlabs.insurance.exceptions;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PolicyNotFoundException extends  RuntimeException {
	 public PolicyNotFoundException(String message) {
	        super(message);
	    }
}
