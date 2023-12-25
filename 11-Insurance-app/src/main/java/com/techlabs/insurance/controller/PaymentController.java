package com.techlabs.insurance.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.PaymentDto;
import com.techlabs.insurance.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/addpayment")
	public ResponseEntity<?> paymentOnPurchase(@RequestBody PaymentDto paymentDto) {
		String response = paymentService.addPayment(paymentDto);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getpayment/{policyid}")
	public ResponseEntity<?> paymentByPolicyId(@PathVariable int policyid,
			@RequestParam int pageno, @RequestParam int pagesize){
		Page<PaymentDto> response = paymentService.getPaymentsbyPolicyid(policyid, pageno, pagesize);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/payments/{policyid}")
	public ResponseEntity<Page<PaymentDto>> getPaymentsByPolicyId(
	        @PathVariable int policyid,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Page<PaymentDto> paymentsDtoPage = paymentService.getPaymentsbyPolicyid(policyid, startDate, endDate, page, size);

	    return ResponseEntity.ok(paymentsDtoPage);
	}

}
