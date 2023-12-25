package com.techlabs.insurance.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.techlabs.insurance.dto.AgentDto;
import com.techlabs.insurance.dto.InsurancePolicyDto;
import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

public interface AgentService {
	
	List<AgentDto> getAllAgents();

	List<AgentDto> getAllPendingAgents();

	List<AgentDto> getAllActiveAgents();

	List<AgentDto> getAllInActiveAgents();

	void updateAgentStatus(int agentId, Status newStatus);
	
	Agent getAgentById(int agentId);
	
	Page<AgentDto> getAllPendingAgentsPageWise(int pageNumber, int pageSize);

	Page<AgentDto> getAllActiveAgentsPageWise(int pageNumber, int pageSize);

	Page<AgentDto> getAllInActiveAgentsPageWise(int pageNumber, int pageSize);
	
	public AgentDto getAgentByid(int agentId);
	
	Page<InsurancePolicyDto> getInsurancePolicy(int agentId, int pageno, int pagesize);
	
	void addCommission(int agentId, double commissionAmount);

}
