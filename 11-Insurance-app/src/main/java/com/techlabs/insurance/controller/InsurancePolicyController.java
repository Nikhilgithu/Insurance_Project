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

import com.techlabs.insurance.dto.InsurancePolicyDto;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.service.InsurencePolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class InsurancePolicyController {

	@Autowired
	private InsurencePolicyService policyService;
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/addpolicy")
	 public ResponseEntity<String> addInsurancePolicy(
	            @ModelAttribute InsurancePolicyDto insurancePolicyDto,
	            @RequestParam("documentFiles") List<MultipartFile> documentFiles) {

	        try {
	        	policyService.addInsurancePolicy(insurancePolicyDto, documentFiles);
	            return ResponseEntity.ok("Insurance policy added successfully.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error adding insurance policy: " + e.getMessage());
	        }
	    }
	
	
	@PreAuthorize("hasRole('EMPLOYEE')")
	@PutMapping("/updatestatus/{policyNo}/{newStatus}")
	public ResponseEntity<?> updatePolicyStatusByPolicyNo(@PathVariable(name="policyNo") int policyNo,
			@PathVariable(name="newStatus") Status newStatus) {
		String response = policyService.updatePolicyStatusByPolicyNo(policyNo, newStatus);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
//	
//	 @PostMapping("/adddocument")
//	    public ResponseEntity<String> addInsurancePolicy(
//	    		  @ModelAttribute InsurancePolicyDto insurancePolicyDto,
//	    		    @ModelAttribute MultipartFile documentFile) {
//
//	        try {
//	        	policyService.addInsurancePolicy(insurancePolicyDto, documentFile);
//	            return ResponseEntity.ok("Insurance policy added successfully.");
//	        } catch (Exception e) {
//	            return ResponseEntity.status(500).body("Error adding insurance policy: " + e.getMessage());
//	        }
//	    }
	
	@GetMapping("/status/{status}")
	public ResponseEntity<?> getPoliciesByStatus(@PathVariable Status status,
			@RequestParam int pageno, @RequestParam int pagesize) {
		Page<InsurancePolicyDto> policyDtos = policyService.getPoliciesByStatus(status, pageno, pagesize);
		return ResponseEntity.ok(policyDtos);
	}
	
	@GetMapping("/getpolicybycustomer/{customerId}")
	public ResponseEntity<Page<InsurancePolicyDto>> getInsurancePoliciesByCustomerId(
	        @PathVariable int customerId,
	        @RequestParam int pageno,
	        @RequestParam int pagesize) {

	    Page<InsurancePolicyDto> policyDtosPage = policyService.getInsurancePoliciesByCustomerId(customerId, pageno, pagesize);

	    return ResponseEntity.ok(policyDtosPage);
	}
	@GetMapping("/getpolicybydate/{customerId}")
	public ResponseEntity<Page<InsurancePolicyDto>> getInsurancePoliciesByCustomerIdAndDateRange(
	        @PathVariable int customerId,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
	        @RequestParam int pageno,
	        @RequestParam int pagesize) {

	    Page<InsurancePolicyDto> policyDtosPage = policyService.getInsurancePoliciesByCustomerIdAndDateRange(
	            customerId, startDate, endDate, pageno, pagesize);

	    return ResponseEntity.ok(policyDtosPage);
	}
}
