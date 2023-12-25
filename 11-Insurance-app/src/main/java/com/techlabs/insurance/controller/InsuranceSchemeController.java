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
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.insurance.dto.InsurenceSchemeDto;
import com.techlabs.insurance.entities.SchemeDetails;
import com.techlabs.insurance.entities.Status;
import com.techlabs.insurance.service.InsurenceSchemeService;
//import com.techlabs.insurance.service.Util;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class InsuranceSchemeController {

	@Autowired
	private InsurenceSchemeService insuranceSchemeService;

//	@Autowired
//	private Util util;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addinsurencescheme")
	public ResponseEntity<String> addInsurenceScheme(@RequestParam("schemeName") String schemeName,
			@RequestParam("image") MultipartFile image, @RequestParam("description") String description,
			@RequestParam("minAmount") double minAmount, @RequestParam("maxAmount") double maxAmount,
			@RequestParam("minInvestment") int minInvestment, @RequestParam("maxInvestment") int maxInvestment,
			@RequestParam("minAge") int minAge, @RequestParam("maxAge") int maxAge,
			@RequestParam("profitRatio") double profitRatio,
			@RequestParam("registrationCommission") double registrationCommission,
			@RequestParam("installmentCommission") double installmentCommission, @RequestParam("planId") int planId,
			@RequestParam("status") Status status) {
		try {
			InsurenceSchemeDto insuranceSchemeDto = new InsurenceSchemeDto();
			insuranceSchemeDto.setSchemeName(schemeName);
			insuranceSchemeDto.setImage(image);
			insuranceSchemeDto.setPlanId(planId);
			SchemeDetails schemeDetails = new SchemeDetails();
			schemeDetails.setDescription(description);
			schemeDetails.setMinAmount(minAmount);
			schemeDetails.setMaxAmount(maxAmount);
			schemeDetails.setMinInvestment(minInvestment);
			schemeDetails.setMaxInvestment(maxInvestment);
			schemeDetails.setMinAge(minAge);
			schemeDetails.setMaxAge(maxAge);
			schemeDetails.setProfitRatio(profitRatio);
			schemeDetails.setRegistrationCommission(registrationCommission);
			schemeDetails.setInstallmentCommission(installmentCommission);

			insuranceSchemeDto.setSchemeDetails(schemeDetails);
			insuranceSchemeDto.setStatus(status);

			insuranceSchemeService.addInsurenceScheme(insuranceSchemeDto);

			return ResponseEntity.status(HttpStatus.CREATED).body("Insurance scheme added successfully.");
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding insurance scheme.");
		}
	}

//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/search")
	public ResponseEntity<List<InsurenceSchemeDto>> searchInsuranceSchemes(@RequestParam String keyword) {
		List<InsurenceSchemeDto> schemes = insuranceSchemeService.getAllInsurenceSchemes(keyword);
		return new ResponseEntity<>(schemes, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateinsurencescheme/{id}")
	public ResponseEntity<String> updateInsurenceScheme(@PathVariable int id,
			@RequestBody InsurenceSchemeDto insuranceSchemeDto) {
		insuranceSchemeService.updateInsurenceScheme(id, insuranceSchemeDto);
		return ResponseEntity.ok("Insurance scheme updated successfully.");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteinsurencescheme/{id}")
	public ResponseEntity<String> deleteInsurenceScheme(@PathVariable int id) {
		insuranceSchemeService.deleteInsurenceSchemeById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Insurance scheme deleted successfully.");
	}

//	@GetMapping("/activeInsurenceSchemes")
//	public ResponseEntity<List<InsurenceSchemeDto>> getActiveInsurenceSchemes() {
//		List<InsurenceSchemeDto> activeSchemes = insuranceSchemeService.getActiveInsurenceSchemes();
//		return ResponseEntity.ok(activeSchemes);
//	}
	@GetMapping("/getactiveschemesandplans")
	public ResponseEntity<List<InsurenceSchemeDto>> getActiveSchemesWithActivePlans() {
		List<InsurenceSchemeDto> activeSchemes = insuranceSchemeService.getActiveSchemesandPlans();
		return ResponseEntity.ok(activeSchemes);
	}

	@GetMapping("/getactiveschemes/{planid}")
	public ResponseEntity<List<InsurenceSchemeDto>> getActiveSchemesByPlanid(@PathVariable int planid) {
		List<InsurenceSchemeDto> activeSchemes = insuranceSchemeService.getActiveSchemesByPlanid(planid);
		return ResponseEntity.ok(activeSchemes);
	}

	@GetMapping("/getinsurenceschemes")
	public ResponseEntity<Page<InsurenceSchemeDto>> getAllInsurenceSchemesPageWise(
			@RequestParam(defaultValue = "0") int pageno, @RequestParam(defaultValue = "10") int pagesize) {
		Page<InsurenceSchemeDto> schemes = insuranceSchemeService.getAllInsurenceSchemesPageWise(pageno, pagesize);
		return ResponseEntity.ok(schemes);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/schemebystatus")
	public ResponseEntity<?> getInsurenceSchemeByStatus(@RequestParam Status status, @RequestParam int pageno,
			@RequestParam int pagesize) {
		Page<InsurenceSchemeDto> schemeDtos = insuranceSchemeService.getInsurenceSchemesByStatus(status, pageno,
				pagesize);
		return new ResponseEntity<>(schemeDtos, HttpStatus.OK);
	}

	@PutMapping("/updateschemestatus/{schemeId}")
	public ResponseEntity<?> updateSchemeStatus(@PathVariable int schemeId, @RequestParam Status status) {

		insuranceSchemeService.updateInsurenceSchemeStatus(schemeId, status);
		return ResponseEntity.ok("Insurance scheme status updated successfully.");
	}
}
