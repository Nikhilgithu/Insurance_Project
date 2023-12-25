package com.techlabs.insurance.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.insurance.dto.DocumentDto;
import com.techlabs.insurance.entities.Claim;
import com.techlabs.insurance.entities.Document;
import com.techlabs.insurance.entities.InsurancePolicy;
import com.techlabs.insurance.exceptions.ClaimNotFoundException;
import com.techlabs.insurance.exceptions.PolicyNotFoundException;
import com.techlabs.insurance.repository.ClaimRepository;
import com.techlabs.insurance.repository.DocumentRepository;
import com.techlabs.insurance.repository.InsurencePolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{
	private final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private InsurencePolicyRepository insurencePolicyRepository;
	
	@Autowired
	private ClaimRepository claimRepository;
	
	
	
	 public List<DocumentDto> getAllDocumentsForPolicy(int policyno) {
	        InsurancePolicy insurancePolicy = insurencePolicyRepository.findById(policyno)
	                .orElseThrow(() -> new PolicyNotFoundException("No policy found with policy no.: " + policyno));

	        List<Document> documents = documentRepository.findByInsurancePolicy(insurancePolicy);
	        List<DocumentDto> documentDtos = new ArrayList<DocumentDto>();

	        for (Document document : documents) {
	            DocumentDto documentDto = new DocumentDto();
	            documentDto.setDocumentName(document.getDocumentName());
	            documentDto.setDocumentFile(document.getDocumentFile());
	            documentDtos.add(documentDto);
	        }

	        logger.info("getAllDocumentsForPolicy:Retrieved all documents for policy no.: {} successfully.", policyno);
	        return documentDtos;
	    }

	    public List<DocumentDto> getAllDocumentsForClaims(int policyno, int claimId) {
	        InsurancePolicy insurancePolicy = insurencePolicyRepository.findById(policyno)
	                .orElseThrow(() -> new PolicyNotFoundException("No policy found with policy no.: " + policyno));
	        List<Document> documents = documentRepository.findByInsurancePolicy(insurancePolicy);

	        Claim claim = claimRepository.findById(claimId)
	                .orElseThrow(() -> new ClaimNotFoundException("No claim found with claim id.: " + claimId));
	        List<Document> documents2 = documentRepository.findByClaim(claim);

	        List<DocumentDto> documentDtos = new ArrayList<DocumentDto>();
	        for (Document document : documents) {
	            DocumentDto documentDto = new DocumentDto();
	            documentDto.setDocumentName(document.getDocumentName());
	            documentDto.setDocumentFile(document.getDocumentFile());
	            documentDtos.add(documentDto);
	        }
	        for (Document document : documents2) {
	            DocumentDto documentDto = new DocumentDto();
	            documentDto.setDocumentName(document.getDocumentName());
	            documentDto.setDocumentFile(document.getDocumentFile());
	            documentDtos.add(documentDto);
	        }

	        logger.info("getAllDocumentsForClaims:Retrieved all documents for policy no.: {} and claim id.: {} successfully.", policyno, claimId);
	        return documentDtos;
	    }
}
