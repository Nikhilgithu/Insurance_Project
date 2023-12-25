package com.techlabs.insurance.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class InsurancePolicyDto {
		private int policyid;
	  	private LocalDate issueDate;
	    private LocalDate maturityDate;
	    private String premiumType;
	    private int totalInstallment;
	    private Double investAmount;
	    private Double premiumAmount;
	    private Double sumAssured;
	    private Status status;
	    private CustomerDto customer;
	    private InsurenceSchemeDto insuranceScheme;
	    private List<NomineeDto> nominees;
	    private int agent;
	    private List<MultipartFile> documentFiles; 
	    
}
