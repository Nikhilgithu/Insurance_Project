package com.techlabs.insurance.dto;

import com.techlabs.insurance.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class InsurencePlanDto {
	private int planid;
	private String planname;
	private Status status;
}
