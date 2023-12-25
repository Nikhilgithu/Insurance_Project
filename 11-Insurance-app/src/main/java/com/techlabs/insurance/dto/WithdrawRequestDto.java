package com.techlabs.insurance.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class WithdrawRequestDto {
	 private double amount;
	    private LocalDate date;
	    private int agentId;
}
