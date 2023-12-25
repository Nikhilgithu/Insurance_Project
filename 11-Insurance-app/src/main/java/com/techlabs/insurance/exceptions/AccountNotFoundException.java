package com.techlabs.insurance.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountNotFoundException extends RuntimeException{
	private String message;
}
