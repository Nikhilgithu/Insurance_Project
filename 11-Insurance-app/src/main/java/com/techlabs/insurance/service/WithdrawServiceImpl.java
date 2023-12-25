package com.techlabs.insurance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.WithdrawDto;
import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Withdraw;
import com.techlabs.insurance.exceptions.AgentNotFoundException;
import com.techlabs.insurance.exceptions.InsufficientBalanceException;
import com.techlabs.insurance.repository.AgentRepository;
import com.techlabs.insurance.repository.WithdrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {

	private final Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

	@Autowired
	private WithdrawRepository withdrawRepository;

	@Autowired
	private AgentRepository agentRepository;

	@Override
	public String withdrawAmount(double amount, LocalDate date, int agentId) {
	    Agent agent = agentRepository.findById(agentId)
	            .orElseThrow(() -> new AgentNotFoundException("Agent with ID " + agentId + " not found"));

	    double commissionEarn = agent.getCommissionEarn();

	    if (amount <= commissionEarn) {
	        Withdraw withdrawal = new Withdraw();
	        withdrawal.setAgent(agent);
	        withdrawal.setAmount(amount);
	        withdrawal.setDate(date);
	        List<Withdraw> withdraws = agent.getWithdraws();
	        withdraws.add(withdrawal);
	        agent.setWithdraws(withdraws);
	        agent.setCommissionEarn(commissionEarn - amount);
	        agentRepository.save(agent);
	        logger.info("withdrawAmount:Withdrawal of {} by agent ID {} successful", amount, agentId);
	        return "Withdrawal successful";
	    } else {
	        throw new InsufficientBalanceException("Insufficient balance for withdrawal");
	    }
	}
	
	

	@Override
	public Page<WithdrawDto> getWithdrawalsByAgentIdAndDate(int agentId, LocalDate startDate, LocalDate endDate, int pageNumber, int pageSize) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("date").descending());
	    Agent agent = agentRepository.findById(agentId)
	            .orElseThrow(() -> new AgentNotFoundException("Agent with ID " + agentId + " not found"));
	    Page<Withdraw> withdrawalsPage = withdrawRepository.findByAgentAndDateBetween(agent, startDate, endDate, pageable);

	    List<WithdrawDto> withdrawalDtos = new ArrayList<>();
	    for (Withdraw withdrawal : withdrawalsPage.getContent()) {
	        WithdrawDto dto = new WithdrawDto();
	        dto.setAmount(withdrawal.getAmount());
	        dto.setDate(withdrawal.getDate());
	        withdrawalDtos.add(dto);
	    }

	    logger.info("getWithdrawalsByAgentIdAndDateRange: Fetched {} withdrawals for agent ID {} within date range {} to {}",
	            withdrawalsPage.getTotalElements(), agentId, startDate, endDate);

	    return new PageImpl<>(withdrawalDtos, pageable, withdrawalsPage.getTotalElements());
	}



	

}
