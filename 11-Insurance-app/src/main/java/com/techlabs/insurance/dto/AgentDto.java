package com.techlabs.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AgentDto {

	private int agentId;
	private String firstname;
	private String lastname;
	private String qualification;
	private Double commissionEarn;
}
