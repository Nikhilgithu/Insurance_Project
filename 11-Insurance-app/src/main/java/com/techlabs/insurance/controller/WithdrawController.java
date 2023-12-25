package com.techlabs.insurance.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.WithdrawDto;
import com.techlabs.insurance.service.WithdrawService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class WithdrawController {

	
	@Autowired
	 private  WithdrawService withdrawService;

	@PreAuthorize("hasRole('AGENT')")
	@PostMapping("/withdrawcommision")
	public ResponseEntity<String> withdrawAmount(@RequestBody WithdrawDto requestDto) {
	    double amount = requestDto.getAmount();
	    LocalDate date = requestDto.getDate();
	    int agentId = requestDto.getAgentId();
        withdrawService.withdrawAmount(amount, date, agentId);
        return ResponseEntity.ok("Amount withdrawn successfully.");
	}
	
	@GetMapping("/withdrawals/{agentId}")
	public ResponseEntity<Page<WithdrawDto>> getWithdrawalsByAgentIdAndDate(
	        @PathVariable int agentId,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Page<WithdrawDto> withdrawalDtoPage = withdrawService.getWithdrawalsByAgentIdAndDate(agentId, startDate, endDate, page, size);

	    return ResponseEntity.ok(withdrawalDtoPage);
	}





}
