package com.techlabs.insurance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.AgentDto;
import com.techlabs.insurance.dto.CustomerDto;
import com.techlabs.insurance.dto.InsurancePolicyDto;
import com.techlabs.insurance.entities.Agent;
import com.techlabs.insurance.entities.Customer;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.exceptions.AccountNotFoundException;
import com.techlabs.insurance.exceptions.IllegalArgumentExceptionCustom;
import com.techlabs.insurance.repository.AgentRepository;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepository;
	
	@Autowired
	private InsurencePolicyRepository insurencePolicyRepository;

	private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

	 @Override
	    public List<AgentDto> getAllAgents() {
	        List<Agent> agents = agentRepository.findAll();
	        List<AgentDto> agentDtos = new ArrayList<>();
	        for (Agent agent : agents) {
	            agentDtos.add(convertAgentToDto(agent));
	        }
	        logger.info("getAllAgents :Retrieved all agents successfully.");
	        return agentDtos;
	    }

	    @Override
	    public List<AgentDto> getAllPendingAgents() {
	        List<Agent> pendingAgents = agentRepository.findByStatus(Status.PENDING);
	        logger.info("getAllPendingAgents: Retrieved all pending agents successfully.");
	        return convertAgentListToDtoList(pendingAgents);
	    }

	    @Override
	    public List<AgentDto> getAllActiveAgents() {
	        List<Agent> activeAgents = agentRepository.findByStatus(Status.ACTIVE);
	        logger.info("getAllActiveAgents:Retrieved all active agents successfully.");
	        return convertAgentListToDtoList(activeAgents);
	    }

	    @Override
	    public List<AgentDto> getAllInActiveAgents() {
	        List<Agent> inactiveAgents = agentRepository.findByStatus(Status.INACTIVE);
	        logger.info("getAllInActiveAgents: Retrieved all inactive agents successfully.");
	        return convertAgentListToDtoList(inactiveAgents);
	    }

	    @Override
	    public void updateAgentStatus(int agentId, Status newStatus) {
	        Agent agent = agentRepository.findById(agentId)
	                .orElseThrow(() -> new IllegalArgumentExceptionCustom("Agent not found with ID: " + agentId));
	        agent.setStatus(newStatus);
	        agentRepository.save(agent);
	        logger.info("updateAgentStatus:Updated agent status with ID: {} to {} successfully.", agentId, newStatus);
	    }

	private AgentDto convertAgentToDto(Agent agent) {
		return new AgentDto(agent.getAgentId(), agent.getFirstname(), agent.getLastname(), agent.getQualification(),
				agent.getCommissionEarn());
	}

	private List<AgentDto> convertAgentListToDtoList(List<Agent> agents) {
		List<AgentDto> agentDtos = new ArrayList<>();
		for (Agent agent : agents) {
			agentDtos.add(convertAgentToDto(agent));
		}
		return agentDtos;
	}
	
	@Override
	public Agent getAgentById(int agentId) {
		 Optional<Agent> agentOptional = agentRepository.findById(agentId);
	        return agentOptional.orElse(null);
	}
	
	  @Override
	    public Page<AgentDto> getAllPendingAgentsPageWise(int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        Page<Agent> pendingAgentsPage = agentRepository.findByStatus(Status.PENDING, pageable);
	        Page<AgentDto> agentDtosPage = pendingAgentsPage.map(this::convertAgentToDto);
	        logger.info("getAllPendingAgentsPageWise: Retrieved all pending agents page-wise successfully.");
	        return agentDtosPage;
	    }

	    @Override
	    public Page<AgentDto> getAllActiveAgentsPageWise(int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        Page<Agent> activeAgentsPage = agentRepository.findByStatus(Status.ACTIVE, pageable);
	        Page<AgentDto> agentDtosPage = activeAgentsPage.map(this::convertAgentToDto);
	        logger.info(" getAllActiveAgentsPageWise: Retrieved all active agents page-wise successfully.");
	        return agentDtosPage;
	    }

	    @Override
	    public Page<AgentDto> getAllInActiveAgentsPageWise(int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        Page<Agent> inactiveAgentsPage = agentRepository.findByStatus(Status.INACTIVE, pageable);
	        Page<AgentDto> agentDtosPage = inactiveAgentsPage.map(this::convertAgentToDto);
	        logger.info("getAllInActiveAgentsPageWise :Retrieved all inactive agents page-wise successfully.");
	        return agentDtosPage;
	    }

	    @Override
	    public AgentDto getAgentByid(int agentId) {
	        Optional<Agent> agentOptional = agentRepository.findById(agentId);
	        if (agentOptional.isPresent()) {
	            Agent agent = agentOptional.get();
	            logger.info(" getAgentByid: Retrieved agent with ID: {} successfully.", agentId);
	            return convertAgentToDto(agent);
	        } else {
	            throw new AccountNotFoundException("Agent not found with ID: " + agentId);
	        }
	    }

	    @Override
	    public Page<InsurancePolicyDto> getInsurancePolicy(int agentId, int pageno, int pagesize) {
	        Agent agent = agentRepository.findById(agentId).orElse(null);
	        if (agent == null) throw new AccountNotFoundException("Agent with agentid: " + agentId + " not found");
	        Pageable pageable = PageRequest.of(pageno, pagesize);
	        Page<InsurancePolicy> policies = insurencePolicyRepository.findByAgent(agent, pageable);
	        Page<InsurancePolicyDto> policyDtosPage = policies.map(policy -> {
	            InsurancePolicyDto insurancePolicyDto = new InsurancePolicyDto();
	            insurancePolicyDto.setPolicyid(policy.getPolicyNo());
	            insurancePolicyDto.setInvestAmount(policy.getInvestamount());
	            insurancePolicyDto.setStatus(policy.getStatus());
	            insurancePolicyDto.setPremiumAmount(policy.getPremiumamount());
	            insurancePolicyDto.setIssueDate(policy.getIssueDate());
	            insurancePolicyDto.setMaturityDate(policy.getMaturityDate());
	            Customer customer = policy.getCustomer();
	            CustomerDto customerDto = new CustomerDto();
	            customerDto.setCustomerId(customer.getCustomerId());
	            customerDto.setFirstname(customer.getFirstname());
	            customerDto.setLastname(customer.getLastname());
	            insurancePolicyDto.setCustomer(customerDto);
	            return insurancePolicyDto;
	        });
	        logger.info("getInsurancePolicy: Retrieved insurance policies for agent with ID: {} successfully.", agentId);
	        return policyDtosPage;
	    }

	    @Override
	    public void addCommission(int agentId, double commissionAmount) {
	        Agent agent = agentRepository.findById(agentId)
	                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));

	        double currentCommission = agent.getCommissionEarn();
	        double updatedCommission = currentCommission + commissionAmount;
	        agent.setCommissionEarn(updatedCommission);

	        agentRepository.save(agent);
	        logger.info("addCommission: Added commission amount {} to agent with ID: {} successfully.", commissionAmount, agentId);
	    }

}
