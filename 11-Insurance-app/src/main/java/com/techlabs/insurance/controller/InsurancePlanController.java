package com.techlabs.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.InsurencePlanDto;
import com.techlabs.insurance.service.InsurancePlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class InsurancePlanController {

	@Autowired
	private InsurancePlanService insurancePlanService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addinsuranceplan")
	public ResponseEntity<String> addInsurancePlan(@RequestBody InsurencePlanDto insurancePlanDto) {
		insurancePlanService.addInsurancePlan(insurancePlanDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Insurance plan added successfully.");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateinsuranceplan/{id}")
	public ResponseEntity<String> updateInsurancePlan(@PathVariable int id,
			@RequestBody InsurencePlanDto updatedInsurancePlanDto) {
		insurancePlanService.updateInsurancePlan(id, updatedInsurancePlanDto);
		return ResponseEntity.status(HttpStatus.OK).body("Insurance plan updated successfully.");
	}

	@DeleteMapping("/deleteinsuranceplan/{id}")
	public ResponseEntity<String> deleteInsurancePlan(@PathVariable int id) {
		insurancePlanService.deleteInsurancePlanById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Insurance plan deleted successfully.");
	}

	 @GetMapping("/getactiveinsuranceplans")
	    public ResponseEntity<List<InsurencePlanDto>> getActiveInsurancePlans() {
	        List<InsurencePlanDto> activePlans = insurancePlanService.getActiveInsurancePlans();
	        return ResponseEntity.ok(activePlans);
	   }
	 
	 @PreAuthorize("hasRole('ADMIN')")
	 @GetMapping("/getallplans")
    public ResponseEntity<List<InsurencePlanDto>> getAllInsurencePlans() {
        List<InsurencePlanDto> plans = insurancePlanService.getAllInsurencePlans();
        return ResponseEntity.ok(plans);
    }
	 
	 @PreAuthorize("hasRole('ADMIN')")
	 @GetMapping("/getallinsurenceplans")
    public ResponseEntity<Page<InsurencePlanDto>> getAllInsurencePlansPageWise(
            @RequestParam(defaultValue = "0") int pageno,
            @RequestParam(defaultValue = "10") int pagesize) {
        Page<InsurencePlanDto> plans = insurancePlanService.getAllInsurencePlansPageWise(pageno, pagesize);
        return ResponseEntity.ok(plans);
    }
}
