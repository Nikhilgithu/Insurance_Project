package com.techlabs.insurance.service;

import java.io.IOException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.ClaimDto;
import com.techlabs.insurance.entities.Claim;
import com.techlabs.insurance.entities.ClaimStatus;
import com.techlabs.insurance.entities.Document;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.exceptions.ClaimNotFoundException;
import com.techlabs.insurance.exceptions.PolicyNotFoundException;
import com.techlabs.insurance.repository.ClaimRepository;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

@Service
public class ClaimServiceImpl implements ClaimService {

	private final Logger logger = LoggerFactory.getLogger(ClaimServiceImpl.class);

	@Autowired
	private ClaimRepository claimRepository;

	@Autowired
	private InsurencePolicyRepository policyRepository;


	 @Override
	    @Transactional
	    public void addClaim(ClaimDto claimdto, List<MultipartFile> documentFiles) {
	        InsurancePolicy policy = policyRepository.findBypolicyNo(claimdto.getPolicyNumber());

	        if (policy != null) {
	            Claim claim = new Claim();
	            claim.setBankAccountNumber(claimdto.getBankAccountNumber());
	            claim.setBankIfscCode(claimdto.getBankIfscCode());
	            claim.setDate(claimdto.getDate());
	            claim.setStatus(ClaimStatus.APPLIED);
	            claim.setInsurancePolicy(policy);

	            Claim savedClaim = claimRepository.save(claim);

	            List<Document> documents = new ArrayList<>();
	            for (MultipartFile documentFile : claimdto.getDocumentFiles()) {
	                if ("application/pdf".equals(documentFile.getContentType())) {
	                    Document document = new Document();
	                    document.setDocumentType("Claim Document");
	                    document.setDocumentName(documentFile.getOriginalFilename());
	                    try {
	                        document.setDocumentFile(documentFile.getBytes());
	                        document.setClaim(claim);
	                        documents.add(document);
	                    } catch (IOException e) {
	                        logger.error("Error while processing document: {}", e.getMessage());
	                    }
	                } else {
	                    logger.error("Uploaded document is not a PDF");
	                }
	            }

	            savedClaim.setDocuments(documents);

	            logger.info("addClaim: Claim added successfully for policy number: {}", claimdto.getPolicyNumber());
	        } else {
	            throw new PolicyNotFoundException("Policy with policy number " + claimdto.getPolicyNumber() + " not found");
	        }
	    }

	    @Override
	    public Page<ClaimDto> getClaimsByPolicyNumber(int policyNumber, int pageNumber, int pageSize) {
	        InsurancePolicy insurancePolicy = policyRepository.findBypolicyNo(policyNumber);
	        if (insurancePolicy == null) {
	            throw new PolicyNotFoundException("Insurance policy with policy number " + policyNumber + " not found");
	        }

	        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("claimId").descending());

	        List<Claim> claims = claimRepository.findByInsurancePolicy_PolicyNo(policyNumber, pageable);
	        Page<Claim> claimPage = new PageImpl<>(claims, pageable, claims.size());

	        List<ClaimDto> claimDTOs = new ArrayList<>();

	        for (Claim claim : claimPage.getContent()) {
	            ClaimDto claimDTO = new ClaimDto();
	            claimDTO.setPolicyNumber(policyNumber);
	            claimDTO.setBankAccountNumber(claim.getBankAccountNumber());
	            claimDTO.setBankIfscCode(claim.getBankIfscCode());
	            claimDTO.setDate(claim.getDate());
	            claimDTO.setStatus(claim.getStatus());

	            claimDTOs.add(claimDTO);
	        }

