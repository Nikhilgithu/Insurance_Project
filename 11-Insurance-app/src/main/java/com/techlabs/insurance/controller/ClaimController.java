package com.techlabs.insurance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.ClaimDto;
import com.techlabs.insurance.entities.Claim;
import com.techlabs.insurance.entities.ClaimStatus;
import com.techlabs.insurance.service.ClaimService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class ClaimController {

	@Autowired
	private ClaimService claimService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/addclaim")
	public ResponseEntity<String> addClaim(@ModelAttribute ClaimDto claimDto,
			@RequestParam("documentFiles") List<MultipartFile> documentFiles) {

		try {
			claimService.addClaim(claimDto, documentFiles);
			return ResponseEntity.ok("Claim added successfully");
		}catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File size is too large. File size should be less tahn 2MB");
		}
	}

	@GetMapping("/getbypolicynumber/{policyNumber}")
	public ResponseEntity<?> getClaimsByPolicyNumber(
	            @PathVariable int policyNumber,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {

		Page<ClaimDto> claims = claimService.getClaimsByPolicyNumber(policyNumber, page, size);
		return new ResponseEntity<> (claims, HttpStatus.OK);
	}
	
//	@GetMapping("/claimsbystatus/{policyNumber}")
//	 public Page<ClaimDto> getClaimsByPolicyNumberAndStatus(
//			 @PathVariable int policyNumber,
//	     @RequestParam ClaimStatus status,
//	     @RequestParam(defaultValue = "0") int pageNumber,
//        @RequestParam(defaultValue = "10") int pageSize)
//	 {
//	     return claimService.getClaimsByPolicyNumberAndStatus(policyNumber, status, pageNumber, pageSize);
//	 }
	
	@PreAuthorize("hasRole('EMPLOYEE')")
	 @GetMapping("/claimsbystatus")
	 public ResponseEntity<?> getClaimsByStatus(@RequestParam(name="status") ClaimStatus status,
	    @RequestParam(defaultValue = "0") int pageno, @RequestParam(defaultValue = "10") int pagesize){
	     return new ResponseEntity<> (claimService.getClaimsByStatus(status, pageno, pagesize), HttpStatus.OK);
	 }
	
	
	@PreAuthorize("hasRole('EMPLOYEE')")
	@PutMapping("/updateclaimstatus/{claimId}")
	public ResponseEntity<?> updateClaimStatus(@PathVariable int claimId, @RequestParam(name="status") ClaimStatus newStatus){
		claimService.updateClaimStatus(claimId, newStatus);
		return new ResponseEntity<> ("Claim status updated successfully", HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/claimsBetweenDates")
	public ResponseEntity<List<Claim>> getClaimsBetweenDates(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

		List<Claim> claims = claimService.getClaimsBetweenDates(startDate, endDate);
		return ResponseEntity.ok(claims);
	}
	@GetMapping("/claimbypolicyanddate/{policyNumber}")
	public ResponseEntity<Page<ClaimDto>> getClaimsByPolicyNumberAndDateRange(
	    @PathVariable int policyNumber,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size) {

	    Page<ClaimDto> claimDTOsPage = claimService.getClaimsByPolicyNumberAndDateRange(policyNumber, startDate, endDate, page, size);

	    return ResponseEntity.ok(claimDTOsPage);
	}

	 
}
