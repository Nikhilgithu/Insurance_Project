package com.techlabs.insurance.service;

import java.util.List;

import com.techlabs.insurance.dto.DocumentDto;

public interface DocumentService {
	public List<DocumentDto> getAllDocumentsForPolicy(int policyno);
	public List<DocumentDto> getAllDocumentsForClaims(int policyno, int claimId);
}