	        logger.info("getClaimsByPolicyNumber: Retrieved claims for policy number {} successfully.", policyNumber);
	        return new PageImpl<>(claimDTOs, pageable, claimPage.getTotalElements());
	    }
	    
	    @Override
	    public Page<ClaimDto> getClaimsByPolicyNumberAndDateRange(int policyNumber, LocalDate startDate, LocalDate endDate, int pageNumber, int pageSize) {
	        InsurancePolicy insurancePolicy = policyRepository.findBypolicyNo(policyNumber);
	        if (insurancePolicy == null) {
	            throw new PolicyNotFoundException("Insurance policy with policy number " + policyNumber + " not found");
	        }

	        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("claimId").descending());

	        List<Claim> claims;

	        if (startDate != null && endDate != null) {
	            claims = claimRepository.findByInsurancePolicy_PolicyNoAndDateBetween(policyNumber, startDate, endDate, pageable);
	        } else {
	            claims = claimRepository.findByInsurancePolicy_PolicyNo(policyNumber, pageable);
	        }

	        Page<Claim> claimPage = new PageImpl<>(claims, pageable, claims.size());

	        List<ClaimDto> claimDTOs = new ArrayList<>();

	        for (Claim claim : claimPage.getContent()) {
	            ClaimDto claimDTO = new ClaimDto();
	            claimDTO.setPolicyNumber(policyNumber);
	            claimDTO.setBankAccountNumber(claim.getBankAccountNumber());
	            claimDTO.setBankIfscCode(claim.getBankIfscCode());
	            claimDTO.setDate(claim.getDate());
	            claimDTO.setStatus(claim.getStatus());

	            claimDTOs.add(claimDTO);
	        }

	        logger.info("getClaimsByPolicyNumberAndDateRange: Retrieved claims for policy number {} within date range successfully.", policyNumber);
	        return new PageImpl<>(claimDTOs, pageable, claimPage.getTotalElements());
	    }


	
//	@Override
//	public Page<ClaimDto> getClaimsByPolicyNumberAndStatus(int policyNumber, ClaimStatus status, int pageNumber, int pageSize) {
//	    InsurancePolicy insurancePolicy = policyRepository.findBypolicyNo(policyNumber);
//
//	    if (insurancePolicy == null) {
//	        throw new PolicyNotFoundException("Insurance policy with policy number " + policyNumber + " not found");
//	    }
//
//	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("claimId").descending());
//
//	    Page<Claim> claimPage = claimRepository.findByPolicyNumberAndStatus(policyNumber, status, pageable);
//
//	    List<ClaimDto> claimDTOs = new ArrayList<>();
//
//	    for (Claim claim : claimPage.getContent()) {
//	        if (claim.getStatus() == status) {  
//	            ClaimDto claimDTO = new ClaimDto();
//	            claimDTO.setPolicyNumber(policyNumber);
//	            claimDTO.setBankAccountNumber(claim.getBankAccountNumber());
//	            claimDTO.setBankIfscCode(claim.getBankIfscCode());
//	            claimDTO.setDate(claim.getDate());
//	            claimDTO.setStatus(claim.getStatus());
//
//	            claimDTOs.add(claimDTO);
//	        }
//	    }
//
//	    return new PageImpl<>(claimDTOs, pageable, claimDTOs.size());
//	}
	
	    public Page<ClaimDto> getClaimsByStatus(ClaimStatus status, int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("claimId").descending());

	        Page<Claim> claimPage = claimRepository.findByStatus(status, pageable);

	        List<ClaimDto> claimDTOs = new ArrayList<>();

	        for (Claim claim : claimPage.getContent()) {
	            if (claim.getStatus() == status) {
	                ClaimDto claimDTO = new ClaimDto();
	                claimDTO.setClaimId(claim.getClaimId());
	                claimDTO.setPolicyNumber(claim.getInsurancePolicy().getPolicyNo());
	                claimDTO.setBankAccountNumber(claim.getBankAccountNumber());
	                claimDTO.setBankIfscCode(claim.getBankIfscCode());
	                claimDTO.setDate(claim.getDate());
	                claimDTO.setStatus(claim.getStatus());

	                claimDTOs.add(claimDTO);
	            }
	        }

	        logger.info("getClaimsByStatus: Retrieved claims by status {} successfully.", status);
	        return new PageImpl<>(claimDTOs, pageable, claimDTOs.size());
	    }

		@Override
		public List<Claim> getClaimsBetweenDates(LocalDate startDate, LocalDate endDate) {
			return claimRepository.findByDateBetween(startDate, endDate);
		}

	    @Override
	    @Transactional
	    public void updateClaimStatus(int claimId, ClaimStatus newStatus) {
	        Claim claim = claimRepository.findById(claimId)
	                .orElseThrow(() -> new ClaimNotFoundException("No claim found with claim id.: " + claimId));

	        claim.setStatus(newStatus);
	        claimRepository.save(claim);

	        logger.info("updateClaimStatus :Claim status updated for claim ID {}: New status: {}", claimId, newStatus);
	    }
}
