package com.techlabs.insurance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.ClaimDto;
import com.techlabs.insurance.entities.Claim;
import com.techlabs.insurance.entities.ClaimStatus;

public interface ClaimService {
	void addClaim(ClaimDto claimdto, List<MultipartFile> documentFiles);
	public Page<ClaimDto> getClaimsByPolicyNumber(int policyNumber, int pageNumber, int pageSize);
//	Page<ClaimDto> getClaimsByPolicyNumberAndStatus(int policyNumber, ClaimStatus status, int pageNumber, int pageSize);
	public Page<ClaimDto> getClaimsByStatus(ClaimStatus status, int pageNumber, int pageSize);
	void updateClaimStatus(int claimId, ClaimStatus newStatus);
	List<Claim> getClaimsBetweenDates(LocalDate startDate, LocalDate endDate);
	Page<ClaimDto> getClaimsByPolicyNumberAndDateRange(int policyNumber, LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize);
}
