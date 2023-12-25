package com.techlabs.insurance.exceptions;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BalanceInsufficientException extends Exception {
	public BalanceInsufficientException(String message) {
		super(message);
	}
}
