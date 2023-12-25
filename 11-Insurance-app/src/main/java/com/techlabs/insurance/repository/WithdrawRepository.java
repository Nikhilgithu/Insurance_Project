package com.techlabs.insurance.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Withdraw;


public interface WithdrawRepository extends JpaRepository<Withdraw, Integer>{
//	Page<Withdraw> findByAgent_AgentId(int agentId, Pageable pageable);
	Page<Withdraw> findByAgent(Agent agent, Pageable pageable);
	 Page<Withdraw> findByAgentAndDateBetween(Agent agent, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
