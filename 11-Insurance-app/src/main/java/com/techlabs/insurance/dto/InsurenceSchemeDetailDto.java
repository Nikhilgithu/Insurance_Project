package com.techlabs.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class InsurenceSchemeDetailDto {
	private double registrationCommission;
	private double installmentCommission;
}
