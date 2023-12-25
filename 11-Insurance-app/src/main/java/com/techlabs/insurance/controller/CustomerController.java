package com.techlabs.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.insurance.dto.CustomerDto;
import com.techlabs.insurance.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/insurenceapp")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PreAuthorize("hasRole('EMPLOYEE')")
	@GetMapping("/getcustomers")
	public ResponseEntity<?> getAllCustomer(@RequestParam int pageno, @RequestParam int pagesize) {
		Page<CustomerDto> response = customerService.getAllCustomer(pageno, pagesize);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/searchcustomer")
	public ResponseEntity<Page<CustomerDto>> getCustomerSearch(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size,
	    @RequestParam(required = false) String keyword)
	     {

	    Page<CustomerDto> customerDtoPage = customerService.getCustomerSearch(page, size, keyword);

	    return ResponseEntity.ok(customerDtoPage);
	}




	@PostMapping("/addCustomer")
	public ResponseEntity<String> addCustomer(@RequestBody CustomerDto customerDto) {
		customerService.addCustomer(customerDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Customer added successfully.");
	}
}
