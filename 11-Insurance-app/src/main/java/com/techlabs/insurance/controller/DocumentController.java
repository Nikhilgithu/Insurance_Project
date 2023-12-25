package com.techlabs.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.DocumentDto;
import com.techlabs.insurance.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class DocumentController {
	@Autowired
	private DocumentService documentService;
	
	@GetMapping("/policy/documents/{policyNo}")
    public ResponseEntity<?> listDocumentsForPolicy(@PathVariable int policyNo) {
        List<DocumentDto> documents = documentService.getAllDocumentsForPolicy(policyNo);
        return ResponseEntity.ok(documents);
    }
	
	@GetMapping("/claim/documents/{policyNo}/{claimId}")
    public ResponseEntity<?> listDocumentsForPolicy(@PathVariable int policyNo, @PathVariable int claimId) {
        List<DocumentDto> documents = documentService.getAllDocumentsForClaims(policyNo, claimId);
        return ResponseEntity.ok(documents);
    }
}
