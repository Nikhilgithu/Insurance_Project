package com.techlabs.insurance.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.WithdrawDto;

public interface WithdrawService {

	String withdrawAmount(double amount, LocalDate date, int agentId);

	

	Page<WithdrawDto> getWithdrawalsByAgentIdAndDate(int agentId, LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize);

}
