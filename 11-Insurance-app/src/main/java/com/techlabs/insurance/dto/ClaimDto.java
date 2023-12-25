package com.techlabs.insurance.dto;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.entities.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ClaimDto {
	
	 private int claimId;
	 private int policyNumber;
	 private String bankAccountNumber;
	 private String bankIfscCode;
	 private LocalDate date;
	 private List<MultipartFile> documentFiles; 
	 private ClaimStatus status;
}
