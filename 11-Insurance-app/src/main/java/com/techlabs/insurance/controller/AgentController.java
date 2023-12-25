package com.techlabs.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.AgentDto;
import com.techlabs.insurance.dto.CommissionDto;
import com.techlabs.insurance.dto.InsurancePolicyDto;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.service.AgentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class AgentController {

	@Autowired
	private AgentService agentService;

	@GetMapping("/getallagents")
	List<AgentDto> getAllAgents() {
		return agentService.getAllAgents();
	}

	@GetMapping("/getallactiveagents")
	public ResponseEntity<List<AgentDto>> getAllActiveAgents() {
		List<AgentDto> activeAgents = agentService.getAllActiveAgents();
		return ResponseEntity.ok(activeAgents);
	}


	@PreAuthorize("hasRole('EMPLOYEE')")
	@PutMapping("/updateagentstatus/{id}")
	public ResponseEntity<String> updateAgentStatus(@PathVariable int id, @RequestParam("status") Status newStatus) {
		agentService.updateAgentStatus(id, newStatus);
		return ResponseEntity.ok("Agent status updated successfully.");
	}
	
	@PreAuthorize("hasRole('EMPLOYEE')")
	@GetMapping("/pendingagents")
	 public ResponseEntity<Page<AgentDto>> getAllPendingAgentsPageWise(
	            @RequestParam(defaultValue = "0") int pageno,
	            @RequestParam(defaultValue = "10") int pagesize) {
	        Page<AgentDto> pendingAgents = agentService.getAllPendingAgentsPageWise(pageno, pagesize);
	        return ResponseEntity.ok(pendingAgents);
	    }

	@PreAuthorize("hasRole('EMPLOYEE')")
	@GetMapping("/activeagents")
	public ResponseEntity<Page<AgentDto>> getAllActiveAgentsPageWise(
           @RequestParam(defaultValue = "0") int pageno,
           @RequestParam(defaultValue = "10") int pagesize) {
       Page<AgentDto> activeAgents = agentService.getAllActiveAgentsPageWise(pageno, pagesize);
       return ResponseEntity.ok(activeAgents);
   }
	
	@PreAuthorize("hasRole('EMPLOYEE')")
	@GetMapping("/inactiveagents")
	 public ResponseEntity<Page<AgentDto>> getAllInActiveAgentsPageWise(
	            @RequestParam(defaultValue = "0") int pageno,
	            @RequestParam(defaultValue = "10") int pagesize) {
	        Page<AgentDto> inactiveAgents = agentService.getAllInActiveAgentsPageWise(pageno, pagesize);
	        return ResponseEntity.ok(inactiveAgents);
	    }
	
	
	@GetMapping("getagentbyid/{agentId}")
    public ResponseEntity<AgentDto> getAgentById(@PathVariable int agentId) {
        AgentDto agentDto = agentService.getAgentByid(agentId);
        return ResponseEntity.ok(agentDto);
    }
	
	@PreAuthorize("hasRole('AGENT')")
	@GetMapping("getpolicies/{agentId}")
	public ResponseEntity<?> getInsurancePolicyByAgent(@PathVariable int agentId, 
			@RequestParam int pageno, @RequestParam int pagesize){
		Page<InsurancePolicyDto> response = agentService.getInsurancePolicy(agentId, pageno, pagesize);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	 @PostMapping("commission/{agentId}")
    public ResponseEntity<String> addCommission(@PathVariable int agentId,@RequestBody CommissionDto commissionDTO) {
        double commissionAmount = commissionDTO.getAmount();
        agentService.addCommission(agentId, commissionAmount);
        return ResponseEntity.ok("Commission added successfully");
    }
}
